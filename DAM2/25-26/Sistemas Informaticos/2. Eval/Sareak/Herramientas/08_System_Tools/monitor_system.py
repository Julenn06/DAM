#!/usr/bin/env python3
"""
Herramienta para monitorear el uso de CPU y memoria en tiempo real
"""
import sys
import psutil
import time

def monitor_system(interval=1, duration=60):
    """Monitorea CPU y memoria del sistema"""
    print(f"Monitoreando sistema cada {interval}s durante {duration}s...")
    print(f"{'Tiempo':<10} {'CPU %':<10} {'RAM %':<10} {'RAM Usada':<15} {'Disco %':<10}")
    print("=" * 65)
    
    start_time = time.time()
    
    try:
        while time.time() - start_time < duration:
            cpu_percent = psutil.cpu_percent(interval=0.5)
            mem = psutil.virtual_memory()
            disk = psutil.disk_usage('/')
            
            elapsed = int(time.time() - start_time)
            ram_used = f"{mem.used / (1024**3):.2f} GB"
            
            print(f"{elapsed:>8}s  {cpu_percent:>6.1f}%  {mem.percent:>6.1f}%  {ram_used:>13}  {disk.percent:>6.1f}%")
            
            time.sleep(interval)
    
    except KeyboardInterrupt:
        print("\n\nMonitoreo detenido por el usuario")

if __name__ == "__main__":
    interval_sec = float(sys.argv[1]) if len(sys.argv) > 1 else 1.0
    duration_sec = int(sys.argv[2]) if len(sys.argv) > 2 else 60
    
    monitor_system(interval_sec, duration_sec)
