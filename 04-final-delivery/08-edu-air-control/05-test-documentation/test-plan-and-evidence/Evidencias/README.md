# Evidencias de pruebas — EduAirControl

Esta carpeta contiene las evidencias de ejecución del `Plan_Pruebas.pdf`.

## Organización

Crear una subcarpeta por fecha, ambiente y versión:

```text
YYYY-MM-DD_<ambiente>_<version>/
├── 00_Reporte_Ejecucion.md
├── 01_Ambiente/
├── 02_Autenticacion/
├── 03_Ambientes_Sensores/
├── 04_Monitoreo_Alertas/
├── 05_Web/
├── 06_Movil/
├── 07_IoT/
├── 08_API_Logs/
└── 09_CI_Despliegue/
```

## Reglas

- Nombrar los archivos con el ID del caso: `SEC-003_PASS_YYYY-MM-DD_01.png`.
- No guardar contraseñas, tokens, API keys, datos personales ni secretos.
- Anonimizar correos y datos de usuarios antes de conservar capturas.
- En cada evidencia registrar caso, fecha, responsable, ambiente y resultado.
- Guardar logs como `.txt` o `.log` cuando sea más útil que una captura.
- Relacionar cada defecto con su caso de prueba y su evidencia.

## Evidencias mínimas recomendadas

- Estado de contenedores y health checks.
- Resultado de `./mvnw -B verify`.
- Resultado de `scripts/smoke.sh`.
- Login exitoso y rechazo de credenciales inválidas.
- Gestión de ambientes y sensores.
- Dashboard, alertas y notificaciones.
- Aplicación móvil comunicándose con el API.
- Lecturas DHT11 y KY-038.
- Logs de API y mensajería sin secretos.
- Resultado del pipeline CI o del despliegue controlado.
