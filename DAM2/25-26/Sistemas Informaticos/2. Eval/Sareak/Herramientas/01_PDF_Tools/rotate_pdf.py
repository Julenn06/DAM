#!/usr/bin/env python3
"""
Herramienta para rotar páginas de un PDF
"""
import sys
from PyPDF2 import PdfReader, PdfWriter

def rotate_pdf(input_file, output_file, rotation=90, pages=None):
    """Rota páginas del PDF. rotation: 90, 180, 270 grados"""
    reader = PdfReader(input_file)
    writer = PdfWriter()
    
    total_pages = len(reader.pages)
    
    for i, page in enumerate(reader.pages):
        if pages is None or i in pages:
            page.rotate(rotation)
            print(f"✓ Página {i+1} rotada {rotation}°")
        writer.add_page(page)
    
    with open(output_file, 'wb') as f:
        writer.write(f)
    
    print(f"\n✓ PDF rotado guardado: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python rotate_pdf.py archivo.pdf [salida.pdf] [grados] [paginas]")
        print("Grados: 90, 180, 270")
        print("Páginas: all o números separados por coma (1,3,5)")
        sys.exit(1)
    
    input_pdf = sys.argv[1]
    output_pdf = sys.argv[2] if len(sys.argv) > 2 else "rotated_" + input_pdf
    degrees = int(sys.argv[3]) if len(sys.argv) > 3 else 90
    
    page_list = None
    if len(sys.argv) > 4 and sys.argv[4].lower() != 'all':
        page_list = [int(p)-1 for p in sys.argv[4].split(',')]
    
    rotate_pdf(input_pdf, output_pdf, degrees, page_list)
