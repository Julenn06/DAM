#!/usr/bin/env python3
"""
Herramienta para convertir YAML a JSON
"""
import sys
import json
import yaml

def yaml_to_json(yaml_file, json_file=None):
    """Convierte un archivo YAML a JSON"""
    if json_file is None:
        json_file = yaml_file.replace('.yaml', '.json').replace('.yml', '.json')
    
    with open(yaml_file, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)
    
    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, ensure_ascii=False)
    
    print(f"✓ Convertido: {yaml_file} → {json_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python yaml_to_json.py archivo.yaml [salida.json]")
        sys.exit(1)
    
    yaml_to_json(sys.argv[1], sys.argv[2] if len(sys.argv) > 2 else None)
