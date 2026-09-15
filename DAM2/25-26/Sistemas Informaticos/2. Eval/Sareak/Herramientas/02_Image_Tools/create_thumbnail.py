#!/usr/bin/env python3
"""
Herramienta para crear miniaturas de imágenes
"""
import sys
from PIL import Image
import os

def create_thumbnail(input_file, output_file=None, size=(128, 128)):
    """Crea una miniatura de la imagen"""
    img = Image.open(input_file)
    img.thumbnail(size, Image.Resampling.LANCZOS)
    
    if output_file is None:
        name, ext = os.path.splitext(input_file)
        output_file = f"{name}_thumb{ext}"
    
    img.save(output_file)
    print(f"✓ Miniatura creada: {img.size[0]}x{img.size[1]}")
    print(f"  Guardada en: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python create_thumbnail.py imagen.jpg [salida.jpg] [tamaño]")
        print("Tamaño: width,height (default: 128,128)")
        sys.exit(1)
    
    input_img = sys.argv[1]
    
    if len(sys.argv) > 2 and ',' in sys.argv[-1]:
        w, h = map(int, sys.argv[-1].split(','))
        thumb_size = (w, h)
        output_img = sys.argv[2] if len(sys.argv) > 3 else None
    else:
        thumb_size = (128, 128)
        output_img = sys.argv[2] if len(sys.argv) > 2 else None
    
    create_thumbnail(input_img, output_img, thumb_size)
