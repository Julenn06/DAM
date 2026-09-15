#!/usr/bin/env python3
"""
Herramienta para extraer todos los enlaces de una página web
"""
import sys
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse

def extract_links(url):
    """Extrae todos los enlaces de una página web"""
    try:
        response = requests.get(url, timeout=10)
        soup = BeautifulSoup(response.text, 'html.parser')
        
        links = []
        for link in soup.find_all('a', href=True):
            href = link['href']
            full_url = urljoin(url, href)
            links.append({
                'text': link.get_text(strip=True),
                'url': full_url
            })
        
        return links
    except Exception as e:
        print(f"Error: {e}")
        return []

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python extract_links.py URL [--output archivo.txt]")
        sys.exit(1)
    
    target_url = sys.argv[1]
    
    print(f"Extrayendo enlaces de: {target_url}\n")
    
    links = extract_links(target_url)
    
    if '--output' in sys.argv:
        output_file = sys.argv[sys.argv.index('--output') + 1]
        with open(output_file, 'w', encoding='utf-8') as f:
            for link in links:
                f.write(f"{link['url']}\n")
        print(f"✓ {len(links)} enlaces guardados en {output_file}")
    else:
        print(f"✓ Encontrados {len(links)} enlaces:\n")
        for i, link in enumerate(links[:50], 1):  # Primeros 50
            text = link['text'][:50] if link['text'] else '(sin texto)'
            print(f"{i}. {text}")
            print(f"   {link['url']}\n")
        
        if len(links) > 50:
            print(f"... y {len(links) - 50} más")
