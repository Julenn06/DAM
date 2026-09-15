#!/usr/bin/env python3
"""
Herramienta para verificar la fortaleza de contraseñas
"""
import sys
import re

def check_password_strength(password):
    """Evalúa la fortaleza de una contraseña"""
    score = 0
    feedback = []
    
    # Longitud
    length = len(password)
    if length >= 12:
        score += 2
        feedback.append("✓ Longitud adecuada (12+ caracteres)")
    elif length >= 8:
        score += 1
        feedback.append("⚠ Longitud aceptable (8-11 caracteres)")
    else:
        feedback.append("✗ Longitud insuficiente (menos de 8 caracteres)")
    
    # Minúsculas
    if re.search(r'[a-z]', password):
        score += 1
        feedback.append("✓ Contiene minúsculas")
    else:
        feedback.append("✗ No contiene minúsculas")
    
    # Mayúsculas
    if re.search(r'[A-Z]', password):
        score += 1
        feedback.append("✓ Contiene mayúsculas")
    else:
        feedback.append("✗ No contiene mayúsculas")
    
    # Números
    if re.search(r'\d', password):
        score += 1
        feedback.append("✓ Contiene números")
    else:
        feedback.append("✗ No contiene números")
    
    # Símbolos
    if re.search(r'[!@#$%^&*()_+\-=\[\]{}|;:,.<>?]', password):
        score += 2
        feedback.append("✓ Contiene símbolos especiales")
    else:
        feedback.append("✗ No contiene símbolos especiales")
    
    # Patrones comunes
    common_patterns = ['123', 'abc', 'qwerty', 'password', '111', '000']
    if any(pattern in password.lower() for pattern in common_patterns):
        score -= 2
        feedback.append("✗ Contiene patrones comunes")
    
    # Clasificación
    if score >= 7:
        strength = "MUY FUERTE"
        color = "🟢"
    elif score >= 5:
        strength = "FUERTE"
        color = "🟡"
    elif score >= 3:
        strength = "MEDIA"
        color = "🟠"
    else:
        strength = "DÉBIL"
        color = "🔴"
    
    return {
        'score': max(0, score),
        'strength': strength,
        'color': color,
        'feedback': feedback
    }

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python check_password.py 'tu_contraseña'")
        print("      python check_password.py --interactive")
        sys.exit(1)
    
    if sys.argv[1] == '--interactive':
        from getpass import getpass
        password = getpass("Introduce la contraseña a evaluar: ")
    else:
        password = sys.argv[1]
    
    result = check_password_strength(password)
    
    print(f"\n=== Análisis de Contraseña ===\n")
    print(f"Fortaleza: {result['color']} {result['strength']}")
    print(f"Puntuación: {result['score']}/8\n")
    
    print("Detalles:")
    for item in result['feedback']:
        print(f"  {item}")
    
    print("\nRecomendaciones:")
    if result['score'] < 7:
        print("  • Usa al menos 12 caracteres")
        print("  • Combina mayúsculas, minúsculas, números y símbolos")
        print("  • Evita palabras del diccionario y patrones comunes")
        print("  • No uses información personal (nombres, fechas)")
