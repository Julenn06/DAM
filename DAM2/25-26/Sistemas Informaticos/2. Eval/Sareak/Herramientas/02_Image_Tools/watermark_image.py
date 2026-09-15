#!/usr/bin/env python3
"""
Herramienta para aplicar marca de agua a imágenes
"""
import sys
from PIL import Image, ImageDraw, ImageFont
import os

def add_watermark(input_file, watermark_text, output_file=None, position="bottom-right"):
    """Añade una marca de agua de texto a la imagen"""
    img = Image.open(input_file)
    draw = ImageDraw.Draw(img)
    
    # Intentar cargar una fuente, o usar la predeterminada
    try:
        font = ImageFont.truetype("arial.ttf", 36)
    except:
        font = ImageFont.load_default()
    
    # Calcular posición
    bbox = draw.textbbox((0, 0), watermark_text, font=font)
    text_width = bbox[2] - bbox[0]
    text_height = bbox[3] - bbox[1]
    
    margin = 10
    if position == "bottom-right":
        x = img.width - text_width - margin
        y = img.height - text_height - margin
    elif position == "bottom-left":
        x = margin
        y = img.height - text_height - margin
    elif position == "top-right":
        x = img.width - text_width - margin
        y = margin
    else:  # top-left
        x = margin
        y = margin
    
    # Dibujar marca de agua con sombra
    draw.text((x+2, y+2), watermark_text, font=font, fill=(0, 0, 0, 128))
    draw.text((x, y), watermark_text, font=font, fill=(255, 255, 255, 200))
    
    if output_file is None:
        name, ext = os.path.splitext(input_file)
        output_file = f"{name}_watermarked{ext}"
    
    img.save(output_file)
    print(f"✓ Marca de agua añadida: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python watermark_image.py imagen.jpg 'Texto' [salida.jpg] [posicion]")
        print("Posiciones: top-left, top-right, bottom-left, bottom-right")
        sys.exit(1)
    
    input_img = sys.argv[1]
    watermark = sys.argv[2]
    output_img = sys.argv[3] if len(sys.argv) > 3 and not sys.argv[3] in ['top-left', 'top-right', 'bottom-left', 'bottom-right'] else None
    pos = sys.argv[-1] if sys.argv[-1] in ['top-left', 'top-right', 'bottom-left', 'bottom-right'] else "bottom-right"
    
    add_watermark(input_img, watermark, output_img, pos)
