#!/usr/bin/env python3
"""
Herramienta para obtener información de una dirección IP (geolocalización)
"""
import sys
import requests
import json

def get_ip_info(ip_address=None):
    """Obtiene información de una IP usando API pública"""
    if ip_address:
        url = f"http://ip-api.com/json/{ip_address}"
    else:
        url = "http://ip-api.com/json/"
    
    try:
        response = requests.get(url, timeout=5)
        data = response.json()
        
        if data['status'] == 'success':
            print(f"IP: {data['query']}")
            print(f"País: {data['country']} ({data['countryCode']})")
            print(f"Región: {data['regionName']}")
            print(f"Ciudad: {data['city']}")
            print(f"Código postal: {data.get('zip', 'N/A')}")
            print(f"Latitud: {data['lat']}")
            print(f"Longitud: {data['lon']}")
            print(f"ISP: {data['isp']}")
            print(f"Organización: {data['org']}")
            print(f"Zona horaria: {data['timezone']}")
        else:
            print(f"Error: {data.get('message', 'No se pudo obtener información')}")
    
    except Exception as e:
        print(f"Error al consultar IP: {e}")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        target_ip = sys.argv[1]
        print(f"Consultando información de: {target_ip}\n")
        get_ip_info(target_ip)
    else:
        print("Consultando información de tu IP pública...\n")
        get_ip_info()
