#!/usr/bin/env python3
"""
Herramienta para ordenar líneas de un archivo alfabéticamente
"""
import sys

def sort_lines(input_file, output_file=None, reverse=False):
    """Ordena las líneas del archivo alfabéticamente"""
    with open(input_file, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    sorted_lines = sorted(lines, reverse=reverse)
    
    if output_file is None:
        output_file = input_file.replace('.txt', '_sorted.txt')
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.writelines(sorted_lines)
    
    order = "descendente" if reverse else "ascendente"
    print(f"✓ {len(lines)} líneas ordenadas en orden {order}")
    print(f"  Guardado en: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python sort_lines.py archivo.txt [salida.txt] [--reverse]")
        sys.exit(1)
    
    input_txt = sys.argv[1]
    reverse_order = "--reverse" in sys.argv
    output_txt = sys.argv[2] if len(sys.argv) > 2 and sys.argv[2] != "--reverse" else None
    
    sort_lines(input_txt, output_txt, reverse_order)
