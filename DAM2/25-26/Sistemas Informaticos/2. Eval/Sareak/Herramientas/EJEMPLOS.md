# 📚 Ejemplos de Uso - Casos Prácticos

Este documento contiene ejemplos prácticos de cómo usar las herramientas en situaciones reales.

## 🎯 Casos de Uso Comunes

### 1. Gestión de Documentos PDF

**Escenario**: Tienes 10 PDFs de un proyecto que necesitas combinar y luego extraer el texto.

```bash
# Paso 1: Combinar todos los PDFs
cd 01_PDF_Tools
python merge_pdfs.py cap1.pdf cap2.pdf cap3.pdf -o proyecto_completo.pdf

# Paso 2: Extraer el texto
python extract_pdf_text.py proyecto_completo.pdf

# Paso 3: Si el PDF es muy grande, comprimirlo
python compress_pdf.py proyecto_completo.pdf proyecto_comprimido.pdf
```

---

### 2. Optimizar Imágenes para Web

**Escenario**: Tienes una carpeta con 100 fotos que necesitas optimizar para web.

```bash
cd 02_Image_Tools

# Redimensionar todas las imágenes a 1920px de ancho
for img in *.jpg; do python resize_image.py "$img" --width 1920; done

# Comprimir con calidad 85
for img in *_resized.jpg; do python compress_image.py "$img" 85; done

# Crear miniaturas
for img in *_compressed.jpg; do python create_thumbnail.py "$img" 200,200; done
```

---

### 3. Análisis de Logs

**Escenario**: Tienes un log gigante con duplicados que necesitas limpiar y analizar.

```bash
cd 03_Text_Tools

# Eliminar líneas duplicadas
python remove_duplicates.py server.log clean.log

# Ordenar líneas por fecha (asumiendo formato específico)
python sort_lines.py clean.log sorted.log

# Buscar y contar errores
python find_replace.py sorted.log "ERROR" "⚠️ ERROR" errors_marked.log

# Contar estadísticas
python count_text_stats.py errors_marked.log
```

---

### 4. Auditoría de Red

**Escenario**: Necesitas verificar la conectividad de varios servidores.

```bash
cd 04_Network_Tools

# Crear lista de servidores
echo "google.com" > servers.txt
echo "github.com" >> servers.txt
echo "8.8.8.8" >> servers.txt

# Verificar conectividad
python ping_hosts.py $(cat servers.txt)

# Escanear puertos comunes en cada uno
python port_scanner.py google.com
python port_scanner.py github.com

# Obtener información de IPs
python ip_info.py 8.8.8.8
```

---

### 5. Limpieza de Proyecto

**Escenario**: Limpar un proyecto antes de subirlo a GitHub.

```bash
cd 05_File_Management

# Encontrar y eliminar duplicados
python find_duplicates.py ./mi_proyecto --delete

# Organizar archivos sueltos
python organize_files.py ./mi_proyecto/descargas

# Calcular tamaño antes de subir
python dir_size.py ./mi_proyecto --subdirs

# Buscar archivos grandes
python find_files.py "*.zip" ./mi_proyecto
```

---

### 6. Conversión de Datos

**Escenario**: Convertir datos entre diferentes formatos para análisis.

```bash
cd 06_Data_Conversion

# Base de datos CSV a JSON
python csv_to_json.py clientes.csv clientes.json

# Formatear JSON para legibilidad
python beautify_json.py clientes.json clientes_formatted.json

# Convertir configuración YAML a JSON
python yaml_to_json.py config.yaml config.json

# Convertir números
python base_converter.py FF hex dec  # Hexadecimal a decimal
```

---

### 7. Web Scraping Básico

**Escenario**: Extraer contenido de un sitio web.

```bash
cd 07_Web_Tools

# Extraer todos los enlaces
python extract_links.py https://ejemplo.com --output enlaces.txt

# Descargar todas las imágenes
python download_images.py https://ejemplo.com ./imagenes

# Extraer metadatos SEO
python extract_metadata.py https://ejemplo.com

# Generar sitemap
python generate_sitemap.py https://ejemplo.com 100 sitemap.xml
```

---

### 8. Monitoreo de Sistema

**Escenario**: Detectar un proceso que consume muchos recursos.

```bash
cd 08_System_Tools

# Ver información general
powershell -File Get-SystemInfo.ps1

# Monitorear sistema por 60 segundos
python monitor_system.py 1 60

# Listar procesos que más CPU usan
python list_processes.py 30

# Encontrar proceso problemático
python list_processes.py --memory 50

# Terminar proceso
python kill_process.py nombre_proceso
```

---

### 9. Edición de Video Rápida

**Escenario**: Crear clips cortos de un video largo y convertirlos.

```bash
cd 09_Media_Tools

# Ver información del video
python media_info.py video_largo.mp4

# Recortar segmento interesante
python trim_video.py video_largo.mp4 00:05:30 --duration 00:01:00

# Crear GIF del momento mejor
python video_to_gif.py video_largo_trimmed.mp4 --start 10 --duration 5

# Extraer audio
python extract_audio.py video_largo.mp4 audio.mp3

# Convertir a formato web
python convert_video.py video_largo_trimmed.mp4 web_video.webm medium
```

---

### 10. Seguridad de Archivos

**Escenario**: Proteger documentos sensibles antes de compartir.

```bash
cd 10_Security_Tools

# Calcular hash para verificar integridad
python hash_file.py documento_importante.pdf sha256

# Cifrar documento
python encrypt_file.py encrypt documento.pdf documento.enc MiContraseña123!

# Generar contraseña segura para compartir
python generate_password.py --length 20 --count 1

# Escanear directorio en busca de archivos sensibles
python scan_sensitive.py ./proyecto

# Borrado seguro de archivo temporal
python secure_delete.py archivo_temporal.txt 3
```

---

### 11. Desarrollo de Software

**Escenario**: Preparar un proyecto para publicación.

```bash
cd 11_Development_Tools

# Contar líneas de código
python count_lines.py ./mi_app

# Encontrar TODOs pendientes
python find_todos.py ./mi_app

# Generar .gitignore apropiado
python generate_gitignore.py python vscode node --output .gitignore

# Formatear código Python
python format_code.py app.py

# Generar documentación
python generate_docs.py main.py docs.md

# Verificar vulnerabilidades (Node.js)
node check_vulnerabilities.js
```

---

### 12. Gestión de Bases de Datos

**Escenario**: Backup, migración y análisis de datos.

```bash
cd 12_Database_Tools

# Backup de MySQL
python backup_mysql.py localhost root password mi_db ./backups

# Exportar tabla SQLite a CSV para análisis
python sqlite_to_csv.py app.db usuarios usuarios.csv

# Importar datos de CSV
python csv_to_sqlite.py nuevos_datos.csv app.db tabla_nueva

# Consultar datos
python query_sqlite.py app.db "SELECT * FROM usuarios WHERE activo=1"

# Ver estructura de la base de datos
python db_schema.py app.db

# Consulta en PostgreSQL
python postgres_query.py localhost mi_db admin password "SELECT COUNT(*) FROM ventas"
```

---

## 🔗 Combinando Herramientas

### Ejemplo 1: Pipeline de Procesamiento de Documentos

```bash
# 1. Combinar PDFs
python 01_PDF_Tools/merge_pdfs.py *.pdf -o completo.pdf

# 2. Extraer texto
python 01_PDF_Tools/extract_pdf_text.py completo.pdf > texto.txt

# 3. Limpiar texto
python 03_Text_Tools/remove_duplicates.py texto.txt texto_limpio.txt

# 4. Contar estadísticas
python 03_Text_Tools/count_text_stats.py texto_limpio.txt
```

### Ejemplo 2: Auditoría Completa de Proyecto

```bash
# 1. Escanear archivos sensibles
python 10_Security_Tools/scan_sensitive.py ./proyecto > sensitive.log

# 2. Buscar TODOs
python 11_Development_Tools/find_todos.py ./proyecto > todos.log

# 3. Contar líneas
python 11_Development_Tools/count_lines.py ./proyecto > stats.log

# 4. Encontrar duplicados
python 05_File_Management/find_duplicates.py ./proyecto > duplicates.log
```

---

## 💡 Tips y Trucos

### Automatización con Scripts

Crea un script para tareas repetitivas:

```bash
# backup_diario.sh
#!/bin/bash

DATE=$(date +%Y%m%d)

# Backup de base de datos
python 12_Database_Tools/backup_mysql.py localhost root pass mi_db ./backups

# Comprimir directorio
tar -czf backup_$DATE.tar.gz ./datos

# Calcular hash
python 10_Security_Tools/hash_file.py backup_$DATE.tar.gz sha256 > hash_$DATE.txt
```

### Alias Útiles

Añade a tu `.bashrc` o perfil de PowerShell:

```bash
alias cuenta='python /path/to/count_text_stats.py'
alias busca='python /path/to/find_files.py'
alias ping-multi='python /path/to/ping_hosts.py'
```

---

## 🎓 Ejercicios Prácticos

1. **Organización de Fotos**: Usa las herramientas de imágenes para organizar, redimensionar y optimizar una colección de fotos.

2. **Análisis Web**: Extrae enlaces de 5 sitios web diferentes y crea un CSV con todos ellos.

3. **Limpieza de Sistema**: Usa las herramientas de sistema para limpiar temporales, encontrar duplicados y liberar espacio.

4. **Proyecto de Datos**: Convierte datos CSV a JSON, impórtalos a SQLite y haz consultas.

5. **Pipeline de Video**: Toma un video, extrae clips, crea GIFs y optimiza para web.

---

**¡Experimenta y descubre nuevas combinaciones!**
