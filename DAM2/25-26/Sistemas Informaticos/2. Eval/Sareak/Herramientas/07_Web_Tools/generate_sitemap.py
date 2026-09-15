#!/usr/bin/env python3
"""
Herramienta para generar sitemap XML de un sitio web
"""
import sys
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse
from datetime import datetime

def crawl_site(base_url, max_pages=50):
    """Rastrea un sitio web y obtiene todas las URLs"""
    visited = set()
    to_visit = [base_url]
    base_domain = urlparse(base_url).netloc
    
    while to_visit and len(visited) < max_pages:
        url = to_visit.pop(0)
        
        if url in visited:
            continue
        
        try:
            response = requests.get(url, timeout=5)
            visited.add(url)
            print(f"✓ Rastreando: {url}")
            
            soup = BeautifulSoup(response.text, 'html.parser')
            
            for link in soup.find_all('a', href=True):
                full_url = urljoin(url, link['href'])
                parsed = urlparse(full_url)
                
                # Solo URLs del mismo dominio
                if parsed.netloc == base_domain and full_url not in visited:
                    # Limpiar fragmentos y parámetros
                    clean_url = f"{parsed.scheme}://{parsed.netloc}{parsed.path}"
                    if clean_url not in visited and clean_url not in to_visit:
                        to_visit.append(clean_url)
        
        except Exception as e:
            print(f"✗ Error en {url}: {e}")
    
    return visited

def generate_sitemap(urls, output_file='sitemap.xml'):
    """Genera un archivo sitemap.xml"""
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('<?xml version="1.0" encoding="UTF-8"?>\n')
        f.write('<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">\n')
        
        for url in sorted(urls):
            f.write('  <url>\n')
            f.write(f'    <loc>{url}</loc>\n')
            f.write(f'    <lastmod>{datetime.now().strftime("%Y-%m-%d")}</lastmod>\n')
            f.write('  </url>\n')
        
        f.write('</urlset>\n')
    
    print(f"\n✓ Sitemap generado: {output_file}")
    print(f"  URLs: {len(urls)}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python generate_sitemap.py URL [max_paginas] [salida.xml]")
        sys.exit(1)
    
    site_url = sys.argv[1]
    max_p = int(sys.argv[2]) if len(sys.argv) > 2 and sys.argv[2].isdigit() else 50
    output = sys.argv[3] if len(sys.argv) > 3 else 'sitemap.xml'
    
    print(f"Rastreando {site_url} (máximo {max_p} páginas)...\n")
    
    urls = crawl_site(site_url, max_p)
    generate_sitemap(urls, output)
