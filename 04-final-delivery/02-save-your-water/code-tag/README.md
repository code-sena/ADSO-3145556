# Code and tags / Código y tags: Cuida Tu Agua

## Original repositories / Repositorios originales

Every original working repository of the project must be listed here and must be **public**. The instructor compares what was delivered against these repositories, so they must stay public and keep their full history until the review ends.

Todos los repositorios originales de trabajo del proyecto deben estar aquí y deben ser **públicos**. El instructor contrasta lo entregado contra estos repositorios, así que deben seguir públicos y con todo su historial hasta terminar la revisión.

If one is missing, add a row. If one is empty or no longer used, write that in the commit column. The visibility shown was checked on 2026-11-04.

Si falta alguno, agregue la fila. Si uno está vacío o ya no se usa, indíquelo en la columna del commit. La visibilidad indicada se revisó el 2026-11-04.

| Component / Componente | Repository / Repositorio | Status / Estado | Access for `ariel5253` / Acceso de `ariel5253` | Commit of tag `final-delivery-2026` / Commit del tag | Main version ZIP / ZIP versión main | Full export ZIP / ZIP completo |
|---|---|---|---|---|---|---|
| Documentation | [code-sena/sy-water-docs](https://github.com/code-sena/sy-water-docs) | **PRIVATE: make it public / PRIVADO: hacerlo público** | Yes / Sí | | | |
| API gateway | [cuida-tu-agua/api-gateway](https://github.com/cuida-tu-agua/api-gateway) | Public / Público | **Add / Agregar** | | | |
| Device / firmware | [cuida-tu-agua/cuida-tu-agua-device-ESP](https://github.com/cuida-tu-agua/cuida-tu-agua-device-ESP) | Public / Público<br>**No `main`: create it from `dev` / Sin `main`: créela desde `dev`** | **Add / Agregar** | | | |
| Web frontend | [cuida-tu-agua/cuida-tu-agua-frontNEW](https://github.com/cuida-tu-agua/cuida-tu-agua-frontNEW) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Backend | [cuida-tu-agua/iam-backend](https://github.com/cuida-tu-agua/iam-backend) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Infrastructure | [cuida-tu-agua/infra](https://github.com/cuida-tu-agua/infra) | Public / Público | **Add / Agregar** | | | |
| Backend | [cuida-tu-agua/ms-consumption](https://github.com/cuida-tu-agua/ms-consumption) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Database | [cuida-tu-agua/ms-consumption-db](https://github.com/cuida-tu-agua/ms-consumption-db) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Database | [cuida-tu-agua/ms-device-db](https://github.com/cuida-tu-agua/ms-device-db) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Backend | [cuida-tu-agua/ms-devices](https://github.com/cuida-tu-agua/ms-devices) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Database | [cuida-tu-agua/ms-iam-db](https://github.com/cuida-tu-agua/ms-iam-db) | Public / Público | **Add / Agregar** | | | |
| Database | [cuida-tu-agua/ms-notification-db](https://github.com/cuida-tu-agua/ms-notification-db) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Backend | [cuida-tu-agua/ms-notifications](https://github.com/cuida-tu-agua/ms-notifications) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Backend | [cuida-tu-agua/ms-places](https://github.com/cuida-tu-agua/ms-places) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Database | [cuida-tu-agua/ms-places-db](https://github.com/cuida-tu-agua/ms-places-db) | Public / Público | **Add / Agregar** | | | |
| Backend | [cuida-tu-agua/ms-valve](https://github.com/cuida-tu-agua/ms-valve) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |
| Database | [cuida-tu-agua/ms-valve-db](https://github.com/cuida-tu-agua/ms-valve-db) | Public / Público<br>Default branch `dev`: update `main` / Rama por defecto `dev`: actualice `main` | **Add / Agregar** | | | |

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
