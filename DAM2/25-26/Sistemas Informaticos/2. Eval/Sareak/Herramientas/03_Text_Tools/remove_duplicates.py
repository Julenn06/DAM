#!/usr/bin/env python3
"""
Herramienta para eliminar líneas duplicadas de un archivo
"""
import sys

def remove_duplicates(input_file, output_file=None, keep_order=True):
    """Elimina líneas duplicadas del archivo"""
    with open(input_file, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    original_count = len(lines)
    
    if keep_order:
        seen = set()
        unique_lines = []
        for line in lines:
            if line not in seen:
                seen.add(line)
                unique_lines.append(line)
    else:
        unique_lines = list(set(lines))
    
    if output_file is None:
        output_file = input_file.replace('.txt', '_unique.txt')
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.writelines(unique_lines)
    
    removed = original_count - len(unique_lines)
    print(f"✓ Líneas originales: {original_count}")
    print(f"  Líneas únicas: {len(unique_lines)}")
    print(f"  Duplicados eliminados: {removed}")
    print(f"  Guardado en: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python remove_duplicates.py archivo.txt [salida.txt]")
        sys.exit(1)
    
    input_txt = sys.argv[1]
    output_txt = sys.argv[2] if len(sys.argv) > 2 else None
    
    remove_duplicates(input_txt, output_txt)
