#!/usr/bin/env python3
"""
Herramienta para convertir CSV a JSON
"""
import sys
import csv
import json

def csv_to_json(csv_file, json_file=None):
    """Convierte un archivo CSV a JSON"""
    if json_file is None:
        json_file = csv_file.replace('.csv', '.json')
    
    data = []
    
    with open(csv_file, 'r', encoding='utf-8') as f:
        csv_reader = csv.DictReader(f)
        for row in csv_reader:
            data.append(row)
    
    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, ensure_ascii=False)
    
    print(f"✓ Convertido: {csv_file} → {json_file}")
    print(f"  Registros: {len(data)}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python csv_to_json.py archivo.csv [salida.json]")
        sys.exit(1)
    
    csv_to_json(sys.argv[1], sys.argv[2] if len(sys.argv) > 2 else None)
