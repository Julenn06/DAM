#!/usr/bin/env python3
"""
Herramienta para hacer backup de bases de datos MySQL/MariaDB
"""
import sys
import subprocess
import os
from datetime import datetime

def backup_mysql(host, user, password, database, output_dir='.'):
    """Crea un backup de una base de datos MySQL"""
    timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
    output_file = os.path.join(output_dir, f"{database}_backup_{timestamp}.sql")
    
    print(f"Haciendo backup de {database}...")
    print(f"Host: {host}")
    print(f"Usuario: {user}\n")
    
    try:
        cmd = [
            'mysqldump',
            f'-h{host}',
            f'-u{user}',
            f'-p{password}',
            database
        ]
        
        with open(output_file, 'w') as f:
            result = subprocess.run(cmd, stdout=f, stderr=subprocess.PIPE, text=True)
        
        if result.returncode == 0:
            size = os.path.getsize(output_file)
            print(f"✓ Backup completado: {output_file}")
            print(f"  Tamaño: {size / (1024**2):.2f} MB")
            
            # Comprimir
            print("\nComprimiendo...")
            subprocess.run(['gzip', output_file])
            compressed_file = output_file + '.gz'
            
            if os.path.exists(compressed_file):
                compressed_size = os.path.getsize(compressed_file)
                print(f"✓ Backup comprimido: {compressed_file}")
                print(f"  Tamaño: {compressed_size / (1024**2):.2f} MB")
        else:
            print(f"✗ Error en el backup: {result.stderr}")
            if os.path.exists(output_file):
                os.remove(output_file)
    
    except FileNotFoundError:
        print("✗ Error: mysqldump no está instalado")
    except Exception as e:
        print(f"✗ Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 5:
        print("Uso: python backup_mysql.py host usuario contraseña base_datos [directorio_salida]")
        print("Ejemplo: python backup_mysql.py localhost root mypass mydb ./backups")
        sys.exit(1)
    
    db_host = sys.argv[1]
    db_user = sys.argv[2]
    db_pass = sys.argv[3]
    db_name = sys.argv[4]
    out_dir = sys.argv[5] if len(sys.argv) > 5 else '.'
    
    if not os.path.exists(out_dir):
        os.makedirs(out_dir)
    
    backup_mysql(db_host, db_user, db_pass, db_name, out_dir)
