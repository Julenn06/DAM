#!/usr/bin/env python3
"""
Herramienta para extraer metadatos de páginas web (título, descripción, etc)
"""
import sys
import requests
from bs4 import BeautifulSoup

def extract_metadata(url):
    """Extrae metadatos de una página web"""
    try:
        response = requests.get(url, timeout=10)
        soup = BeautifulSoup(response.text, 'html.parser')
        
        metadata = {
            'url': url,
            'title': None,
            'description': None,
            'keywords': None,
            'author': None,
            'og_title': None,
            'og_description': None,
            'og_image': None,
        }
        
        # Título
        if soup.title:
            metadata['title'] = soup.title.string
        
        # Meta tags
        for meta in soup.find_all('meta'):
            name = meta.get('name', '').lower()
            property = meta.get('property', '').lower()
            content = meta.get('content', '')
            
            if name == 'description':
                metadata['description'] = content
            elif name == 'keywords':
                metadata['keywords'] = content
            elif name == 'author':
                metadata['author'] = content
            elif property == 'og:title':
                metadata['og_title'] = content
            elif property == 'og:description':
                metadata['og_description'] = content
            elif property == 'og:image':
                metadata['og_image'] = content
        
        return metadata
        
    except Exception as e:
        print(f"Error: {e}")
        return None

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python extract_metadata.py URL")
        sys.exit(1)
    
    target_url = sys.argv[1]
    
    print(f"Extrayendo metadatos de: {target_url}\n")
    
    metadata = extract_metadata(target_url)
    
    if metadata:
        print("--- Metadatos ---")
        print(f"Título: {metadata['title']}")
        print(f"Descripción: {metadata['description']}")
        print(f"Keywords: {metadata['keywords']}")
        print(f"Autor: {metadata['author']}")
        
        print("\n--- Open Graph ---")
        print(f"OG Título: {metadata['og_title']}")
        print(f"OG Descripción: {metadata['og_description']}")
        print(f"OG Imagen: {metadata['og_image']}")
