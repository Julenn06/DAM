#!/usr/bin/env python3
"""
Herramienta para obtener información detallada de archivos multimedia
"""
import sys
import subprocess
import json

def get_media_info(file_path):
    """Obtiene información de un archivo multimedia usando ffprobe"""
    try:
        cmd = [
            'ffprobe',
            '-v', 'quiet',
            '-print_format', 'json',
            '-show_format',
            '-show_streams',
            file_path
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode == 0:
            data = json.loads(result.stdout)
            
            print(f"=== Información de: {file_path} ===\n")
            
            # Información general
            if 'format' in data:
                fmt = data['format']
                print("--- Información General ---")
                print(f"Formato: {fmt.get('format_long_name', 'N/A')}")
                print(f"Duración: {float(fmt.get('duration', 0)):.2f} segundos")
                print(f"Tamaño: {int(fmt.get('size', 0)) / (1024**2):.2f} MB")
                print(f"Bitrate: {int(fmt.get('bit_rate', 0)) / 1000:.0f} kbps")
                print()
            
            # Streams
            if 'streams' in data:
                for i, stream in enumerate(data['streams'], 1):
                    codec_type = stream.get('codec_type', 'unknown')
                    
                    print(f"--- Stream {i}: {codec_type.upper()} ---")
                    print(f"Codec: {stream.get('codec_long_name', 'N/A')}")
                    
                    if codec_type == 'video':
                        print(f"Resolución: {stream.get('width', '?')}x{stream.get('height', '?')}")
                        print(f"FPS: {eval(stream.get('r_frame_rate', '0/1')):.2f}")
                        print(f"Bitrate: {int(stream.get('bit_rate', 0)) / 1000:.0f} kbps")
                    
                    elif codec_type == 'audio':
                        print(f"Sample Rate: {stream.get('sample_rate', 'N/A')} Hz")
                        print(f"Channels: {stream.get('channels', 'N/A')}")
                        print(f"Bitrate: {int(stream.get('bit_rate', 0)) / 1000:.0f} kbps")
                    
                    print()
        else:
            print("✗ Error al obtener información")
    
    except FileNotFoundError:
        print("✗ Error: ffprobe no está instalado")
    except Exception as e:
        print(f"✗ Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python media_info.py archivo_multimedia")
        sys.exit(1)
    
    get_media_info(sys.argv[1])
