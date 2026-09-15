#!/usr/bin/env python3
"""
Herramienta para encontrar y eliminar archivos duplicados
"""
import sys
import os
import hashlib
from collections import defaultdict

def get_file_hash(filepath):
    """Calcula el hash MD5 de un archivo"""
    hash_md5 = hashlib.md5()
    try:
        with open(filepath, 'rb') as f:
            for chunk in iter(lambda: f.read(4096), b""):
                hash_md5.update(chunk)
        return hash_md5.hexdigest()
    except:
        return None

def find_duplicates(directory):
    """Encuentra archivos duplicados en un directorio"""
    hashes = defaultdict(list)
    
    print(f"Escaneando archivos en {directory}...\n")
    
    for root, dirs, files in os.walk(directory):
        for filename in files:
            filepath = os.path.join(root, filename)
            file_hash = get_file_hash(filepath)
            
            if file_hash:
                hashes[file_hash].append(filepath)
    
    # Filtrar solo duplicados
    duplicates = {hash_val: paths for hash_val, paths in hashes.items() if len(paths) > 1}
    
    return duplicates

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python find_duplicates.py directorio [--delete]")
        sys.exit(1)
    
    target_dir = sys.argv[1]
    auto_delete = '--delete' in sys.argv
    
    duplicates = find_duplicates(target_dir)
    
    if duplicates:
        print(f"✓ Encontrados {len(duplicates)} grupos de archivos duplicados:\n")
        
        total_waste = 0
        for hash_val, paths in duplicates.items():
            size = os.path.getsize(paths[0])
            waste = size * (len(paths) - 1)
            total_waste += waste
            
            print(f"Grupo (tamaño: {size} bytes, duplicados: {len(paths)}):")
            for i, path in enumerate(paths, 1):
                marker = "  [ORIGINAL]" if i == 1 else "  [DUPLICADO]"
                print(f"  {i}. {path}{marker}")
            print()
            
            if auto_delete and len(paths) > 1:
                for dup_path in paths[1:]:
                    try:
                        os.remove(dup_path)
                        print(f"  ✓ Eliminado: {dup_path}")
                    except Exception as e:
                        print(f"  ✗ Error al eliminar {dup_path}: {e}")
        
        print(f"\n--- Resumen ---")
        print(f"Espacio desperdiciado: {total_waste / 1024:.2f} KB")
        
        if auto_delete:
            print(f"✓ Duplicados eliminados")
    else:
        print("✓ No se encontraron archivos duplicados")
