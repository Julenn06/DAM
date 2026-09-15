#!/bin/bash
# Herramienta para hacer backup de un directorio con compresión

if [ $# -lt 2 ]; then
    echo "Uso: ./backup_directory.sh directorio archivo_salida.tar.gz"
    exit 1
fi

SOURCE_DIR="$1"
OUTPUT_FILE="$2"

if [ ! -d "$SOURCE_DIR" ]; then
    echo "Error: $SOURCE_DIR no es un directorio válido"
    exit 1
fi

echo "Creando backup de $SOURCE_DIR..."
echo "Destino: $OUTPUT_FILE"
echo ""

tar -czf "$OUTPUT_FILE" -C "$(dirname "$SOURCE_DIR")" "$(basename "$SOURCE_DIR")"

if [ $? -eq 0 ]; then
    SIZE=$(du -h "$OUTPUT_FILE" | cut -f1)
    echo "✓ Backup completado exitosamente"
    echo "  Tamaño del archivo: $SIZE"
else
    echo "✗ Error al crear el backup"
    exit 1
fi
