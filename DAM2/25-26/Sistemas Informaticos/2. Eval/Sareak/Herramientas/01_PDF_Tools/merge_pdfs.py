#!/usr/bin/env python3
"""
Herramienta para combinar múltiples archivos PDF en uno solo
"""
import sys
from PyPDF2 import PdfMerger
import os

def merge_pdfs(input_files, output_file):
    """Combina múltiples PDFs en uno solo"""
    merger = PdfMerger()
    
    for pdf in input_files:
        if os.path.exists(pdf):
            print(f"Añadiendo: {pdf}")
            merger.append(pdf)
        else:
            print(f"Advertencia: {pdf} no existe, se omite")
    
    merger.write(output_file)
    merger.close()
    print(f"\n✓ PDF combinado guardado en: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python merge_pdfs.py archivo1.pdf archivo2.pdf ... -o salida.pdf")
        sys.exit(1)
    
    if "-o" in sys.argv:
        output_idx = sys.argv.index("-o")
        output_file = sys.argv[output_idx + 1]
        input_files = sys.argv[1:output_idx]
    else:
        input_files = sys.argv[1:]
        output_file = "merged.pdf"
    
    merge_pdfs(input_files, output_file)
