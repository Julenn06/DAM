#!/usr/bin/env python3
"""
Herramienta para convertir XML a JSON
"""
import sys
import json
import xml.etree.ElementTree as ET

def xml_to_dict(element):
    """Convierte un elemento XML a diccionario"""
    result = {}
    
    # Atributos
    if element.attrib:
        result['@attributes'] = element.attrib
    
    # Texto
    if element.text and element.text.strip():
        if len(element) == 0:
            return element.text.strip()
        result['#text'] = element.text.strip()
    
    # Elementos hijos
    for child in element:
        child_data = xml_to_dict(child)
        
        if child.tag in result:
            if not isinstance(result[child.tag], list):
                result[child.tag] = [result[child.tag]]
            result[child.tag].append(child_data)
        else:
            result[child.tag] = child_data
    
    return result

def xml_to_json(xml_file, json_file=None):
    """Convierte un archivo XML a JSON"""
    if json_file is None:
        json_file = xml_file.replace('.xml', '.json')
    
    tree = ET.parse(xml_file)
    root = tree.getroot()
    
    data = {root.tag: xml_to_dict(root)}
    
    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, ensure_ascii=False)
    
    print(f"✓ Convertido: {xml_file} → {json_file}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python xml_to_json.py archivo.xml [salida.json]")
        sys.exit(1)
    
    xml_to_json(sys.argv[1], sys.argv[2] if len(sys.argv) > 2 else None)
