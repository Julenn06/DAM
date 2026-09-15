#!/usr/bin/env python3
"""
Herramienta para convertir imágenes entre formatos (PNG, JPG, WEBP, BMP, etc)
"""
import sys
from PIL import Image
import os

def convert_image(input_file, output_format, output_file=None):
    """Convierte una imagen a otro formato"""
    img = Image.open(input_file)
    
    # Convertir RGBA a RGB si es necesario para JPEG
    if output_format.upper() in ['JPEG', 'JPG'] and img.mode == 'RGBA':
        rgb_img = Image.new('RGB', img.size, (255, 255, 255))
        rgb_img.paste(img, mask=img.split()[3])
        img = rgb_img
    
    if output_file is None:
        name = os.path.splitext(input_file)[0]
        output_file = f"{name}.{output_format.lower()}"
    
    img.save(output_file, output_format.upper())
    print(f"✓ Imagen convertida: {input_file} → {output_file}")
    print(f"  Formato: {output_format.upper()}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python convert_image.py imagen.png jpg [salida.jpg]")
        print("Formatos soportados: PNG, JPEG/JPG, WEBP, BMP, GIF, TIFF")
        sys.exit(1)
    
    input_img = sys.argv[1]
    format_out = sys.argv[2]
    output_img = sys.argv[3] if len(sys.argv) > 3 else None
    
    convert_image(input_img, format_out, output_img)
