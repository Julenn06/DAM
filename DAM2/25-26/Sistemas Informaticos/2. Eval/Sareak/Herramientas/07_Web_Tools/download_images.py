#!/usr/bin/env python3
"""
Herramienta para descargar todas las imágenes de una página web
"""
import sys
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse
import os

def download_images(url, output_dir='images'):
    """Descarga todas las imágenes de una página web"""
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    try:
        response = requests.get(url, timeout=10)
        soup = BeautifulSoup(response.text, 'html.parser')
        
        images = []
        for img in soup.find_all('img'):
            img_url = img.get('src') or img.get('data-src')
            if img_url:
                images.append(urljoin(url, img_url))
        
        print(f"Encontradas {len(images)} imágenes\n")
        
        downloaded = 0
        for i, img_url in enumerate(images, 1):
            try:
                img_response = requests.get(img_url, timeout=10)
                
                # Extraer nombre del archivo
                parsed = urlparse(img_url)
                filename = os.path.basename(parsed.path) or f'image_{i}.jpg'
                filepath = os.path.join(output_dir, filename)
                
                with open(filepath, 'wb') as f:
                    f.write(img_response.content)
                
                print(f"✓ {i}/{len(images)} - {filename}")
                downloaded += 1
                
            except Exception as e:
                print(f"✗ {i}/{len(images)} - Error: {e}")
        
        print(f"\n✓ {downloaded}/{len(images)} imágenes descargadas en {output_dir}")
        
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python download_images.py URL [directorio_salida]")
        sys.exit(1)
    
    target_url = sys.argv[1]
    output_folder = sys.argv[2] if len(sys.argv) > 2 else 'images'
    
    download_images(target_url, output_folder)
