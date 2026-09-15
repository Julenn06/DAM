#!/usr/bin/env python3
"""
Herramienta para generar archivos .gitignore personalizados
"""
import sys
import requests

def generate_gitignore(languages, output_file='.gitignore'):
    """Genera un .gitignore usando la API de gitignore.io"""
    url = f"https://www.toptal.com/developers/gitignore/api/{','.join(languages)}"
    
    try:
        response = requests.get(url, timeout=10)
        
        if response.status_code == 200:
            content = response.text
            
            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(content)
            
            print(f"✓ .gitignore generado: {output_file}")
            print(f"  Lenguajes/Frameworks: {', '.join(languages)}")
            print(f"  Líneas: {len(content.splitlines())}")
        else:
            print(f"✗ Error al generar .gitignore: {response.status_code}")
    
    except Exception as e:
        print(f"✗ Error: {e}")

def list_available_templates():
    """Lista algunos templates comunes disponibles"""
    common_templates = [
        'Python', 'Node', 'Java', 'C++', 'C#', 'Go', 'Rust',
        'VisualStudioCode', 'IntelliJ', 'Eclipse',
        'Windows', 'Linux', 'macOS',
        'Django', 'Flask', 'React', 'Vue', 'Angular'
    ]
    
    print("Templates comunes disponibles:")
    for i, template in enumerate(common_templates, 1):
        print(f"  {i}. {template}")
    
    print("\nPara ver todos los templates: https://www.toptal.com/developers/gitignore")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python generate_gitignore.py lenguaje1 lenguaje2 ... [--output archivo]")
        print("Ejemplo: python generate_gitignore.py python node visualstudiocode")
        print("")
        list_available_templates()
        sys.exit(1)
    
    if '--list' in sys.argv:
        list_available_templates()
        sys.exit(0)
    
    # Parsear argumentos
    output_path = '.gitignore'
    langs = []
    
    i = 1
    while i < len(sys.argv):
        if sys.argv[i] == '--output' and i + 1 < len(sys.argv):
            output_path = sys.argv[i + 1]
            i += 2
        else:
            langs.append(sys.argv[i])
            i += 1
    
    if langs:
        generate_gitignore(langs, output_path)
    else:
        print("Error: Especifica al menos un lenguaje o framework")
