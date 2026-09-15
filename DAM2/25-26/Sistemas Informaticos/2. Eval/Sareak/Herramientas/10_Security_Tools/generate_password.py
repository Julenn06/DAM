#!/usr/bin/env python3
"""
Herramienta para generar contraseñas seguras aleatorias
"""
import sys
import secrets
import string

def generate_password(length=16, use_upper=True, use_lower=True, use_digits=True, use_symbols=True):
    """Genera una contraseña segura aleatoria"""
    characters = ''
    
    if use_lower:
        characters += string.ascii_lowercase
    if use_upper:
        characters += string.ascii_uppercase
    if use_digits:
        characters += string.digits
    if use_symbols:
        characters += '!@#$%^&*()_+-=[]{}|;:,.<>?'
    
    if not characters:
        print("Error: Debes seleccionar al menos un tipo de caracteres")
        return None
    
    password = ''.join(secrets.choice(characters) for _ in range(length))
    return password

if __name__ == "__main__":
    length = 16
    count = 1
    no_upper = False
    no_lower = False
    no_digits = False
    no_symbols = False
    
    i = 1
    while i < len(sys.argv):
        if sys.argv[i] == '--length' and i + 1 < len(sys.argv):
            length = int(sys.argv[i + 1])
            i += 2
        elif sys.argv[i] == '--count' and i + 1 < len(sys.argv):
            count = int(sys.argv[i + 1])
            i += 2
        elif sys.argv[i] == '--no-upper':
            no_upper = True
            i += 1
        elif sys.argv[i] == '--no-lower':
            no_lower = True
            i += 1
        elif sys.argv[i] == '--no-digits':
            no_digits = True
            i += 1
        elif sys.argv[i] == '--no-symbols':
            no_symbols = True
            i += 1
        else:
            i += 1
    
    print(f"Generando {count} contraseña(s) de {length} caracteres:\n")
    
    for i in range(count):
        pwd = generate_password(
            length,
            use_upper=not no_upper,
            use_lower=not no_lower,
            use_digits=not no_digits,
            use_symbols=not no_symbols
        )
        
        if pwd:
            print(f"{i+1}. {pwd}")
