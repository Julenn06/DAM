#!/usr/bin/env python3
"""
Herramienta para formatear código (Python, JavaScript, JSON, etc.)
"""
import sys
import json
import subprocess
import os

def format_json(content):
    """Formatea código JSON"""
    try:
        data = json.loads(content)
        return json.dumps(data, indent=2, ensure_ascii=False)
    except:
        return None

def format_code(file_path, language=None):
    """Formatea código según el lenguaje"""
    if not os.path.exists(file_path):
        print(f"Error: {file_path} no existe")
        return
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Detectar lenguaje por extensión si no se especifica
    if language is None:
        ext = os.path.splitext(file_path)[1].lower()
        language_map = {
            '.py': 'python',
            '.js': 'javascript',
            '.json': 'json',
            '.html': 'html',
            '.css': 'css'
        }
        language = language_map.get(ext, 'unknown')
    
    formatted = None
    
    if language == 'json':
        formatted = format_json(content)
    elif language == 'python':
        # Usar black si está disponible
        try:
            result = subprocess.run(['black', '--quiet', file_path], capture_output=True)
            if result.returncode == 0:
                print(f"✓ Código Python formateado con Black")
                return
        except FileNotFoundError:
            print("⚠ Black no está instalado, saltando formateo Python")
            return
    elif language == 'javascript':
        # Usar prettier si está disponible
        try:
            result = subprocess.run(['npx', 'prettier', '--write', file_path], capture_output=True)
            if result.returncode == 0:
                print(f"✓ Código JavaScript formateado con Prettier")
                return
        except FileNotFoundError:
            print("⚠ Prettier no está disponible")
            return
    
    if formatted:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(formatted)
        print(f"✓ Archivo formateado: {file_path}")
    else:
        print(f"✗ No se pudo formatear el archivo")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python format_code.py archivo [lenguaje]")
        print("Lenguajes: python, javascript, json, html, css")
        sys.exit(1)
    
    file_to_format = sys.argv[1]
    lang = sys.argv[2].lower() if len(sys.argv) > 2 else None
    
    format_code(file_to_format, lang)
