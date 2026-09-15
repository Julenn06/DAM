#!/usr/bin/env python3
"""
Herramienta para generar documentación de APIs desde código
"""
import sys
import ast
import os

def extract_python_docstrings(file_path):
    """Extrae docstrings de funciones y clases de un archivo Python"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            tree = ast.parse(f.read())
        
        documentation = []
        
        for node in ast.walk(tree):
            if isinstance(node, ast.FunctionDef):
                docstring = ast.get_docstring(node)
                if docstring:
                    # Extraer parámetros
                    params = [arg.arg for arg in node.args.args]
                    
                    documentation.append({
                        'type': 'function',
                        'name': node.name,
                        'params': params,
                        'docstring': docstring
                    })
            
            elif isinstance(node, ast.ClassDef):
                docstring = ast.get_docstring(node)
                if docstring:
                    documentation.append({
                        'type': 'class',
                        'name': node.name,
                        'docstring': docstring
                    })
        
        return documentation
    
    except Exception as e:
        print(f"Error al analizar {file_path}: {e}")
        return []

def generate_markdown_docs(file_path, output_file=None):
    """Genera documentación en formato Markdown"""
    docs = extract_python_docstrings(file_path)
    
    if not docs:
        print("No se encontró documentación en el archivo")
        return
    
    if output_file is None:
        output_file = os.path.splitext(file_path)[0] + '_docs.md'
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(f"# Documentación: {os.path.basename(file_path)}\n\n")
        
        for item in docs:
            if item['type'] == 'class':
                f.write(f"## Clase: `{item['name']}`\n\n")
                f.write(f"{item['docstring']}\n\n")
            
            elif item['type'] == 'function':
                params_str = ', '.join(item['params'])
                f.write(f"### `{item['name']}({params_str})`\n\n")
                f.write(f"{item['docstring']}\n\n")
    
    print(f"✓ Documentación generada: {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python generate_docs.py archivo.py [salida.md]")
        sys.exit(1)
    
    source_file = sys.argv[1]
    output_md = sys.argv[2] if len(sys.argv) > 2 else None
    
    generate_markdown_docs(source_file, output_md)
