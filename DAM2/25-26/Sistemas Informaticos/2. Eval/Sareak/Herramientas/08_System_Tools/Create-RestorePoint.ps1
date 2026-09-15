# Herramienta PowerShell para crear un punto de restauración del sistema
# Create-RestorePoint.ps1

param(
    [string]$Description = "Punto de restauración manual"
)

Write-Host "=== Crear Punto de Restauración ===" -ForegroundColor Cyan
Write-Host ""

# Verificar permisos de administrador
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $isAdmin) {
    Write-Host "✗ Este script requiere permisos de administrador" -ForegroundColor Red
    exit 1
}

try {
    Write-Host "Creando punto de restauración: $Description" -ForegroundColor Yellow
    
    # Habilitar la protección del sistema si no está habilitada
    Enable-ComputerRestore -Drive "C:\"
    
    # Crear el punto de restauración
    Checkpoint-Computer -Description $Description -RestorePointType "MODIFY_SETTINGS"
    
    Write-Host "✓ Punto de restauración creado exitosamente" -ForegroundColor Green
    
    # Listar puntos de restauración recientes
    Write-Host "`n--- Puntos de Restauración Disponibles ---" -ForegroundColor Yellow
    Get-ComputerRestorePoint | Select-Object -Last 5 | Format-Table SequenceNumber, CreationTime, Description -AutoSize
    
} catch {
    Write-Host "✗ Error al crear punto de restauración: $_" -ForegroundColor Red
    exit 1
}
