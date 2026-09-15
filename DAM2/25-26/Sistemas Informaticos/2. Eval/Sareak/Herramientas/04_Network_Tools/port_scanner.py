#!/usr/bin/env python3
"""
Herramienta para escanear puertos abiertos en un host
"""
import sys
import socket
from concurrent.futures import ThreadPoolExecutor, as_completed

def scan_port(host, port, timeout=1):
    """Escanea un puerto específico"""
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.settimeout(timeout)
        result = sock.connect_ex((host, port))
        sock.close()
        return port, result == 0
    except:
        return port, False

def scan_ports(host, ports, max_workers=50):
    """Escanea múltiples puertos"""
    print(f"Escaneando {host}...\n")
    open_ports = []
    
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        futures = {executor.submit(scan_port, host, port): port for port in ports}
        
        for future in as_completed(futures):
            port, is_open = future.result()
            if is_open:
                service = get_service_name(port)
                print(f"✓ Puerto {port} ABIERTO - {service}")
                open_ports.append(port)
    
    return open_ports

def get_service_name(port):
    """Obtiene el nombre del servicio común para un puerto"""
    services = {
        20: "FTP-DATA", 21: "FTP", 22: "SSH", 23: "Telnet",
        25: "SMTP", 53: "DNS", 80: "HTTP", 110: "POP3",
        143: "IMAP", 443: "HTTPS", 445: "SMB", 3306: "MySQL",
        3389: "RDP", 5432: "PostgreSQL", 8080: "HTTP-Alt"
    }
    return services.get(port, "Unknown")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python port_scanner.py hostname [puerto_inicio-puerto_fin]")
        print("Ejemplo: python port_scanner.py google.com 1-1000")
        sys.exit(1)
    
    target_host = sys.argv[1]
    
    if len(sys.argv) > 2 and '-' in sys.argv[2]:
        start, end = map(int, sys.argv[2].split('-'))
        port_range = range(start, end + 1)
    else:
        # Puertos comunes
        port_range = [21, 22, 23, 25, 53, 80, 110, 143, 443, 445, 3306, 3389, 5432, 8080]
    
    open_ports = scan_ports(target_host, port_range)
    
    print(f"\n--- Resumen ---")
    print(f"Puertos abiertos: {len(open_ports)}")
    if open_ports:
        print(f"Puertos: {', '.join(map(str, sorted(open_ports)))}")
