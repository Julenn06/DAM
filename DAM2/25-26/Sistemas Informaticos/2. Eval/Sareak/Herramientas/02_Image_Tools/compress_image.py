#!/usr/bin/env python3
"""
Herramienta para comprimir imágenes reduciendo calidad
"""
import sys
from PIL import Image
import os

def compress_image(input_file, output_file=None, quality=85):
    """Comprime una imagen reduciendo su calidad"""
    img = Image.open(input_file)
    
    if output_file is None:
        name, ext = os.path.splitext(input_file)
        output_file = f"{name}_compressed{ext}"
    
    # Convertir a RGB si es necesario
    if img.mode == 'RGBA' and output_file.lower().endswith('.jpg'):
        rgb_img = Image.new('RGB', img.size, (255, 255, 255))
        rgb_img.paste(img, mask=img.split()[3])
        img = rgb_img
    
    img.save(output_file, quality=quality, optimize=True)
    
    original_size = os.path.getsize(input_file)
    compressed_size = os.path.getsize(output_file)
    reduction = ((original_size - compressed_size) / original_size) * 100
    
    print(f"✓ Imagen comprimida guardada: {output_file}")
    print(f"  Tamaño original: {original_size / 1024:.2f} KB")
    print(f"  Tamaño comprimido: {compressed_size / 1024:.2f} KB")
    print(f"  Reducción: {reduction:.2f}%")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python compress_image.py imagen.jpg [salida.jpg] [calidad]")
        print("Calidad: 1-100 (default: 85)")
        sys.exit(1)
    
    input_img = sys.argv[1]
    output_img = sys.argv[2] if len(sys.argv) > 2 and not sys.argv[2].isdigit() else None
    quality_val = int(sys.argv[-1]) if sys.argv[-1].isdigit() else 85
    
    compress_image(input_img, output_img, quality_val)
