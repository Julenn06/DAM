#!/usr/bin/env python3
"""
Herramienta para buscar archivos por nombre o patrón
"""
import sys
import os
import fnmatch

def find_files(directory, pattern="*", recursive=True):
    """Busca archivos que coincidan con el patrón"""
    matches = []
    
    if recursive:
        for root, dirs, files in os.walk(directory):
            for filename in files:
                if fnmatch.fnmatch(filename.lower(), pattern.lower()):
                    full_path = os.path.join(root, filename)
                    matches.append(full_path)
    else:
        for filename in os.listdir(directory):
            full_path = os.path.join(directory, filename)
            if os.path.isfile(full_path) and fnmatch.fnmatch(filename.lower(), pattern.lower()):
                matches.append(full_path)
    
    return matches

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python find_files.py patron [directorio] [--no-recursive]")
        print("Ejemplo: python find_files.py *.txt C:\\Users")
        sys.exit(1)
    
    search_pattern = sys.argv[1]
    search_dir = sys.argv[2] if len(sys.argv) > 2 and not sys.argv[2].startswith('--') else '.'
    is_recursive = '--no-recursive' not in sys.argv
    
    print(f"Buscando '{search_pattern}' en {os.path.abspath(search_dir)}...")
    if is_recursive:
        print("(búsqueda recursiva)\n")
    
    results = find_files(search_dir, search_pattern, is_recursive)
    
    if results:
        print(f"✓ Encontrados {len(results)} archivo(s):\n")
        for file_path in results:
            size = os.path.getsize(file_path)
            print(f"  {file_path} ({size} bytes)")
    else:
        print("✗ No se encontraron archivos")
