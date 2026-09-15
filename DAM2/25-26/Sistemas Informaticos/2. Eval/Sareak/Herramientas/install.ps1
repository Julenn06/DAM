# Script de instalación de dependencias para todas las herramientas
# install.ps1

Write-Host "=== Instalador de Herramientas ===" -ForegroundColor Cyan
Write-Host ""

# Verificar Python
Write-Host "Verificando Python..." -ForegroundColor Yellow
try {
    $pythonVersion = python --version 2>&1
    Write-Host "  ✓ $pythonVersion encontrado" -ForegroundColor Green
} catch {
    Write-Host "  ✗ Python no está instalado" -ForegroundColor Red
    Write-Host "  Descarga Python desde: https://www.python.org/downloads/" -ForegroundColor Yellow
    exit 1
}

# Verificar Node.js
Write-Host "`nVerificando Node.js..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version 2>&1
    Write-Host "  ✓ Node.js $nodeVersion encontrado" -ForegroundColor Green
} catch {
    Write-Host "  ⚠ Node.js no está instalado (opcional para algunas herramientas)" -ForegroundColor Yellow
    Write-Host "  Descarga Node.js desde: https://nodejs.org/" -ForegroundColor Yellow
}

# Instalar dependencias de Python
Write-Host "`nInstalando dependencias de Python..." -ForegroundColor Yellow
try {
    python -m pip install --upgrade pip
    python -m pip install -r requirements.txt
    Write-Host "  ✓ Dependencias de Python instaladas" -ForegroundColor Green
} catch {
    Write-Host "  ✗ Error al instalar dependencias de Python" -ForegroundColor Red
}

# Verificar FFmpeg (opcional)
Write-Host "`nVerificando FFmpeg (opcional para herramientas multimedia)..." -ForegroundColor Yellow
try {
    $ffmpegVersion = ffmpeg -version 2>&1
    Write-Host "  ✓ FFmpeg encontrado" -ForegroundColor Green
} catch {
    Write-Host "  ⚠ FFmpeg no está instalado" -ForegroundColor Yellow
    Write-Host "  Las herramientas multimedia necesitarán FFmpeg" -ForegroundColor Yellow
    Write-Host "  Descarga desde: https://ffmpeg.org/download.html" -ForegroundColor Yellow
}

Write-Host "`n=== Instalación completada ===" -ForegroundColor Cyan
Write-Host "Lee README.md para instrucciones de uso" -ForegroundColor White
