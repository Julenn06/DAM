#!/usr/bin/env python3
"""
Herramienta para recortar videos (trim)
"""
import sys
import subprocess
import os

def trim_video(input_file, output_file, start_time, duration=None, end_time=None):
    """Recorta un video desde start_time hasta end_time o por duración"""
    if output_file is None:
        base_name = os.path.splitext(input_file)[0]
        output_file = f"{base_name}_trimmed.mp4"
    
    print(f"Recortando video: {input_file}")
    print(f"Desde: {start_time}")
    
    cmd = ['ffmpeg', '-i', input_file, '-ss', start_time]
    
    if duration:
        cmd.extend(['-t', duration])
        print(f"Duración: {duration}")
    elif end_time:
        cmd.extend(['-to', end_time])
        print(f"Hasta: {end_time}")
    
    cmd.extend(['-c', 'copy', output_file, '-y'])
    
    print(f"\nProcesando...\n")
    
    try:
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode == 0:
            size = os.path.getsize(output_file)
            print(f"✓ Video recortado: {output_file}")
            print(f"  Tamaño: {size / (1024**2):.2f} MB")
        else:
            print("✗ Error al recortar video")
    
    except FileNotFoundError:
        print("✗ Error: ffmpeg no está instalado")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python trim_video.py video.mp4 00:01:30 [salida.mp4] [--duration 00:00:30]")
        print("      python trim_video.py video.mp4 00:01:30 [salida.mp4] [--end 00:02:00]")
        print("Formato de tiempo: HH:MM:SS o MM:SS")
        sys.exit(1)
    
    input_vid = sys.argv[1]
    start = sys.argv[2]
    
    output_vid = None
    dur = None
    end = None
    
    i = 3
    while i < len(sys.argv):
        if sys.argv[i] == '--duration' and i + 1 < len(sys.argv):
            dur = sys.argv[i + 1]
            i += 2
        elif sys.argv[i] == '--end' and i + 1 < len(sys.argv):
            end = sys.argv[i + 1]
            i += 2
        elif not output_vid:
            output_vid = sys.argv[i]
            i += 1
        else:
            i += 1
    
    trim_video(input_vid, output_vid, start, dur, end)
