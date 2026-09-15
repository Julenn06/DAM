#!/usr/bin/env python3
"""
Herramienta para obtener información de esquema de bases de datos SQLite
"""
import sys
import sqlite3
import os

def get_database_info(db_file):
    """Obtiene información completa del esquema de la base de datos"""
    if not os.path.exists(db_file):
        print(f"Error: {db_file} no existe")
        return
    
    try:
        conn = sqlite3.connect(db_file)
        cursor = conn.cursor()
        
        # Obtener todas las tablas
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table'")
        tables = cursor.fetchall()
        
        print(f"=== Base de datos: {db_file} ===\n")
        print(f"Tablas: {len(tables)}\n")
        
        for table in tables:
            table_name = table[0]
            
            print(f"--- Tabla: {table_name} ---")
            
            # Información de columnas
            cursor.execute(f"PRAGMA table_info({table_name})")
            columns = cursor.fetchall()
            
            print("Columnas:")
            for col in columns:
                col_id, col_name, col_type, not_null, default_val, is_pk = col
                pk_str = " PRIMARY KEY" if is_pk else ""
                null_str = " NOT NULL" if not_null else ""
                default_str = f" DEFAULT {default_val}" if default_val else ""
                
                print(f"  {col_name}: {col_type}{pk_str}{null_str}{default_str}")
            
            # Contar registros
            cursor.execute(f"SELECT COUNT(*) FROM {table_name}")
            count = cursor.fetchone()[0]
            print(f"Registros: {count}")
            
            # Índices
            cursor.execute(f"PRAGMA index_list({table_name})")
            indexes = cursor.fetchall()
            if indexes:
                print("Índices:")
                for idx in indexes:
                    print(f"  {idx[1]} (unique: {bool(idx[2])})")
            
            print()
        
        # Tamaño del archivo
        file_size = os.path.getsize(db_file)
        print(f"Tamaño del archivo: {file_size / 1024:.2f} KB")
        
        conn.close()
    
    except sqlite3.Error as e:
        print(f"✗ Error SQL: {e}")
    except Exception as e:
        print(f"✗ Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python db_schema.py database.db")
        sys.exit(1)
    
    db_path = sys.argv[1]
    get_database_info(db_path)
