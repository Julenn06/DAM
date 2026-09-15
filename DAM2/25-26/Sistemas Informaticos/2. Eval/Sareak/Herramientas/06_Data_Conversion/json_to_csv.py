#!/usr/bin/env python3
"""
Herramienta para convertir JSON a CSV
"""
import sys
import json
import csv

def json_to_csv(json_file, csv_file=None):
    """Convierte un archivo JSON a CSV"""
    if csv_file is None:
        csv_file = json_file.replace('.json', '.csv')
    
    with open(json_file, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    if not isinstance(data, list):
        data = [data]
    
    if not data:
        print("Error: El archivo JSON está vacío")
        return
    
    # Obtener todas las claves
    fieldnames = set()
    for item in data:
        if isinstance(item, dict):
            fieldnames.update(item.keys())
    
    fieldnames = sorted(fieldnames)
    
    with open(csv_file, 'w', encoding='utf-8', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        
        for item in data:
            if isinstance(item, dict):
                writer.writerow(item)
    
    print(f"✓ Convertido: {json_file} → {csv_file}")
    print(f"  Registros: {len(data)}")
    print(f"  Columnas: {len(fieldnames)}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python json_to_csv.py archivo.json [salida.csv]")
        sys.exit(1)
    
    json_to_csv(sys.argv[1], sys.argv[2] if len(sys.argv) > 2 else None)
