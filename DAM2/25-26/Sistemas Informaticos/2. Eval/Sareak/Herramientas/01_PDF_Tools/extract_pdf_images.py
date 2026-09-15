#!/usr/bin/env python3
"""
Herramienta para extraer imágenes de archivos PDF
Extrae todas las imágenes del PDF y genera un manifest JSON con la información
"""
from __future__ import annotations

import json
import sys
from pathlib import Path

import fitz  # PyMuPDF


def extract_images_from_pdf(pdf_path: Path, output_dir: Path | None = None, create_manifest: bool = True) -> None:
    """Extrae todas las imágenes de un PDF"""
    if not pdf_path.exists():
        print(f"Error: El archivo {pdf_path} no existe")
        sys.exit(1)

    if output_dir is None:
        output_dir = pdf_path.parent / f"{pdf_path.stem}_images"
    
    output_dir.mkdir(parents=True, exist_ok=True)

    print(f"Extrayendo imágenes de: {pdf_path}")
    print(f"Directorio de salida: {output_dir}\n")

    try:
        doc = fitz.open(pdf_path)

        manifest: dict[str, object] = {
            "source": str(pdf_path),
            "pages": doc.page_count,
            "images": [],
        }

        extracted_count = 0

        for page_index in range(doc.page_count):
            page = doc.load_page(page_index)
            page_number = page_index + 1

            images = page.get_images(full=True)
            page_entries = []

            # Eliminar duplicados por xref dentro de la página
            seen_xref: set[int] = set()

            for img_i, img in enumerate(images, start=1):
                xref = int(img[0])
                if xref in seen_xref:
                    continue
                seen_xref.add(xref)

                info = doc.extract_image(xref)
                ext = info.get("ext", "bin")
                image_bytes = info["image"]

                file_name = f"p{page_number:02d}_img{img_i:02d}.{ext}"
                file_path = output_dir / file_name
                file_path.write_bytes(image_bytes)

                entry = {
                    "page": page_number,
                    "xref": xref,
                    "ext": ext,
                    "filename": file_name,
                    "width": info.get("width"),
                    "height": info.get("height"),
                }
                page_entries.append(entry)
                extracted_count += 1
                
                size_kb = len(image_bytes) / 1024
                print(f"✓ Página {page_number}: {file_name} ({info.get('width')}x{info.get('height')}, {size_kb:.1f} KB)")

            # Solo registrar páginas que tienen al menos una imagen
            if page_entries:
                manifest["images"].extend(page_entries)

        # Generar manifest JSON
        if create_manifest:
            manifest_path = output_dir / "manifest.json"
            manifest_path.write_text(json.dumps(manifest, ensure_ascii=False, indent=2), encoding="utf-8")
            print(f"\n✓ Manifest generado: {manifest_path}")

        print(f"\n--- Resumen ---")
        print(f"Páginas: {doc.page_count}")
        print(f"Imágenes extraídas: {extracted_count}")
        print(f"Directorio: {output_dir}")
    
    except Exception as e:
        print(f"✗ Error al procesar el PDF: {e}")
        sys.exit(1)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python extract_pdf_images.py archivo.pdf [directorio_salida] [--no-manifest]")
        print("Extrae todas las imágenes de un archivo PDF")
        print("\nOpciones:")
        print("  --no-manifest  No generar archivo manifest.json")
        sys.exit(1)
    
    input_pdf = Path(sys.argv[1])
    output_folder = Path(sys.argv[2]) if len(sys.argv) > 2 and not sys.argv[2].startswith('--') else None
    no_manifest = '--no-manifest' in sys.argv
    
    extract_images_from_pdf(input_pdf, output_folder, create_manifest=not no_manifest)
