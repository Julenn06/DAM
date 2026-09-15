#!/usr/bin/env python3
"""
Herramienta para ejecutar consultas SQL en bases de datos SQLite
"""
import sys
import sqlite3
import os

def execute_query(db_file, query, output_format='table'):
    """Ejecuta una consulta SQL y muestra los resultados"""
    if not os.path.exists(db_file):
        print(f"Error: {db_file} no existe")
        return
    
    try:
        conn = sqlite3.connect(db_file)
        cursor = conn.cursor()
        
        cursor.execute(query)
        
        # Si es SELECT, mostrar resultados
        if query.strip().upper().startswith('SELECT'):
            rows = cursor.fetchall()
            columns = [description[0] for description in cursor.description]
            
            if output_format == 'table':
                # Calcular anchos de columna
                col_widths = [len(col) for col in columns]
                for row in rows:
                    for i, val in enumerate(row):
                        col_widths[i] = max(col_widths[i], len(str(val)))
                
                # Imprimir cabecera
                header = ' | '.join([col.ljust(width) for col, width in zip(columns, col_widths)])
                print(header)
                print('-' * len(header))
                
                # Imprimir filas
                for row in rows:
                    print(' | '.join([str(val).ljust(width) for val, width in zip(row, col_widths)]))
                
                print(f"\n{len(rows)} fila(s) encontrada(s)")
            
            elif output_format == 'csv':
                # Formato CSV
                print(','.join(columns))
                for row in rows:
                    print(','.join([str(val) for val in row]))
        
        else:
            # Para INSERT, UPDATE, DELETE
            conn.commit()
            print(f"✓ Consulta ejecutada exitosamente")
            print(f"  Filas afectadas: {cursor.rowcount}")
        
        conn.close()
    
    except sqlite3.Error as e:
        print(f"✗ Error SQL: {e}")
    except Exception as e:
        print(f"✗ Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Uso: python query_sqlite.py database.db 'SELECT * FROM tabla'")
        print("      python query_sqlite.py database.db 'SELECT * FROM tabla' --csv")
        sys.exit(1)
    
    db_path = sys.argv[1]
    sql_query = sys.argv[2]
    fmt = 'csv' if '--csv' in sys.argv else 'table'
    
    execute_query(db_path, sql_query, fmt)
