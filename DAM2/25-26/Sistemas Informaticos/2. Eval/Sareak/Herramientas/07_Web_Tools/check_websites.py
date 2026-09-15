#!/usr/bin/env python3
"""
Herramienta para verificar el estado de múltiples sitios web
"""
import sys
import requests
from concurrent.futures import ThreadPoolExecutor, as_completed

def check_website(url):
    """Verifica el estado de un sitio web"""
    if not url.startswith('http'):
        url = 'https://' + url
    
    try:
        response = requests.get(url, timeout=10)
        return {
            'url': url,
            'status': response.status_code,
            'time': response.elapsed.total_seconds(),
            'success': response.status_code == 200
        }
    except Exception as e:
        return {
            'url': url,
            'status': None,
            'time': None,
            'success': False,
            'error': str(e)
        }

def check_websites(urls):
    """Verifica múltiples sitios web en paralelo"""
    results = []
    
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = {executor.submit(check_website, url): url for url in urls}
        
        for future in as_completed(futures):
            result = future.result()
            results.append(result)
            
            if result['success']:
                print(f"✓ {result['url']} - OK ({result['status']}) - {result['time']:.2f}s")
            else:
                error = result.get('error', 'Sin respuesta')
                print(f"✗ {result['url']} - ERROR - {error}")
    
    return results

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python check_websites.py URL1 URL2 URL3 ...")
        print("      python check_websites.py --file urls.txt")
        sys.exit(1)
    
    if '--file' in sys.argv:
        file_path = sys.argv[sys.argv.index('--file') + 1]
        with open(file_path, 'r') as f:
            websites = [line.strip() for line in f if line.strip()]
    else:
        websites = sys.argv[1:]
    
    print(f"Verificando {len(websites)} sitio(s)...\n")
    
    results = check_websites(websites)
    
    print(f"\n--- Resumen ---")
    up = sum(1 for r in results if r['success'])
    down = len(results) - up
    print(f"Disponibles: {up}/{len(results)}")
    print(f"No disponibles: {down}/{len(results)}")
