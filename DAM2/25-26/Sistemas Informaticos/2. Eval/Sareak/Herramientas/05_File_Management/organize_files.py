#!/usr/bin/env python3
"""
Herramienta para organizar archivos por extensión en carpetas
"""
import sys
import os
import shutil

def organize_by_extension(directory, create_others=True):
    """Organiza archivos en carpetas según su extensión"""
    organized_count = 0
    
    for filename in os.listdir(directory):
        filepath = os.path.join(directory, filename)
        
        if os.path.isfile(filepath):
            _, extension = os.path.splitext(filename)
            
            if extension:
                folder_name = extension[1:].upper() + "_Files"
            elif create_others:
                folder_name = "No_Extension"
            else:
                continue
            
            target_folder = os.path.join(directory, folder_name)
            
            if not os.path.exists(target_folder):
                os.makedirs(target_folder)
                print(f"✓ Carpeta creada: {folder_name}")
            
            target_path = os.path.join(target_folder, filename)
            
            try:
                shutil.move(filepath, target_path)
                print(f"  {filename} → {folder_name}/")
                organized_count += 1
            except Exception as e:
                print(f"✗ Error moviendo {filename}: {e}")
    
    print(f"\n✓ {organized_count} archivos organizados")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python organize_files.py directorio")
        sys.exit(1)
    
    target_dir = sys.argv[1]
    organize_by_extension(target_dir)
