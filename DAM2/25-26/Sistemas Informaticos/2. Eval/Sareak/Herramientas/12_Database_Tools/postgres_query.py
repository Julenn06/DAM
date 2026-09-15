#!/usr/bin/env python3
"""
Herramienta para conectar y ejecutar consultas en bases de datos PostgreSQL
"""
import sys

try:
    import psycopg2
    from psycopg2 import sql
except ImportError:
    print("Error: psycopg2 no está instalado")
    print("Instala con: pip install psycopg2-binary")
    sys.exit(1)

def connect_postgres(host, database, user, password, port=5432):
    """Conecta a PostgreSQL y ejecuta una consulta"""
    try:
        conn = psycopg2.connect(
            host=host,
            database=database,
            user=user,
            password=password,
            port=port
        )
        
        return conn
    
    except Exception as e:
        print(f"✗ Error de conexión: {e}")
        return None

def execute_query(conn, query):
    """Ejecuta una consulta"""
    try:
        cursor = conn.cursor()
        cursor.execute(query)
        
        if query.strip().upper().startswith('SELECT'):
            rows = cursor.fetchall()
            columns = [desc[0] for desc in cursor.description]
            
            # Mostrar resultados
            col_widths = [len(col) for col in columns]
            for row in rows:
                for i, val in enumerate(row):
                    col_widths[i] = max(col_widths[i], len(str(val)))
            
            header = ' | '.join([col.ljust(width) for col, width in zip(columns, col_widths)])
            print(header)
            print('-' * len(header))
            
            for row in rows:
                print(' | '.join([str(val).ljust(width) for val, width in zip(row, col_widths)]))
            
            print(f"\n{len(rows)} fila(s) encontrada(s)")
        else:
            conn.commit()
            print(f"✓ Consulta ejecutada")
        
        cursor.close()
    
    except Exception as e:
        print(f"✗ Error: {e}")
        conn.rollback()

if __name__ == "__main__":
    if len(sys.argv) < 6:
        print("Uso: python postgres_query.py host database user password 'SELECT * FROM tabla'")
        sys.exit(1)
    
    pg_host = sys.argv[1]
    pg_db = sys.argv[2]
    pg_user = sys.argv[3]
    pg_pass = sys.argv[4]
    pg_query = sys.argv[5]
    
    connection = connect_postgres(pg_host, pg_db, pg_user, pg_pass)
    
    if connection:
        print(f"✓ Conectado a PostgreSQL: {pg_db}@{pg_host}\n")
        execute_query(connection, pg_query)
        connection.close()
