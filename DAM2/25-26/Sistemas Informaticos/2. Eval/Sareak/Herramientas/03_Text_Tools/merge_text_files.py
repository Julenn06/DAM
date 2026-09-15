#!/usr/bin/env python3
"""
Herramienta para combinar múltiples archivos de texto en uno solo
"""
import sys
import os

def merge_text_files(input_files, output_file, separator="\n"):
    """Combina múltiples archivos de texto"""
    combined_content = []
    
    for file_path in input_files:
        if os.path.exists(file_path):
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
                combined_content.append(content)
                print(f"✓ Añadido: {file_path}")
        else:
            print(f"⚠ No encontrado: {file_path}")
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(separator.join(combined_content))
    
    print(f"\n✓ {len(combined_content)} archivos combinados en: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python merge_text_files.py archivo1.txt archivo2.txt ... -o salida.txt")
        sys.exit(1)
    
    if "-o" in sys.argv:
        output_idx = sys.argv.index("-o")
        output_txt = sys.argv[output_idx + 1]
        input_files = sys.argv[1:output_idx]
    else:
        input_files = sys.argv[1:-1]
        output_txt = sys.argv[-1]
    
    merge_text_files(input_files, output_txt)
