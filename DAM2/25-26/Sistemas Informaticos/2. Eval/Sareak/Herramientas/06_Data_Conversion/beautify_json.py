#!/usr/bin/env python3
"""
Herramienta para formatear y embellecer JSON
"""
import sys
import json

def beautify_json(input_file, output_file=None, indent=2):
    """Formatea un archivo JSON para que sea más legible"""
    with open(input_file, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    if output_file is None:
        output_file = input_file.replace('.json', '_beautified.json')
    
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=indent, ensure_ascii=False, sort_keys=True)
    
    print(f"✓ JSON formateado: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python beautify_json.py archivo.json [salida.json] [indent]")
        sys.exit(1)
    
    input_json = sys.argv[1]
    output_json = sys.argv[2] if len(sys.argv) > 2 and not sys.argv[2].isdigit() else None
    indent_val = int(sys.argv[-1]) if sys.argv[-1].isdigit() else 2
    
    beautify_json(input_json, output_json, indent_val)
