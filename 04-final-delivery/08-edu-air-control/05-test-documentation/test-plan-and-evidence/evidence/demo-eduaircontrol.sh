#!/usr/bin/env bash
# demo-eduaircontrol.sh — Flujo de demostracion para sustentacion (6 de noviembre)
# Ejecutar con el stack levantado: bash demo-eduaircontrol.sh
set -euo pipefail

GATEWAY="${GATEWAY_URL:-http://localhost:8080}"
SUPERADMIN_EMAIL="${SUPERADMIN_EMAIL:-admin@eduaircontrol.com}"
SUPERADMIN_PASSWORD="${SUPERADMIN_PASSWORD:?Set SUPERADMIN_PASSWORD}"

echo "============================================"
echo "  EduAirControl — Demo de sustentacion"
echo "============================================"
echo

# ── 1. Login admin ──────────────────────────────────────────────
echo "1) Login como SUPER_ADMIN..."
SA_LOGIN=$(curl -fsS -X POST "$GATEWAY/api/v1/auth/login" \
  -H 'Content-Type: application/json' \
  -d "{\"email\":\"$SUPERADMIN_EMAIL\",\"password\":\"$SUPERADMIN_PASSWORD\"}")
SA_TOKEN=$(echo "$SA_LOGIN" | jq -r .accessToken)
echo "   Token obtenido: $([ -n "$SA_TOKEN" ] && echo 'SI' || echo 'NO')"
echo

# ── 2. Dashboard summary ────────────────────────────────────────
echo "2) Dashboard summary..."
curl -fsS "$GATEWAY/api/v1/dashboard/summary" -H "Authorization: Bearer $SA_TOKEN" | jq '{counts, lastUpdated}'
echo

# ── 3. Ambiente + sensor + medicion ─────────────────────────────
ENVIRONMENT_ID="00000000-0000-4000-8000-000000000401"
TEMPERATURE_VARIABLE_ID="00000000-0000-4000-8000-000000000071"
NOW="$(date -u +%Y-%m-%dT%H:%M:%SZ)"

echo "3) Alta de sensor..."
SENSOR=$(curl -fsS -X POST "$GATEWAY/api/v1/sensors" -H "Authorization: Bearer $SA_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"serialNumber":"DEMO-001","sensorModelId":"00000000-0000-4000-8000-000000000204","sensorStatusId":"00000000-0000-4000-8000-000000000211"}')
SENSOR_ID=$(echo "$SENSOR" | jq -r .sensorId)
echo "   sensor: $SENSOR_ID"

echo "3b) Instalacion del sensor..."
INSTALLATION=$(curl -fsS -X POST "$GATEWAY/api/v1/sensor-installations" \
  -H "Authorization: Bearer $SA_TOKEN" -H 'Content-Type: application/json' \
  -d "{\"sensorId\":\"$SENSOR_ID\",\"educationalEnvironmentId\":\"$ENVIRONMENT_ID\",\"installedAt\":\"$NOW\"}")
INSTALLATION_ID=$(echo "$INSTALLATION" | jq -r .sensorInstallationId)
echo "   instalacion: $INSTALLATION_ID"

echo "3c) Credencial de dispositivo..."
DEVICE=$(curl -fsS -X POST "$GATEWAY/api/v1/device-tokens" -H "Authorization: Bearer $SA_TOKEN" \
  -H 'Content-Type: application/json' -d "{\"subject\":\"$SENSOR_ID\"}")
DEVICE_TOKEN=$(echo "$DEVICE" | jq -r .accessToken)
echo "   device token: $([ -n "$DEVICE_TOKEN" ] && echo 'OK' || echo 'FAIL')"

echo "3d) Ingesta de medicion..."
for attempt in $(seq 1 30); do
  HTTP=$(curl -sS -o /tmp/demo-measurement.json -w '%{http_code}' \
    -X POST "$GATEWAY/api/v1/measurements" -H "Authorization: Bearer $DEVICE_TOKEN" \
    -H 'Content-Type: application/json' \
    -d "{\"sensorInstallationId\":\"$INSTALLATION_ID\",\"variableId\":\"$TEMPERATURE_VARIABLE_ID\",\"value\":23.5,\"measuredAt\":\"$NOW\"}")
  [ "$HTTP" = "201" ] && break
  sleep 2
done
echo "   medicion ingerida (http=$HTTP): $(cat /tmp/demo-measurement.json)"
echo

# ── 4. Historico ────────────────────────────────────────────────
echo "4) Historico de mediciones..."
curl -fsS "$GATEWAY/api/v1/environments/$ENVIRONMENT_ID/measurements?limit=5" \
  -H "Authorization: Bearer $SA_TOKEN" | jq '.[0] | {variableCode, value, measuredAt}'
echo

# ── 5. Alertas ──────────────────────────────────────────────────
echo "5) Alertas activas..."
ALERTS=$(curl -fsS "$GATEWAY/api/v1/alerts?status=active&limit=3" -H "Authorization: Bearer $SA_TOKEN")
echo "$ALERTS" | jq 'length' | xargs -I{} echo "   alertas activas: {}"
ALERT_ID=$(echo "$ALERTS" | jq -r '.[0].id // empty')
if [ -n "$ALERT_ID" ]; then
  echo "   Reconociendo alerta $ALERT_ID..."
  curl -fsS -X POST "$GATEWAY/api/v1/alerts/$ALERT_ID/acknowledge" \
    -H "Authorization: Bearer $SA_TOKEN" | jq '{id, severityCode, acknowledgedAt}'
fi
echo

# ── 6. Exportacion CSV ───────────────────────────────────────────
echo "6) Exportacion CSV..."
curl -fsS -o /tmp/demo-export.csv \
  "$GATEWAY/api/v1/measurements/export?environmentId=$ENVIRONMENT_ID&format=csv" \
  -H "Authorization: Bearer $SA_TOKEN"
echo "   archivo: /tmp/demo-export.csv ($(wc -l < /tmp/demo-export.csv) lineas)"
head -2 /tmp/demo-export.csv
echo

# ── 7. Tenant isolation ─────────────────────────────────────────
echo "7) Tenant isolation (login como USER de otra institucion)..."
# (requiere un usuario USER pre-creado; se omite si no existe)
echo "   [verificar manualmente en la presentacion]"
echo

# ── 8. Health de todos los servicios ────────────────────────────
echo "8) Health de servicios..."
for svc in api-gateway ms-security ms-user-management ms-classroom-management ms-sensor-management ms-environment-monitoring ms-user-experience; do
  port=$(case $svc in api-gateway) echo 8080;; ms-security) echo 8081;; ms-classroom-management) echo 3002;; ms-environment-monitoring) echo 3003;; ms-sensor-management) echo 3004;; ms-user-experience) echo 3006;; ms-user-management) echo 3007;; esac)
  status=$(curl -s -o /dev/null -w '%{http_code}' "http://localhost:$port/health" 2>/dev/null || echo "N/A")
  echo "   $svc ($port): $status"
done
echo

echo "============================================"
echo "  Demo completada"
echo "============================================"