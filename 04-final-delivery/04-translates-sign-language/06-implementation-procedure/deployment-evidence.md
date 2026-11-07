# Deployment evidence / Evidencia de despliegue: TraduceSeñas

Checked from outside the server on 2026-11-07 at 15:34 UTC (10:34 Colombia). / Verificado desde fuera del servidor el 2026-11-07 a las 15:34 UTC (10:34 en Colombia).

## Web application / Aplicación web

`GET https://traducesenas.tech/` → `200`, `Content-Type: text/html`, `Strict-Transport-Security: max-age=31536000`.

## Backend health / Salud del backend

`GET https://traducesenas.tech/api/health` → `200`

```json
{"status":"ok","version":"1.4.0","services":{"iam":{"status":"up"},"recognition":{"status":"up"},"lexicon":{"status":"up"},"analytics":{"status":"up"},"profile":{"status":"up"}}}
```

The gateway answers `ok` with the backend version `1.4.0` and the five services up. / El gateway responde `ok` con la versión `1.4.0` del backend y los cinco servicios arriba.

## HTTPS certificate / Certificado HTTPS

| Field / Campo | Value / Valor |
|---|---|
| Subject / Sujeto | `CN=traducesenas.tech` |
| Issuer / Emisor | Let's Encrypt (`CN=YE2`) |
| Valid from / Válido desde | 2026-10-16 |
| Valid until / Válido hasta | 2027-01-14 |

## How to repeat it / Cómo repetirla

```bash
curl -i https://traducesenas.tech/api/health
echo | openssl s_client -connect traducesenas.tech:443 -servername traducesenas.tech | openssl x509 -noout -issuer -subject -dates
```

The screenshots planned in `deployment-plan-es.pdf` (Portainer, logs, DevTools, migrations) need access to the server and are not attached. / Las capturas previstas en `deployment-plan-es.pdf` (Portainer, logs, DevTools, migraciones) requieren acceso al servidor y no se adjuntan.
