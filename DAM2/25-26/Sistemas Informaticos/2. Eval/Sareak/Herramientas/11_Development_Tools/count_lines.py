#!/usr/bin/env python3
"""
Herramienta para contar líneas de código en un proyecto
"""
import sys
import os
from collections import defaultdict

# Extensiones a contar
CODE_EXTENSIONS = {
    '.py': 'Python',
    '.js': 'JavaScript',
    '.jsx': 'React',
    '.ts': 'TypeScript',
    '.tsx': 'TypeScript React',
    '.java': 'Java',
    '.c': 'C',
    '.cpp': 'C++',
    '.h': 'Header',
    '.cs': 'C#',
    '.php': 'PHP',
    '.rb': 'Ruby',
    '.go': 'Go',
    '.rs': 'Rust',
    '.html': 'HTML',
    '.css': 'CSS',
    '.scss': 'SCSS',
    '.sql': 'SQL',
    '.sh': 'Shell',
    '.ps1': 'PowerShell',
}

def count_lines_in_file(file_path):
    """Cuenta líneas de código, comentarios y vacías en un archivo"""
    try:
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
            lines = f.readlines()
        
        total = len(lines)
        blank = sum(1 for line in lines if not line.strip())
        code = total - blank
        
        return {'total': total, 'code': code, 'blank': blank}
    except:
        return {'total': 0, 'code': 0, 'blank': 0}

def count_lines_in_project(directory, exclude_dirs=None):
    """Cuenta líneas en todo un proyecto"""
    if exclude_dirs is None:
        exclude_dirs = ['node_modules', '.git', 'venv', '__pycache__', 'dist', 'build']
    
    stats_by_language = defaultdict(lambda: {'files': 0, 'total': 0, 'code': 0, 'blank': 0})
    
    for root, dirs, files in os.walk(directory):
        # Excluir directorios
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        
        for filename in files:
            ext = os.path.splitext(filename)[1].lower()
            
            if ext in CODE_EXTENSIONS:
                filepath = os.path.join(root, filename)
                counts = count_lines_in_file(filepath)
                
                language = CODE_EXTENSIONS[ext]
                stats_by_language[language]['files'] += 1
                stats_by_language[language]['total'] += counts['total']
                stats_by_language[language]['code'] += counts['code']
                stats_by_language[language]['blank'] += counts['blank']
    
    return dict(stats_by_language)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python count_lines.py directorio_proyecto")
        sys.exit(1)
    
    project_dir = sys.argv[1]
    
    print(f"Contando líneas de código en: {project_dir}\n")
    
    stats = count_lines_in_project(project_dir)
    
    if not stats:
        print("No se encontraron archivos de código")
    else:
        print(f"{'Lenguaje':<20} {'Archivos':<10} {'Líneas':<12} {'Código':<12} {'Vacías':<10}")
        print("=" * 70)
        
        total_files = 0
        total_lines = 0
        total_code = 0
        total_blank = 0
        
        for language in sorted(stats.keys()):
            s = stats[language]
            print(f"{language:<20} {s['files']:<10} {s['total']:<12} {s['code']:<12} {s['blank']:<10}")
            
            total_files += s['files']
            total_lines += s['total']
            total_code += s['code']
            total_blank += s['blank']
        
        print("=" * 70)
        print(f"{'TOTAL':<20} {total_files:<10} {total_lines:<12} {total_code:<12} {total_blank:<10}")
