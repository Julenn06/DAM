#!/usr/bin/env python3
"""
Herramienta para contar palabras, líneas y caracteres en archivos de texto
"""
import sys
import os

def count_text_stats(file_path):
    """Cuenta estadísticas del archivo de texto"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
        lines = content.split('\n')
        words = content.split()
        chars = len(content)
        chars_no_spaces = len(content.replace(' ', '').replace('\n', '').replace('\t', ''))
    
    print(f"Archivo: {file_path}")
    print(f"  Líneas: {len(lines)}")
    print(f"  Palabras: {len(words)}")
    print(f"  Caracteres (con espacios): {chars}")
    print(f"  Caracteres (sin espacios): {chars_no_spaces}")
    print(f"  Tamaño: {os.path.getsize(file_path)} bytes")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python count_text_stats.py archivo.txt")
        sys.exit(1)
    
    count_text_stats(sys.argv[1])
