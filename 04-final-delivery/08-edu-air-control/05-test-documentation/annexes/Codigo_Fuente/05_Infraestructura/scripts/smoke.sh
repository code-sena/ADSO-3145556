#!/usr/bin/env bash
# Smoke test end-to-end a través del gateway. Requiere el stack levantado
# (docker compose up -d) y jq instalado.
set -euo pipefail

GATEWAY="${GATEWAY_URL:-http://localhost:8080}"
SUFFIX="$(date +%s)"
EMAIL="smoke-${SUFFIX}@example.com"

echo "== health del gateway =="
curl -fsS "$GATEWAY/health" | jq .

echo "== registro =="
REG=$(curl -fsS -X POST "$GATEWAY/api/v1/auth/register" -H 'Content-Type: application/json' \
  -d "{\"email\":\"$EMAIL\",\"password\":\"SecurePass123!\",\"username\":\"smoke${SUFFIX}\"}")
echo "$REG" | jq '{email: .user.email, roles: .user.roles}'

echo "== login =="
LOGIN=$(curl -fsS -X POST "$GATEWAY/api/v1/auth/login" -H 'Content-Type: application/json' \
  -d "{\"email\":\"$EMAIL\",\"password\":\"SecurePass123!\"}")
TOKEN=$(echo "$LOGIN" | jq -r .accessToken)
REFRESH=$(echo "$LOGIN" | jq -r .refreshToken)
echo "access token obtenido: $([ -n "$TOKEN" ] && echo si || echo no)"

echo "== logout (revoca token) =="
curl -fsS -o /dev/null -w 'logout http=%{http_code}\n' -X POST "$GATEWAY/api/v1/auth/logout" \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d "{\"refreshToken\":\"$REFRESH\",\"allDevices\":false}"

echo "== reutilizar token revocado (debe dar 401 TOKEN_REVOKED) =="
curl -sS -o /dev/null -w 'reuse http=%{http_code}\n' -X POST "$GATEWAY/api/v1/auth/logout" \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{}'

echo "Smoke completado."
