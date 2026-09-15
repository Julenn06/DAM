#!/usr/bin/env python3
"""
Herramienta para cifrar y descifrar archivos usando AES
"""
import sys
import os
from cryptography.fernet import Fernet
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2
import base64

def derive_key(password, salt):
    """Deriva una clave desde una contraseña"""
    kdf = PBKDF2(
        algorithm=hashes.SHA256(),
        length=32,
        salt=salt,
        iterations=100000,
    )
    key = base64.urlsafe_b64encode(kdf.derive(password.encode()))
    return key

def encrypt_file(input_file, output_file, password):
    """Cifra un archivo"""
    salt = os.urandom(16)
    key = derive_key(password, salt)
    fernet = Fernet(key)
    
    with open(input_file, 'rb') as f:
        data = f.read()
    
    encrypted_data = fernet.encrypt(data)
    
    with open(output_file, 'wb') as f:
        f.write(salt + encrypted_data)
    
    print(f"✓ Archivo cifrado: {output_file}")

def decrypt_file(input_file, output_file, password):
    """Descifra un archivo"""
    with open(input_file, 'rb') as f:
        salt = f.read(16)
        encrypted_data = f.read()
    
    key = derive_key(password, salt)
    fernet = Fernet(key)
    
    try:
        decrypted_data = fernet.decrypt(encrypted_data)
        
        with open(output_file, 'wb') as f:
            f.write(decrypted_data)
        
        print(f"✓ Archivo descifrado: {output_file}")
    
    except Exception as e:
        print(f"✗ Error al descifrar: Contraseña incorrecta o archivo corrupto")

if __name__ == "__main__":
    if len(sys.argv) < 4:
        print("Uso:")
        print("  Cifrar:    python encrypt_file.py encrypt archivo.txt salida.enc contraseña")
        print("  Descifrar: python encrypt_file.py decrypt archivo.enc salida.txt contraseña")
        sys.exit(1)
    
    mode = sys.argv[1].lower()
    input_f = sys.argv[2]
    output_f = sys.argv[3]
    pwd = sys.argv[4] if len(sys.argv) > 4 else input("Contraseña: ")
    
    if mode == 'encrypt':
        encrypt_file(input_f, output_f, pwd)
    elif mode == 'decrypt':
        decrypt_file(input_f, output_f, pwd)
    else:
        print("Modo inválido. Usa 'encrypt' o 'decrypt'")
