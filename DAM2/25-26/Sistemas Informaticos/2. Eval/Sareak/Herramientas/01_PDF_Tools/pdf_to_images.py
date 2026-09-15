#!/usr/bin/env python3
"""
Herramienta para convertir cada página de un PDF a imagen (PNG/JPG)
"""
import sys
from pdf2image import convert_from_path
import os

def pdf_to_images(input_file, output_folder="pdf_images", format="PNG", dpi=200):
    """Convierte cada página del PDF a una imagen"""
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    
    print(f"Convirtiendo {input_file} a imágenes...")
    images = convert_from_path(input_file, dpi=dpi)
    
    for i, image in enumerate(images, 1):
        output_file = os.path.join(output_folder, f"page_{i:03d}.{format.lower()}")
        image.save(output_file, format)
        print(f"✓ Página {i}/{len(images)} guardada")
    
    print(f"\n✓ Todas las imágenes guardadas en: {output_folder}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python pdf_to_images.py archivo.pdf [carpeta] [formato] [dpi]")
        print("Formatos: PNG, JPEG")
        sys.exit(1)
    
    input_pdf = sys.argv[1]
    output_dir = sys.argv[2] if len(sys.argv) > 2 else "pdf_images"
    img_format = sys.argv[3].upper() if len(sys.argv) > 3 else "PNG"
    dpi_value = int(sys.argv[4]) if len(sys.argv) > 4 else 200
    
    pdf_to_images(input_pdf, output_dir, img_format, dpi_value)
