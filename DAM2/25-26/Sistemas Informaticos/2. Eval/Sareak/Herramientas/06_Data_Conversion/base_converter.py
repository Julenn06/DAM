#!/usr/bin/env python3
"""
Herramienta para convertir entre bases numéricas (binario, decimal, hexadecimal)
"""
import sys

def convert_base(number, from_base, to_base):
    """Convierte un número entre diferentes bases"""
    # Primero convertir a decimal
    if from_base == 'bin':
        decimal = int(number, 2)
    elif from_base == 'oct':
        decimal = int(number, 8)
    elif from_base == 'dec':
        decimal = int(number)
    elif from_base == 'hex':
        decimal = int(number, 16)
    else:
        print(f"Base no soportada: {from_base}")
        return None
    
    # Luego convertir a la base objetivo
    if to_base == 'bin':
        result = bin(decimal)
    elif to_base == 'oct':
        result = oct(decimal)
    elif to_base == 'dec':
        result = str(decimal)
    elif to_base == 'hex':
        result = hex(decimal)
    else:
        print(f"Base no soportada: {to_base}")
        return None
    
    return result

if __name__ == "__main__":
    if len(sys.argv) < 4:
        print("Uso: python base_converter.py numero base_origen base_destino")
        print("Bases: bin, oct, dec, hex")
        print("Ejemplo: python base_converter.py 255 dec hex")
        sys.exit(1)
    
    num = sys.argv[1]
    from_b = sys.argv[2].lower()
    to_b = sys.argv[3].lower()
    
    result = convert_base(num, from_b, to_b)
    
    if result:
        print(f"{num} ({from_b}) = {result} ({to_b})")
