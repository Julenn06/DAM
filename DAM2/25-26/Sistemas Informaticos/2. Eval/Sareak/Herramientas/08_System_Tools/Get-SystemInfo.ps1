# Herramienta PowerShell para obtener información del sistema
# Get-SystemInfo.ps1

Write-Host "=== INFORMACIÓN DEL SISTEMA ===" -ForegroundColor Cyan
Write-Host ""

# Sistema Operativo
Write-Host "--- Sistema Operativo ---" -ForegroundColor Yellow
$os = Get-CimInstance Win32_OperatingSystem
Write-Host "OS: $($os.Caption)"
Write-Host "Versión: $($os.Version)"
Write-Host "Arquitectura: $($os.OSArchitecture)"
Write-Host "Último reinicio: $($os.LastBootUpTime)"
Write-Host ""

# CPU
Write-Host "--- Procesador ---" -ForegroundColor Yellow
$cpu = Get-CimInstance Win32_Processor
Write-Host "Nombre: $($cpu.Name)"
Write-Host "Núcleos: $($cpu.NumberOfCores)"
Write-Host "Threads: $($cpu.NumberOfLogicalProcessors)"
Write-Host ""

# Memoria RAM
Write-Host "--- Memoria RAM ---" -ForegroundColor Yellow
$totalRAM = [math]::Round($os.TotalVisibleMemorySize / 1MB, 2)
$freeRAM = [math]::Round($os.FreePhysicalMemory / 1MB, 2)
$usedRAM = [math]::Round($totalRAM - $freeRAM, 2)
Write-Host "Total: $totalRAM GB"
Write-Host "Usada: $usedRAM GB"
Write-Host "Libre: $freeRAM GB"
Write-Host ""

# Discos
Write-Host "--- Discos ---" -ForegroundColor Yellow
$disks = Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 }
foreach ($disk in $disks) {
    $total = [math]::Round($disk.Size / 1GB, 2)
    $free = [math]::Round($disk.FreeSpace / 1GB, 2)
    $used = [math]::Round($total - $free, 2)
    $percent = [math]::Round(($used / $total) * 100, 1)
    
    Write-Host "$($disk.DeviceID) - Total: $total GB | Usado: $used GB ($percent%) | Libre: $free GB"
}
Write-Host ""

# Red
Write-Host "--- Red ---" -ForegroundColor Yellow
$adapters = Get-NetAdapter | Where-Object { $_.Status -eq 'Up' }
foreach ($adapter in $adapters) {
    Write-Host "Adaptador: $($adapter.Name) - $($adapter.InterfaceDescription)"
    $ip = (Get-NetIPAddress -InterfaceIndex $adapter.ifIndex -AddressFamily IPv4 -ErrorAction SilentlyContinue).IPAddress
    Write-Host "  IP: $ip"
}
