#!/usr/bin/env python3
"""
Herramienta para dividir un archivo de texto en múltiples archivos
"""
import sys
import os

def split_text_file(input_file, lines_per_file=100, output_dir="split_output"):
    """Divide un archivo de texto en archivos más pequeños"""
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    with open(input_file, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    total_lines = len(lines)
    file_count = 0
    
    for i in range(0, total_lines, lines_per_file):
        chunk = lines[i:i + lines_per_file]
        file_count += 1
        
        output_file = os.path.join(output_dir, f"part_{file_count:03d}.txt")
        with open(output_file, 'w', encoding='utf-8') as f:
            f.writelines(chunk)
        
        print(f"✓ Creado {output_file} ({len(chunk)} líneas)")
    
    print(f"\n✓ {total_lines} líneas divididas en {file_count} archivos")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python split_text_file.py archivo.txt [lineas_por_archivo] [carpeta_salida]")
        sys.exit(1)
    
    input_txt = sys.argv[1]
    lines_count = int(sys.argv[2]) if len(sys.argv) > 2 and sys.argv[2].isdigit() else 100
    output_folder = sys.argv[3] if len(sys.argv) > 3 else "split_output"
    
    split_text_file(input_txt, lines_count, output_folder)
