#!/usr/bin/env python3
"""
Herramienta para normalizar el volumen de archivos de audio
"""
import sys
import subprocess
import os

def normalize_audio(input_file, output_file=None, target_level=-20):
    """Normaliza el volumen de un archivo de audio"""
    if output_file is None:
        base_name = os.path.splitext(input_file)[0]
        ext = os.path.splitext(input_file)[1]
        output_file = f"{base_name}_normalized{ext}"
    
    print(f"Normalizando audio: {input_file}")
    print(f"Nivel objetivo: {target_level} dB\n")
    
    try:
        cmd = [
            'ffmpeg', '-i', input_file,
            '-af', f'loudnorm=I={target_level}:TP=-1.5:LRA=11',
            output_file, '-y'
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode == 0:
            size = os.path.getsize(output_file)
            print(f"✓ Audio normalizado: {output_file}")
            print(f"  Tamaño: {size / (1024**2):.2f} MB")
        else:
            print("✗ Error al normalizar audio")
    
    except FileNotFoundError:
        print("✗ Error: ffmpeg no está instalado")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python normalize_audio.py audio.mp3 [salida.mp3] [--level -20]")
        sys.exit(1)
    
    input_audio = sys.argv[1]
    output_audio = None
    level = -20
    
    i = 2
    while i < len(sys.argv):
        if sys.argv[i] == '--level' and i + 1 < len(sys.argv):
            level = int(sys.argv[i + 1])
            i += 2
        elif not output_audio:
            output_audio = sys.argv[i]
            i += 1
        else:
            i += 1
    
    normalize_audio(input_audio, output_audio, level)
