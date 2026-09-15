#!/usr/bin/env python3
"""
Herramienta para crear GIF animado desde un video
"""
import sys
import subprocess
import os

def video_to_gif(input_file, output_file=None, start_time='0', duration='5', fps=10, width=480):
    """Convierte un segmento de video a GIF animado"""
    if output_file is None:
        base_name = os.path.splitext(input_file)[0]
        output_file = f"{base_name}.gif"
    
    print(f"Creando GIF desde: {input_file}")
    print(f"Configuración:")
    print(f"  Inicio: {start_time}s")
    print(f"  Duración: {duration}s")
    print(f"  FPS: {fps}")
    print(f"  Ancho: {width}px\n")
    
    try:
        # Generar paleta de colores para mejor calidad
        palette_file = 'palette.png'
        
        cmd_palette = [
            'ffmpeg', '-ss', start_time, '-t', duration, '-i', input_file,
            '-vf', f'fps={fps},scale={width}:-1:flags=lanczos,palettegen',
            palette_file, '-y'
        ]
        
        subprocess.run(cmd_palette, capture_output=True)
        
        # Crear GIF con la paleta
        cmd_gif = [
            'ffmpeg', '-ss', start_time, '-t', duration, '-i', input_file,
            '-i', palette_file,
            '-filter_complex', f'fps={fps},scale={width}:-1:flags=lanczos[x];[x][1:v]paletteuse',
            output_file, '-y'
        ]
        
        result = subprocess.run(cmd_gif, capture_output=True, text=True)
        
        # Limpiar archivo temporal
        if os.path.exists(palette_file):
            os.remove(palette_file)
        
        if result.returncode == 0:
            size = os.path.getsize(output_file)
            print(f"✓ GIF creado: {output_file}")
            print(f"  Tamaño: {size / (1024**2):.2f} MB")
        else:
            print("✗ Error al crear GIF")
    
    except FileNotFoundError:
        print("✗ Error: ffmpeg no está instalado")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python video_to_gif.py video.mp4 [salida.gif] [--start 0] [--duration 5] [--fps 10] [--width 480]")
        sys.exit(1)
    
    input_vid = sys.argv[1]
    
    # Parsear argumentos
    output_gif = None
    start = '0'
    dur = '5'
    fps_val = 10
    width_val = 480
    
    i = 2
    while i < len(sys.argv):
        if sys.argv[i] == '--start' and i + 1 < len(sys.argv):
            start = sys.argv[i + 1]
            i += 2
        elif sys.argv[i] == '--duration' and i + 1 < len(sys.argv):
            dur = sys.argv[i + 1]
            i += 2
        elif sys.argv[i] == '--fps' and i + 1 < len(sys.argv):
            fps_val = int(sys.argv[i + 1])
            i += 2
        elif sys.argv[i] == '--width' and i + 1 < len(sys.argv):
            width_val = int(sys.argv[i + 1])
            i += 2
        elif not output_gif and not sys.argv[i].startswith('--'):
            output_gif = sys.argv[i]
            i += 1
        else:
            i += 1
    
    video_to_gif(input_vid, output_gif, start, dur, fps_val, width_val)
