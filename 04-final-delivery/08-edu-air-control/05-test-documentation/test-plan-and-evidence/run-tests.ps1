# Ejecuta verify en cada microservicio y guarda logs como evidencia.
# Ejecutar desde la carpeta microservicios/ con Docker Desktop encendido.
$fecha = Get-Date -Format 'yyyy-MM-dd'
$dest  = (Resolve-Path '..').Path + "\07_Pruebas\Evidencias\${fecha}_local_v1.0\08_API_Logs"
New-Item -ItemType Directory -Force $dest | Out-Null
$servicios = 'ea-control-api-gateway','ea-control-ms-security','ea-control-ms-classroom-management','ea-control-ms-sensor-management','ms-environment-monitoring','ea-control-ms-user-management','-ms-user-experience'
foreach ($s in $servicios) {
  Push-Location $s
  $n = $s.TrimStart('-')
  .\mvnw.cmd -B clean verify *>&1 | Tee-Object "$dest\verify_$n.log"
  $res = if ($LASTEXITCODE -eq 0) {'PASS'} else {'FAIL'}
  Copy-Item target\surefire-reports "$dest\surefire_${n}_$res" -Recurse -Force -ErrorAction SilentlyContinue
  Pop-Location
  "$s => $res"
}
