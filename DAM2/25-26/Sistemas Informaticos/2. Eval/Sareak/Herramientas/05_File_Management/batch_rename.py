#!/usr/bin/env python3
"""
Herramienta para renombrar archivos en lote
"""
import sys
import os
import re

def batch_rename(directory, pattern, replacement):
    """Renombra archivos usando expresiones regulares"""
    renamed_count = 0
    
    for filename in os.listdir(directory):
        filepath = os.path.join(directory, filename)
        
        if os.path.isfile(filepath):
            new_name = re.sub(pattern, replacement, filename)
            
            if new_name != filename:
                new_path = os.path.join(directory, new_name)
                
                try:
                    os.rename(filepath, new_path)
                    print(f"✓ {filename} → {new_name}")
                    renamed_count += 1
                except Exception as e:
                    print(f"✗ Error renombrando {filename}: {e}")
    
    print(f"\n✓ {renamed_count} archivos renombrados")

if __name__ == "__main__":
    if len(sys.argv) < 4:
        print("Uso: python batch_rename.py directorio patron reemplazo")
        print("Ejemplo: python batch_rename.py ./fotos 'IMG_' 'Foto_'")
        sys.exit(1)
    
    target_dir = sys.argv[1]
    search_pattern = sys.argv[2]
    replace_with = sys.argv[3]
    
    batch_rename(target_dir, search_pattern, replace_with)
