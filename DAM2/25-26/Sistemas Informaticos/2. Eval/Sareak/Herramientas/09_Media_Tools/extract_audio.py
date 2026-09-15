#!/usr/bin/env python3
"""
Herramienta para extraer audio de archivos de video
"""
import sys
import subprocess
import os

def extract_audio(video_file, output_file=None, format='mp3'):
    """Extrae el audio de un archivo de video usando ffmpeg"""
    if output_file is None:
        base_name = os.path.splitext(video_file)[0]
        output_file = f"{base_name}.{format}"
    
    print(f"Extrayendo audio de: {video_file}")
    print(f"Formato de salida: {format}\n")
    
    try:
        cmd = ['ffmpeg', '-i', video_file, '-vn', '-acodec', 'libmp3lame' if format == 'mp3' else 'copy', output_file, '-y']
        
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode == 0:
            size = os.path.getsize(output_file)
            print(f"\n✓ Audio extraído: {output_file}")
            print(f"  Tamaño: {size / (1024**2):.2f} MB")
        else:
            print(f"\n✗ Error al extraer audio")
            print(result.stderr)
    
    except FileNotFoundError:
        print("✗ Error: ffmpeg no está instalado")
        print("Instala ffmpeg desde: https://ffmpeg.org/download.html")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python extract_audio.py video.mp4 [salida.mp3] [formato]")
        print("Formatos: mp3, wav, aac, ogg")
        sys.exit(1)
    
    video = sys.argv[1]
    output = sys.argv[2] if len(sys.argv) > 2 and not sys.argv[2] in ['mp3', 'wav', 'aac', 'ogg'] else None
    fmt = sys.argv[-1] if sys.argv[-1] in ['mp3', 'wav', 'aac', 'ogg'] else 'mp3'
    
    extract_audio(video, output, fmt)
