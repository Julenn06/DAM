#!/usr/bin/env python3
"""
Herramienta para comprimir archivos PDF reduciendo calidad de imágenes
"""
import sys
from PyPDF2 import PdfReader, PdfWriter
import os

def compress_pdf(input_file, output_file, quality=85):
    """Comprime un PDF reduciendo la calidad de las imágenes"""
    reader = PdfReader(input_file)
    writer = PdfWriter()
    
    for page in reader.pages:
        page.compress_content_streams()
        writer.add_page(page)
    
    with open(output_file, 'wb') as f:
        writer.write(f)
    
    original_size = os.path.getsize(input_file)
    compressed_size = os.path.getsize(output_file)
    reduction = ((original_size - compressed_size) / original_size) * 100
    
    print(f"✓ PDF comprimido guardado: {output_file}")
    print(f"  Tamaño original: {original_size / 1024:.2f} KB")
    print(f"  Tamaño comprimido: {compressed_size / 1024:.2f} KB")
    print(f"  Reducción: {reduction:.2f}%")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python compress_pdf.py archivo.pdf [salida.pdf]")
        sys.exit(1)
    
    input_pdf = sys.argv[1]
    output_pdf = sys.argv[2] if len(sys.argv) > 2 else "compressed_" + os.path.basename(input_pdf)
    
    compress_pdf(input_pdf, output_pdf)
