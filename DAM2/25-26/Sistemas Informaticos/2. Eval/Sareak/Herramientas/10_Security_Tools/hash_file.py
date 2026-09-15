#!/usr/bin/env python3
"""
Herramienta para calcular hash de archivos (MD5, SHA1, SHA256, etc.)
"""
import sys
import hashlib
import os

def calculate_hash(file_path, algorithm='sha256'):
    """Calcula el hash de un archivo"""
    algorithms = {
        'md5': hashlib.md5,
        'sha1': hashlib.sha1,
        'sha256': hashlib.sha256,
        'sha512': hashlib.sha512
    }
    
    if algorithm not in algorithms:
        print(f"Algoritmo no soportado: {algorithm}")
        print(f"Algoritmos disponibles: {', '.join(algorithms.keys())}")
        return None
    
    hash_obj = algorithms[algorithm]()
    
    try:
        with open(file_path, 'rb') as f:
            for chunk in iter(lambda: f.read(4096), b""):
                hash_obj.update(chunk)
        
        return hash_obj.hexdigest()
    
    except Exception as e:
        print(f"Error al calcular hash: {e}")
        return None

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python hash_file.py archivo [algoritmo]")
        print("Algoritmos: md5, sha1, sha256, sha512 (default: sha256)")
        sys.exit(1)
    
    file_path = sys.argv[1]
    algo = sys.argv[2].lower() if len(sys.argv) > 2 else 'sha256'
    
    if not os.path.exists(file_path):
        print(f"Error: El archivo {file_path} no existe")
        sys.exit(1)
    
    print(f"Calculando {algo.upper()} hash de: {file_path}\n")
    
    hash_value = calculate_hash(file_path, algo)
    
    if hash_value:
        print(f"{algo.upper()}: {hash_value}")
        print(f"\nTamaño del archivo: {os.path.getsize(file_path)} bytes")
