#!/usr/bin/env python3
"""
Herramienta para escanear directorios en busca de archivos sensibles
"""
import sys
import os
import re

# Patrones de archivos sensibles
SENSITIVE_PATTERNS = {
    'Credenciales': [r'.*\.pem$', r'.*\.key$', r'.*\.ppk$', r'.*\.p12$', r'.*\.pfx$'],
    'Configuración': [r'.*\.env$', r'.*\.config$', r'\.aws/credentials$', r'\.ssh/id_rsa$'],
    'Bases de datos': [r'.*\.db$', r'.*\.sqlite$', r'.*\.mdb$'],
    'Backups': [r'.*\.bak$', r'.*\.backup$', r'.*\.old$'],
    'Logs': [r'.*\.log$', r'.*\.logs$'],
}

# Palabras clave en nombres de archivo
KEYWORDS = ['password', 'secret', 'token', 'api', 'key', 'credentials', 'private', 'backup']

def scan_for_sensitive_files(directory, max_depth=5):
    """Escanea en busca de archivos potencialmente sensibles"""
    findings = {category: [] for category in SENSITIVE_PATTERNS.keys()}
    findings['Palabras clave'] = []
    
    for root, dirs, files in os.walk(directory):
        # Limitar profundidad
        depth = root[len(directory):].count(os.sep)
        if depth > max_depth:
            continue
        
        for filename in files:
            filepath = os.path.join(root, filename)
            relative_path = os.path.relpath(filepath, directory)
            
            # Verificar patrones
            for category, patterns in SENSITIVE_PATTERNS.items():
                for pattern in patterns:
                    if re.match(pattern, filename, re.IGNORECASE):
                        findings[category].append(relative_path)
                        break
            
            # Verificar palabras clave
            if any(keyword in filename.lower() for keyword in KEYWORDS):
                if relative_path not in sum(findings.values(), []):
                    findings['Palabras clave'].append(relative_path)
    
    return findings

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python scan_sensitive.py directorio [--depth N]")
        sys.exit(1)
    
    target_dir = sys.argv[1]
    depth = 5
    
    if '--depth' in sys.argv:
        depth = int(sys.argv[sys.argv.index('--depth') + 1])
    
    print(f"Escaneando: {target_dir}")
    print(f"Profundidad máxima: {depth}\n")
    
    results = scan_for_sensitive_files(target_dir, depth)
    
    total_found = sum(len(files) for files in results.values())
    
    if total_found == 0:
        print("✓ No se encontraron archivos sensibles")
    else:
        print(f"⚠ Se encontraron {total_found} archivo(s) potencialmente sensible(s):\n")
        
        for category, files in results.items():
            if files:
                print(f"--- {category} ({len(files)}) ---")
                for file in files[:10]:  # Mostrar primeros 10
                    print(f"  • {file}")
                if len(files) > 10:
                    print(f"  ... y {len(files) - 10} más")
                print()
