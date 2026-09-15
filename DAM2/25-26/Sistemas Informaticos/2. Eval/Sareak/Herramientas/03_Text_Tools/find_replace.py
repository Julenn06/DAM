#!/usr/bin/env python3
"""
Herramienta para buscar y reemplazar texto en archivos
"""
import sys
import os

def find_replace(file_path, search_text, replace_text, output_file=None):
    """Busca y reemplaza texto en un archivo"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    count = content.count(search_text)
    new_content = content.replace(search_text, replace_text)
    
    if output_file is None:
        output_file = file_path
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    print(f"✓ Reemplazadas {count} ocurrencias de '{search_text}' por '{replace_text}'")
    print(f"  Guardado en: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 4:
        print("Uso: python find_replace.py archivo.txt 'buscar' 'reemplazar' [salida.txt]")
        sys.exit(1)
    
    input_file = sys.argv[1]
    search = sys.argv[2]
    replace = sys.argv[3]
    output = sys.argv[4] if len(sys.argv) > 4 else None
    
    find_replace(input_file, search, replace, output)
