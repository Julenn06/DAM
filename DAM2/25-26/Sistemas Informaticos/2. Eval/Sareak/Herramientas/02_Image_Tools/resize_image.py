#!/usr/bin/env python3
"""
Herramienta para redimensionar imágenes manteniendo proporción
"""
import sys
from PIL import Image
import os

def resize_image(input_file, output_file=None, width=None, height=None, scale=None):
    """Redimensiona una imagen"""
    img = Image.open(input_file)
    original_width, original_height = img.size
    
    if scale:
        new_width = int(original_width * scale)
        new_height = int(original_height * scale)
    elif width and height:
        new_width, new_height = width, height
    elif width:
        new_width = width
        new_height = int(original_height * (width / original_width))
    elif height:
        new_height = height
        new_width = int(original_width * (height / original_height))
    else:
        print("Error: Especifica width, height o scale")
        sys.exit(1)
    
    resized_img = img.resize((new_width, new_height), Image.Resampling.LANCZOS)
    
    if output_file is None:
        name, ext = os.path.splitext(input_file)
        output_file = f"{name}_resized{ext}"
    
    resized_img.save(output_file)
    print(f"✓ Imagen redimensionada: {original_width}x{original_height} → {new_width}x{new_height}")
    print(f"  Guardada en: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python resize_image.py imagen.jpg --width 800")
        print("      python resize_image.py imagen.jpg --height 600")
        print("      python resize_image.py imagen.jpg --scale 0.5")
        sys.exit(1)
    
    input_img = sys.argv[1]
    w, h, s = None, None, None
    
    if "--width" in sys.argv:
        w = int(sys.argv[sys.argv.index("--width") + 1])
    if "--height" in sys.argv:
        h = int(sys.argv[sys.argv.index("--height") + 1])
    if "--scale" in sys.argv:
        s = float(sys.argv[sys.argv.index("--scale") + 1])
    
    output = sys.argv[-1] if not sys.argv[-1].startswith("--") and len(sys.argv) > 3 else None
    
    resize_image(input_img, output, w, h, s)
