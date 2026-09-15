#!/usr/bin/env python3
"""
Herramienta para hacer ping a múltiples hosts y verificar conectividad
"""
import sys
import subprocess
import platform

def ping_host(host, count=4):
    """Hace ping a un host"""
    param = '-n' if platform.system().lower() == 'windows' else '-c'
    command = ['ping', param, str(count), host]
    
    try:
        output = subprocess.run(command, capture_output=True, text=True, timeout=10)
        if output.returncode == 0:
            print(f"✓ {host} - ACCESIBLE")
            return True
        else:
            print(f"✗ {host} - NO ACCESIBLE")
            return False
    except Exception as e:
        print(f"✗ {host} - ERROR: {e}")
        return False

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python ping_hosts.py google.com 8.8.8.8 [count]")
        sys.exit(1)
    
    count_arg = 4
    hosts = []
    
    for arg in sys.argv[1:]:
        if arg.isdigit():
            count_arg = int(arg)
        else:
            hosts.append(arg)
    
    print(f"Probando conectividad ({count_arg} pings por host)...\n")
    
    results = []
    for host in hosts:
        result = ping_host(host, count_arg)
        results.append((host, result))
    
    print(f"\n--- Resumen ---")
    accessible = sum(1 for _, r in results if r)
    print(f"Accesibles: {accessible}/{len(results)}")
