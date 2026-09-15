#!/usr/bin/env python3
"""
Herramienta para importar CSV a base de datos SQLite
"""
import sys
import sqlite3
import csv
import os

def csv_to_sqlite(csv_file, db_file, table_name=None):
    """Importa un archivo CSV a una tabla SQLite"""
    if not os.path.exists(csv_file):
        print(f"Error: {csv_file} no existe")
        return
    
    if table_name is None:
        table_name = os.path.splitext(os.path.basename(csv_file))[0]
    
    try:
        # Leer CSV
        with open(csv_file, 'r', encoding='utf-8') as f:
            csv_reader = csv.reader(f)
            headers = next(csv_reader)
            rows = list(csv_reader)
        
        # Conectar a SQLite
        conn = sqlite3.connect(db_file)
        cursor = conn.cursor()
        
        # Crear tabla
        columns = ', '.join([f'"{col}" TEXT' for col in headers])
        cursor.execute(f'CREATE TABLE IF NOT EXISTS "{table_name}" ({columns})')
        
        # Insertar datos
        placeholders = ', '.join(['?' for _ in headers])
        cursor.executemany(f'INSERT INTO "{table_name}" VALUES ({placeholders})', rows)
        
        conn.commit()
        conn.close()
        
        print(f"✓ Datos importados a SQLite")
        print(f"  Base de datos: {db_file}")
        print(f"  Tabla: {table_name}")
        print(f"  Registros insertados: {len(rows)}")
        print(f"  Columnas: {len(headers)}")
    
    except Exception as e:
        print(f"✗ Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python csv_to_sqlite.py archivo.csv database.db [nombre_tabla]")
        sys.exit(1)
    
    csv_path = sys.argv[1]
    db_path = sys.argv[2]
    table = sys.argv[3] if len(sys.argv) > 3 else None
    
    csv_to_sqlite(csv_path, db_path, table)
