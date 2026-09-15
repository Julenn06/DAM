#!/usr/bin/env python3
"""
Herramienta para extraer texto de archivos PDF
Extrae el texto de cada página y lo guarda en un archivo de texto
"""
from __future__ import annotations

import sys
from pathlib import Path

from pypdf import PdfReader


def extract_text_from_pdf(pdf_path: Path, output_path: Path | None = None) -> None:
    """Extrae el texto de un PDF y lo guarda en un archivo de texto"""
    if not pdf_path.exists():
        print(f"Error: El archivo {pdf_path} no existe")
        sys.exit(1)

    if output_path is None:
        output_path = pdf_path.with_suffix(".extracted.txt")

    print(f"Extrayendo texto de: {pdf_path}")
    
    try:
        reader = PdfReader(str(pdf_path))
        chunks: list[str] = []
        
        for i, page in enumerate(reader.pages, start=1):
            text = (page.extract_text() or "").strip("\n")
            chunks.append(f"\n\n===== PÁGINA {i} =====\n\n{text}\n")
            print(f"✓ Página {i}/{len(reader.pages)} procesada")

        output_path.write_text("".join(chunks), encoding="utf-8")
        
        print(f"\n✓ Texto extraído: {output_path}")
        print(f"  Páginas: {len(reader.pages)}")
        print(f"  Tamaño: {output_path.stat().st_size / 1024:.2f} KB")
    
    except Exception as e:
        print(f"✗ Error al procesar el PDF: {e}")
        sys.exit(1)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python extract_pdf_text.py archivo.pdf [salida.txt]")
        print("Extrae el texto de un archivo PDF")
        sys.exit(1)
    
    input_pdf = Path(sys.argv[1])
    output_txt = Path(sys.argv[2]) if len(sys.argv) > 2 else None
    
    extract_text_from_pdf(input_pdf, output_txt)
