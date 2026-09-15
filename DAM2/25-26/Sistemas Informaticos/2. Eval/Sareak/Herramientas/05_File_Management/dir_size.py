#!/usr/bin/env python3
"""
Herramienta para calcular el tamaño de directorios
"""
import sys
import os

def get_dir_size(directory):
    """Calcula el tamaño total de un directorio"""
    total_size = 0
    file_count = 0
    dir_count = 0
    
    for dirpath, dirnames, filenames in os.walk(directory):
        dir_count += len(dirnames)
        
        for filename in filenames:
            filepath = os.path.join(dirpath, filename)
            try:
                total_size += os.path.getsize(filepath)
                file_count += 1
            except:
                pass
    
    return total_size, file_count, dir_count

def format_size(bytes_size):
    """Formatea el tamaño en bytes a una unidad legible"""
    for unit in ['B', 'KB', 'MB', 'GB', 'TB']:
        if bytes_size < 1024.0:
            return f"{bytes_size:.2f} {unit}"
        bytes_size /= 1024.0

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python dir_size.py directorio [--subdirs]")
        sys.exit(1)
    
    target_dir = sys.argv[1]
    show_subdirs = '--subdirs' in sys.argv
    
    if not os.path.exists(target_dir):
        print(f"Error: {target_dir} no existe")
        sys.exit(1)
    
    if show_subdirs:
        print(f"Tamaños de subdirectorios en {target_dir}:\n")
        
        subdirs = []
        for item in os.listdir(target_dir):
            item_path = os.path.join(target_dir, item)
            if os.path.isdir(item_path):
                size, files, dirs = get_dir_size(item_path)
                subdirs.append((item, size, files, dirs))
        
        # Ordenar por tamaño
        subdirs.sort(key=lambda x: x[1], reverse=True)
        
        for name, size, files, dirs in subdirs:
            print(f"{format_size(size):>12} - {name}/ ({files} archivos, {dirs} directorios)")
        
        total_size = sum(s[1] for s in subdirs)
        print(f"\n{'─' * 50}")
        print(f"{format_size(total_size):>12} - TOTAL")
    else:
        print(f"Calculando tamaño de {target_dir}...\n")
        total_size, file_count, dir_count = get_dir_size(target_dir)
        
        print(f"Tamaño total: {format_size(total_size)}")
        print(f"Archivos: {file_count}")
        print(f"Directorios: {dir_count}")
