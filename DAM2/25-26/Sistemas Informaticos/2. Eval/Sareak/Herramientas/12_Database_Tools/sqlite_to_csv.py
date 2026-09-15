#!/usr/bin/env python3
"""
Herramienta para exportar tablas de SQLite a CSV
"""
import sys
import sqlite3
import csv
import os

def sqlite_to_csv(db_file, table_name, output_file=None):
    """Exporta una tabla de SQLite a CSV"""
    if not os.path.exists(db_file):
        print(f"Error: {db_file} no existe")
        return
    
    if output_file is None:
        output_file = f"{table_name}.csv"
    
    try:
        conn = sqlite3.connect(db_file)
        cursor = conn.cursor()
        
        # Obtener datos
        cursor.execute(f"SELECT * FROM {table_name}")
        rows = cursor.fetchall()
        
        # Obtener nombres de columnas
        column_names = [description[0] for description in cursor.description]
        
        # Escribir a CSV
        with open(output_file, 'w', newline='', encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow(column_names)
            writer.writerows(rows)
        
        conn.close()
        
        print(f"✓ Tabla exportada: {output_file}")
        print(f"  Registros: {len(rows)}")
        print(f"  Columnas: {len(column_names)}")
    
    except sqlite3.Error as e:
        print(f"✗ Error de SQLite: {e}")
    except Exception as e:
        print(f"✗ Error: {e}")

def list_tables(db_file):
    """Lista todas las tablas en una base de datos SQLite"""
    try:
        conn = sqlite3.connect(db_file)
        cursor = conn.cursor()
        
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table'")
        tables = cursor.fetchall()
        
        conn.close()
        
        print(f"Tablas en {db_file}:")
        for i, table in enumerate(tables, 1):
            print(f"  {i}. {table[0]}")
    
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python sqlite_to_csv.py database.db [tabla] [salida.csv]")
        print("      python sqlite_to_csv.py database.db --list")
        sys.exit(1)
    
    db_path = sys.argv[1]
    
    if '--list' in sys.argv:
        list_tables(db_path)
    elif len(sys.argv) > 2:
        table = sys.argv[2]
        output_csv = sys.argv[3] if len(sys.argv) > 3 else None
        sqlite_to_csv(db_path, table, output_csv)
    else:
        list_tables(db_path)
