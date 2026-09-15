#!/usr/bin/env python3
"""
Herramienta para borrado seguro de archivos (sobrescritura)
"""
import sys
import os
import random

def secure_delete(file_path, passes=3):
    """Borra un archivo de forma segura sobrescribiéndolo"""
    if not os.path.exists(file_path):
        print(f"Error: {file_path} no existe")
        return False
    
    try:
        file_size = os.path.getsize(file_path)
        
        print(f"Borrando de forma segura: {file_path}")
        print(f"Tamaño: {file_size} bytes")
        print(f"Pasadas: {passes}\n")
        
        with open(file_path, 'r+b') as f:
            for pass_num in range(passes):
                f.seek(0)
                
                # Sobrescribir con datos aleatorios
                chunk_size = 4096
                remaining = file_size
                
                while remaining > 0:
                    chunk = min(chunk_size, remaining)
                    f.write(os.urandom(chunk))
                    remaining -= chunk
                
                f.flush()
                os.fsync(f.fileno())
                
                print(f"✓ Pasada {pass_num + 1}/{passes} completada")
        
        # Eliminar el archivo
        os.remove(file_path)
        print(f"\n✓ Archivo eliminado de forma segura")
        return True
    
    except Exception as e:
        print(f"✗ Error: {e}")
        return False

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python secure_delete.py archivo [pasadas]")
        print("Pasadas: número de veces que se sobrescribe (default: 3)")
        sys.exit(1)
    
    file_to_delete = sys.argv[1]
    num_passes = int(sys.argv[2]) if len(sys.argv) > 2 else 3
    
    # Confirmación
    response = input(f"⚠ ¿Estás seguro de borrar '{file_to_delete}'? (s/n): ")
    
    if response.lower() == 's':
        secure_delete(file_to_delete, num_passes)
    else:
        print("Operación cancelada")
