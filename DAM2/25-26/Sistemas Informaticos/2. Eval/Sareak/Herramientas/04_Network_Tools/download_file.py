#!/usr/bin/env python3
"""
Herramienta para descargar archivos de URLs
"""
import sys
import requests
import os
from urllib.parse import urlparse

def download_file(url, output_path=None):
    """Descarga un archivo desde una URL"""
    if output_path is None:
        # Extraer nombre del archivo de la URL
        parsed_url = urlparse(url)
        output_path = os.path.basename(parsed_url.path) or 'downloaded_file'
    
    print(f"Descargando: {url}")
    print(f"Destino: {output_path}\n")
    
    try:
        response = requests.get(url, stream=True, timeout=30)
        response.raise_for_status()
        
        total_size = int(response.headers.get('content-length', 0))
        block_size = 8192
        downloaded = 0
        
        with open(output_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=block_size):
                if chunk:
                    f.write(chunk)
                    downloaded += len(chunk)
                    
                    if total_size > 0:
                        percent = (downloaded / total_size) * 100
                        print(f"\rProgreso: {percent:.1f}% ({downloaded}/{total_size} bytes)", end='')
        
        print(f"\n\n✓ Descarga completada: {output_path}")
        print(f"  Tamaño: {downloaded / 1024:.2f} KB")
        
    except Exception as e:
        print(f"\n✗ Error al descargar: {e}")
        if os.path.exists(output_path):
            os.remove(output_path)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python download_file.py URL [archivo_salida]")
        sys.exit(1)
    
    file_url = sys.argv[1]
    output_file = sys.argv[2] if len(sys.argv) > 2 else None
    
    download_file(file_url, output_file)
