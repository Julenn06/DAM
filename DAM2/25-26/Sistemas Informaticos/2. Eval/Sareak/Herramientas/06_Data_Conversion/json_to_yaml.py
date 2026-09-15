#!/usr/bin/env python3
"""
Herramienta para convertir JSON a YAML
"""
import sys
import json
import yaml

def json_to_yaml(json_file, yaml_file=None):
    """Convierte un archivo JSON a YAML"""
    if yaml_file is None:
        yaml_file = json_file.replace('.json', '.yaml')
    
    with open(json_file, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    with open(yaml_file, 'w', encoding='utf-8') as f:
        yaml.dump(data, f, default_flow_style=False, allow_unicode=True)
    
    print(f"✓ Convertido: {json_file} → {yaml_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python json_to_yaml.py archivo.json [salida.yaml]")
        sys.exit(1)
    
    json_to_yaml(sys.argv[1], sys.argv[2] if len(sys.argv) > 2 else None)
