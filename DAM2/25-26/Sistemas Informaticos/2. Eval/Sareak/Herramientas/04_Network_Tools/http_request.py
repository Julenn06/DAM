#!/usr/bin/env python3
"""
Herramienta para hacer peticiones HTTP GET/POST y ver respuestas
"""
import sys
import requests
import json

def make_request(url, method='GET', headers=None, data=None):
    """Realiza una petición HTTP"""
    print(f"Realizando petición {method} a: {url}\n")
    
    try:
        if method == 'GET':
            response = requests.get(url, headers=headers, timeout=10)
        elif method == 'POST':
            response = requests.post(url, headers=headers, data=data, timeout=10)
        else:
            print(f"Método no soportado: {method}")
            return
        
        print(f"--- Estado ---")
        print(f"Código: {response.status_code} {response.reason}")
        
        print(f"\n--- Cabeceras de respuesta ---")
        for key, value in response.headers.items():
            print(f"{key}: {value}")
        
        print(f"\n--- Contenido ---")
        content_type = response.headers.get('Content-Type', '')
        
        if 'application/json' in content_type:
            try:
                print(json.dumps(response.json(), indent=2, ensure_ascii=False))
            except:
                print(response.text)
        else:
            print(response.text[:1000])  # Primeros 1000 caracteres
            if len(response.text) > 1000:
                print(f"\n... ({len(response.text)} caracteres en total)")
        
        print(f"\n--- Información adicional ---")
        print(f"Tiempo de respuesta: {response.elapsed.total_seconds():.2f}s")
        print(f"Tamaño: {len(response.content)} bytes")
        
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python http_request.py URL [GET|POST] [data]")
        print("Ejemplo: python http_request.py https://api.github.com")
        sys.exit(1)
    
    target_url = sys.argv[1]
    req_method = sys.argv[2].upper() if len(sys.argv) > 2 else 'GET'
    req_data = sys.argv[3] if len(sys.argv) > 3 else None
    
    make_request(target_url, req_method, data=req_data)
