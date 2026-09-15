#!/usr/bin/env python3
"""
Herramienta para rotar y voltear imágenes
"""
import sys
from PIL import Image
import os

def rotate_image(input_file, output_file=None, angle=90, flip=None):
    """Rota o voltea una imagen"""
    img = Image.open(input_file)
    
    if flip == 'horizontal':
        img = img.transpose(Image.Flip.LEFT_RIGHT)
        operation = "Volteada horizontalmente"
    elif flip == 'vertical':
        img = img.transpose(Image.Flip.TOP_BOTTOM)
        operation = "Volteada verticalmente"
    else:
        img = img.rotate(angle, expand=True)
        operation = f"Rotada {angle}°"
    
    if output_file is None:
        name, ext = os.path.splitext(input_file)
        output_file = f"{name}_rotated{ext}"
    
    img.save(output_file)
    print(f"✓ {operation}")
    print(f"  Guardada en: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python rotate_image.py imagen.jpg [angulo] [salida.jpg]")
        print("      python rotate_image.py imagen.jpg --flip horizontal [salida.jpg]")
        sys.exit(1)
    
    input_img = sys.argv[1]
    
    if "--flip" in sys.argv:
        flip_dir = sys.argv[sys.argv.index("--flip") + 1]
        output_img = sys.argv[-1] if not sys.argv[-1] in ['horizontal', 'vertical'] else None
        rotate_image(input_img, output_img, flip=flip_dir)
    else:
        angle_val = int(sys.argv[2]) if len(sys.argv) > 2 and sys.argv[2].lstrip('-').isdigit() else 90
        output_img = sys.argv[3] if len(sys.argv) > 3 else None
        rotate_image(input_img, output_img, angle_val)
