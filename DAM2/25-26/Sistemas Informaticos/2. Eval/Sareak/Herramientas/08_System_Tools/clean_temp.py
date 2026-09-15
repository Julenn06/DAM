#!/usr/bin/env python3
"""
Herramienta para limpiar archivos temporales del sistema
"""
import sys
import os
import shutil
import tempfile

def get_folder_size(folder):
    """Calcula el tamaño de una carpeta"""
    total = 0
    try:
        for dirpath, dirnames, filenames in os.walk(folder):
            for filename in filenames:
                filepath = os.path.join(dirpath, filename)
                try:
                    total += os.path.getsize(filepath)
                except:
                    pass
    except:
        pass
    return total

def clean_temp_files(dry_run=False):
    """Limpia archivos temporales"""
    temp_dir = tempfile.gettempdir()
    
    print(f"Directorio temporal: {temp_dir}\n")
    
    # Calcular tamaño antes
    size_before = get_folder_size(temp_dir)
    print(f"Tamaño actual: {size_before / (1024**2):.2f} MB")
    
    if dry_run:
        print("\nModo simulación (--dry-run): No se eliminará nada\n")
    else:
        print("\nEliminando archivos temporales...\n")
    
    deleted_count = 0
    deleted_size = 0
    
    for item in os.listdir(temp_dir):
        item_path = os.path.join(temp_dir, item)
        
        try:
            item_size = get_folder_size(item_path) if os.path.isdir(item_path) else os.path.getsize(item_path)
            
            if not dry_run:
                if os.path.isfile(item_path):
                    os.remove(item_path)
                elif os.path.isdir(item_path):
                    shutil.rmtree(item_path)
            
            print(f"✓ {'[SIMULADO] ' if dry_run else ''}Eliminado: {item} ({item_size / 1024:.2f} KB)")
            deleted_count += 1
            deleted_size += item_size
            
        except Exception as e:
            print(f"✗ No se pudo eliminar {item}: {e}")
    
    print(f"\n--- Resumen ---")
    print(f"Archivos/carpetas {'a eliminar' if dry_run else 'eliminados'}: {deleted_count}")
    print(f"Espacio {'a liberar' if dry_run else 'liberado'}: {deleted_size / (1024**2):.2f} MB")

if __name__ == "__main__":
    is_dry_run = '--dry-run' in sys.argv
    
    print("=== Limpiador de Archivos Temporales ===\n")
    
    if not is_dry_run:
        response = input("¿Deseas continuar? (s/n): ")
        if response.lower() != 's':
            print("Cancelado")
            sys.exit(0)
    
    clean_temp_files(is_dry_run)
