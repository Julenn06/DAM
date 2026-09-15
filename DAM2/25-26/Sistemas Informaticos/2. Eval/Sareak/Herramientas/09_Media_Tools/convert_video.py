#!/usr/bin/env python3
"""
Herramienta para convertir videos entre formatos
"""
import sys
import subprocess
import os

def convert_video(input_file, output_file=None, format='mp4', quality='medium'):
    """Convierte un video a otro formato usando ffmpeg"""
    if output_file is None:
        base_name = os.path.splitext(input_file)[0]
        output_file = f"{base_name}_converted.{format}"
    
    # Configuración de calidad
    crf_values = {
        'high': '18',
        'medium': '23',
        'low': '28'
    }
    crf = crf_values.get(quality, '23')
    
    print(f"Convirtiendo: {input_file} → {output_file}")
    print(f"Formato: {format} | Calidad: {quality}\n")
    
    try:
        cmd = ['ffmpeg', '-i', input_file, '-crf', crf, '-preset', 'medium', output_file, '-y']
        
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode == 0:
            size = os.path.getsize(output_file)
            print(f"\n✓ Video convertido: {output_file}")
            print(f"  Tamaño: {size / (1024**2):.2f} MB")
        else:
            print(f"\n✗ Error en la conversión")
    
    except FileNotFoundError:
        print("✗ Error: ffmpeg no está instalado")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python convert_video.py video.avi [salida.mp4] [formato] [calidad]")
        print("Formatos: mp4, avi, mkv, webm, mov")
        print("Calidad: high, medium, low")
        sys.exit(1)
    
    input_vid = sys.argv[1]
    
    # Parsear argumentos
    output_vid = None
    vid_format = 'mp4'
    vid_quality = 'medium'
    
    for arg in sys.argv[2:]:
        if arg in ['mp4', 'avi', 'mkv', 'webm', 'mov']:
            vid_format = arg
        elif arg in ['high', 'medium', 'low']:
            vid_quality = arg
        elif not output_vid:
            output_vid = arg
    
    convert_video(input_vid, output_vid, vid_format, vid_quality)
