#!/usr/bin/env bash
# Espera a que todos los servicios del stack respondan (o falla con timeout).
set -euo pipefail

TIMEOUT="${TIMEOUT:-180}"

declare -A ENDPOINTS=(
  [api-gateway]="http://localhost:8080/health"
  [ms-security]="http://localhost:8081/api/v1/auth/health"
  [ms-classroom-management]="http://localhost:3002/health"
  [ms-sensor-management]="http://localhost:3004/health"
)

echo "Esperando servicios (timeout ${TIMEOUT}s)..."
for name in "${!ENDPOINTS[@]}"; do
  url="${ENDPOINTS[$name]}"
  deadline=$((SECONDS + TIMEOUT))
  until curl -fsS -o /dev/null --max-time 3 "$url"; do
    if [ "$SECONDS" -ge "$deadline" ]; then
      echo "  x $name no respondio en $url"
      exit 1
    fi
    sleep 2
  done
  echo "  ok $name"
done

echo "Todos los servicios estan listos."
