# ms-user-experience

Microservicio de **preferencias e interacciones del usuario** (puerto `3006`,
esquema PostgreSQL `user_experience`).

Dueño de los datos de interaccion: preferencias, favoritos, ratings de ambientes
y busquedas. No posee usuarios, ambientes ni variables: los referencia por UUID.

## Contrato

`07-api/contracts/openapi/` (repositorio de documentacion, por definir).

## Arranque local

Requiere PostgreSQL en `localhost:5432`.

```bash
$env:POSTGRES_USER = "eduaircontrol"
$env:POSTGRES_PASSWORD = "eduaircontrol"
$env:JWT_SECRET = "una-clave-de-al-menos-32-caracteres-aqui"

.\mvnw.cmd spring-boot:run
```

## Verificacion

```bash
curl http://localhost:3006/health
```
