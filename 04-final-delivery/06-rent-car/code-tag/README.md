# Code and tags / Código y tags: RentaMóvil

## Original repositories / Repositorios originales

Every original working repository of the project must be listed here and must be **public**. The instructor compares what was delivered against these repositories, so they must stay public and keep their full history until the review ends.

Todos los repositorios originales de trabajo del proyecto deben estar aquí y deben ser **públicos**. El instructor contrasta lo entregado contra estos repositorios, así que deben seguir públicos y con todo su historial hasta terminar la revisión.

If one is missing, add a row. If one is empty or no longer used, write that in the commit column. The visibility shown was checked on 2026-11-04.

Si falta alguno, agregue la fila. Si uno está vacío o ya no se usa, indíquelo en la columna del commit. La visibilidad indicada se revisó el 2026-11-04.

| Component / Componente | Repository / Repositorio | Status / Estado | Access for `ariel5253` / Acceso de `ariel5253` | Commit of tag `final-delivery-2026` / Commit del tag | Main version ZIP / ZIP versión main | Full export ZIP / ZIP completo |
|---|---|---|---|---|---|---|
| Documentation | [code-sena/rtm-docs](https://github.com/code-sena/rtm-docs) | **PRIVATE: make it public / PRIVADO: hacerlo público** | Yes / Sí | | | |
| API gateway | [RentaMovil/rtm-api-gateway](https://github.com/RentaMovil/rtm-api-gateway) | Public / Público | **Add / Agregar** | | | |
| Database | [RentaMovil/rtm-booking-db](https://github.com/RentaMovil/rtm-booking-db) | Public / Público | **Add / Agregar** | | | |
| Backend | [RentaMovil/rtm-booking-reservation](https://github.com/RentaMovil/rtm-booking-reservation) | Public / Público | **Add / Agregar** | | | |
| Infrastructure | [RentaMovil/rtm-deploy](https://github.com/RentaMovil/rtm-deploy) | Public / Público<br>**No `main`: create it from `develop` / Sin `main`: créela desde `develop`** | **Add / Agregar** | | | |
| Documentation | [RentaMovil/rtm-documentation](https://github.com/RentaMovil/rtm-documentation) | Public / Público | **Add / Agregar** | | | |
| Backend | [RentaMovil/rtm-fleet-maintenance](https://github.com/RentaMovil/rtm-fleet-maintenance) | Public / Público | **Add / Agregar** | | | |
| Database | [RentaMovil/rtm-fleet-maintenance-db](https://github.com/RentaMovil/rtm-fleet-maintenance-db) | Public / Público | **Add / Agregar** | | | |
| Web frontend | [RentaMovil/rtm-frontend](https://github.com/RentaMovil/rtm-frontend) | Public / Público<br>Default branch `develop`: update `main` / Rama por defecto `develop`: actualice `main` | **Add / Agregar** | | | |
| Backend | [RentaMovil/rtm-iam](https://github.com/RentaMovil/rtm-iam) | Public / Público | **Add / Agregar** | | | |
| Database | [RentaMovil/rtm-iam-db](https://github.com/RentaMovil/rtm-iam-db) | Public / Público | **Add / Agregar** | | | |
| Backend | [RentaMovil/rtm-payment-billing](https://github.com/RentaMovil/rtm-payment-billing) | Public / Público | **Add / Agregar** | | | |
| Database | [RentaMovil/rtm-payment-billing-db](https://github.com/RentaMovil/rtm-payment-billing-db) | Public / Público | **Add / Agregar** | | | |
| Backend | [RentaMovil/rtm-telemetry-gps](https://github.com/RentaMovil/rtm-telemetry-gps) | Public / Público | **Add / Agregar** | | | |
| Database | [RentaMovil/rtm-telemetry-gps-db](https://github.com/RentaMovil/rtm-telemetry-gps-db) | Public / Público | **Add / Agregar** | | | |

Before making a repository public, remove `.env` files and change any password, token or key that was ever committed.

Antes de hacer público un repositorio, quite los archivos `.env` y cambie toda contraseña, token o llave que se haya subido alguna vez.

The instructor account `ariel5253` needs write access to every repository, so pending items can be corrected for the software registration. Add it as a collaborator where the table says so.

La cuenta del instructor, `ariel5253`, necesita acceso de escritura en cada repositorio, para poder corregir pendientes del registro de software. Agréguela como colaboradora donde la tabla lo indique.

## Why two ZIP files / Por qué dos ZIP

| File / Archivo | Content / Contenido | Purpose / Para qué sirve |
|---|---|---|
| `<repo>-final-delivery-2026-main.zip` | Only the code of `main` at tag `final-delivery-2026` / Solo el código de `main` en el tag | The exact version delivered, used for software registration / La versión exacta entregada, la que se usa para registro de software |
| `<repo>-full-export.zip` | The whole repository: all branches, tags and history / Todo el repositorio: ramas, tags e historial | Proof of the work done and a complete backup / Prueba del trabajo realizado y respaldo completo |

## Where the ZIP files live / Dónde quedan los ZIP

The ZIP files do not go in this folder. They are attached to a GitHub Release in the fork of the person who delivers, named `final-delivery`, and linked in the table above.

Los ZIP no van en esta carpeta. Se adjuntan a un Release de GitHub llamado `final-delivery` en el fork de quien entrega, y se enlazan en la tabla de arriba.

## Integrity / Integridad

Copy the SHA-256 of every ZIP into [`SHA256SUMS.txt`](SHA256SUMS.txt). / Copie el SHA-256 de cada ZIP en ese archivo.
