#!/usr/bin/env python3
"""
Herramienta para detectar TODOs y FIXMEs en el código
"""
import sys
import os
import re

def find_todos(directory, extensions=None):
    """Encuentra comentarios TODO, FIXME, HACK, etc. en el código"""
    if extensions is None:
        extensions = ['.py', '.js', '.java', '.c', '.cpp', '.h', '.cs', '.rb', '.go', '.php']
    
    patterns = {
        'TODO': re.compile(r'#.*TODO:?\s*(.+)', re.IGNORECASE),
        'FIXME': re.compile(r'#.*FIXME:?\s*(.+)', re.IGNORECASE),
        'HACK': re.compile(r'#.*HACK:?\s*(.+)', re.IGNORECASE),
        'XXX': re.compile(r'#.*XXX:?\s*(.+)', re.IGNORECASE),
        'NOTE': re.compile(r'#.*NOTE:?\s*(.+)', re.IGNORECASE),
    }
    
    # Patrones para comentarios de bloque
    block_patterns = {
        'TODO': re.compile(r'//.*TODO:?\s*(.+)|/\*.*TODO:?\s*(.+)\*/', re.IGNORECASE),
        'FIXME': re.compile(r'//.*FIXME:?\s*(.+)|/\*.*FIXME:?\s*(.+)\*/', re.IGNORECASE),
    }
    
    findings = []
    
    for root, dirs, files in os.walk(directory):
        # Excluir directorios comunes
        dirs[:] = [d for d in dirs if d not in ['node_modules', '.git', 'venv', '__pycache__']]
        
        for filename in files:
            ext = os.path.splitext(filename)[1].lower()
            
            if ext in extensions:
                filepath = os.path.join(root, filename)
                
                try:
                    with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
                        for line_num, line in enumerate(f, 1):
                            for tag, pattern in patterns.items():
                                match = pattern.search(line)
                                if match:
                                    findings.append({
                                        'file': os.path.relpath(filepath, directory),
                                        'line': line_num,
                                        'tag': tag,
                                        'message': match.group(1).strip()
                                    })
                            
                            # También buscar patrones de bloque para JS/C
                            if ext in ['.js', '.c', '.cpp', '.java', '.cs']:
                                for tag, pattern in block_patterns.items():
                                    match = pattern.search(line)
                                    if match:
                                        msg = match.group(1) or match.group(2)
                                        if msg:
                                            findings.append({
                                                'file': os.path.relpath(filepath, directory),
                                                'line': line_num,
                                                'tag': tag,
                                                'message': msg.strip()
                                            })
                except:
                    pass
    
    return findings

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python find_todos.py directorio_proyecto")
        sys.exit(1)
    
    project_dir = sys.argv[1]
    
    print(f"Buscando TODOs en: {project_dir}\n")
    
    results = find_todos(project_dir)
    
    if not results:
        print("✓ No se encontraron TODOs, FIXMEs o similares")
    else:
        # Agrupar por tipo
        by_tag = {}
        for finding in results:
            tag = finding['tag']
            if tag not in by_tag:
                by_tag[tag] = []
            by_tag[tag].append(finding)
        
        print(f"Se encontraron {len(results)} item(s):\n")
        
        for tag in sorted(by_tag.keys()):
            items = by_tag[tag]
            print(f"=== {tag} ({len(items)}) ===")
            
            for item in items:
                print(f"  {item['file']}:{item['line']}")
                print(f"    {item['message']}")
                print()
