#!/usr/bin/env python3
"""
Herramienta para matar procesos por nombre o PID
"""
import sys
import psutil
import signal

def kill_process(identifier):
    """Mata un proceso por PID o nombre"""
    killed = []
    
    # Si es un número, tratar como PID
    if identifier.isdigit():
        try:
            proc = psutil.Process(int(identifier))
            proc.terminate()
            print(f"✓ Proceso terminado: {proc.name()} (PID: {identifier})")
            killed.append(identifier)
        except psutil.NoSuchProcess:
            print(f"✗ No existe proceso con PID: {identifier}")
        except psutil.AccessDenied:
            print(f"✗ Acceso denegado para PID: {identifier}")
    else:
        # Buscar por nombre
        count = 0
        for proc in psutil.process_iter(['pid', 'name']):
            try:
                if identifier.lower() in proc.info['name'].lower():
                    proc.terminate()
                    print(f"✓ Proceso terminado: {proc.info['name']} (PID: {proc.info['pid']})")
                    killed.append(str(proc.info['pid']))
                    count += 1
            except (psutil.NoSuchProcess, psutil.AccessDenied):
                pass
        
        if count == 0:
            print(f"✗ No se encontraron procesos con nombre: {identifier}")
    
    return killed

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python kill_process.py [PID|nombre_proceso]")
        print("Ejemplo: python kill_process.py 1234")
        print("Ejemplo: python kill_process.py chrome")
        sys.exit(1)
    
    target = sys.argv[1]
    
    print(f"Buscando proceso: {target}\n")
    killed = kill_process(target)
    
    if killed:
        print(f"\n✓ Total de procesos terminados: {len(killed)}")
