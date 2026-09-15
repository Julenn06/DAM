#!/usr/bin/env python3
"""
Herramienta para listar procesos en ejecución con uso de recursos
"""
import sys
import psutil

def list_processes(sort_by='cpu', limit=20):
    """Lista los procesos ordenados por uso de recursos"""
    processes = []
    
    for proc in psutil.process_iter(['pid', 'name', 'cpu_percent', 'memory_percent', 'status']):
        try:
            pinfo = proc.info
            processes.append(pinfo)
        except (psutil.NoSuchProcess, psutil.AccessDenied):
            pass
    
    # Ordenar
    if sort_by == 'cpu':
        processes.sort(key=lambda x: x.get('cpu_percent', 0), reverse=True)
    elif sort_by == 'memory':
        processes.sort(key=lambda x: x.get('memory_percent', 0), reverse=True)
    
    # Mostrar
    print(f"{'PID':<8} {'Nombre':<30} {'CPU %':<10} {'RAM %':<10} {'Estado':<15}")
    print("=" * 80)
    
    for proc in processes[:limit]:
        pid = proc.get('pid', 'N/A')
        name = proc.get('name', 'N/A')[:28]
        cpu = proc.get('cpu_percent', 0)
        mem = proc.get('memory_percent', 0)
        status = proc.get('status', 'N/A')
        
        print(f"{pid:<8} {name:<30} {cpu:>6.1f}%    {mem:>6.2f}%    {status:<15}")

if __name__ == "__main__":
    sort_option = 'cpu'
    limit_num = 20
    
    if '--memory' in sys.argv or '-m' in sys.argv:
        sort_option = 'memory'
    
    if len(sys.argv) > 1 and sys.argv[-1].isdigit():
        limit_num = int(sys.argv[-1])
    
    print(f"Top {limit_num} procesos por {sort_option.upper()}:\n")
    list_processes(sort_option, limit_num)
