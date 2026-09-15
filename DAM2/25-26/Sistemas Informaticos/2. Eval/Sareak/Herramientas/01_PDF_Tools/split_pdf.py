#!/usr/bin/env python3
"""
Herramienta para dividir un PDF en páginas individuales
"""
import sys
from PyPDF2 import PdfReader, PdfWriter
import os

def split_pdf(input_file, output_folder="split_output"):
    """Divide un PDF en páginas individuales"""
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    
    reader = PdfReader(input_file)
    total_pages = len(reader.pages)
    
    print(f"Dividiendo {input_file} ({total_pages} páginas)...")
    
    for i, page in enumerate(reader.pages, 1):
        writer = PdfWriter()
        writer.add_page(page)
        
        output_filename = os.path.join(output_folder, f"page_{i:03d}.pdf")
        with open(output_filename, 'wb') as output_file:
            writer.write(output_file)
        
        print(f"✓ Página {i}/{total_pages} guardada")
    
    print(f"\n✓ Todas las páginas guardadas en: {output_folder}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python split_pdf.py archivo.pdf [carpeta_salida]")
        sys.exit(1)
    
    input_pdf = sys.argv[1]
    output_dir = sys.argv[2] if len(sys.argv) > 2 else "split_output"
    
    split_pdf(input_pdf, output_dir)
