# 🛠️ Colección de Herramientas Multifuncionales

Colección completa de herramientas de línea de comandos organizadas por categorías. Más de 60 utilidades en Python, JavaScript, PowerShell y Bash para automatizar tareas cotidianas.

## 📋 Índice

- [01 - PDF Tools](#01-pdf-tools)
- [02 - Image Tools](#02-image-tools)
- [03 - Text Tools](#03-text-tools)
- [04 - Network Tools](#04-network-tools)
- [05 - File Management](#05-file-management)
- [06 - Data Conversion](#06-data-conversion)
- [07 - Web Tools](#07-web-tools)
- [08 - System Tools](#08-system-tools)
- [09 - Media Tools](#09-media-tools)
- [10 - Security Tools](#10-security-tools)
- [11 - Development Tools](#11-development-tools)
- [12 - Database Tools](#12-database-tools)

---

## 01 PDF Tools

### extract_pdf_text.py
Extrae texto de archivos PDF
```bash
python extract_pdf_text.py documento.pdf
```

### extract_pdf_images.py
Extrae imágenes de archivos PDF
```bash
python extract_pdf_images.py documento.pdf carpeta_salida
```

### merge_pdfs.py
Combina múltiples PDFs en uno solo
```bash
python merge_pdfs.py archivo1.pdf archivo2.pdf -o salida.pdf
```

### split_pdf.py
Divide un PDF en páginas individuales
```bash
python split_pdf.py documento.pdf carpeta_salida
```

### compress_pdf.py
Comprime archivos PDF reduciendo calidad
```bash
python compress_pdf.py documento.pdf salida.pdf
```

### rotate_pdf.py
Rota páginas de un PDF
```bash
python rotate_pdf.py documento.pdf salida.pdf 90
```

### pdf_to_images.py
Convierte cada página del PDF a imagen
```bash
python pdf_to_images.py documento.pdf carpeta PNG 200
```

---

## 02 Image Tools

### resize_image.py
Redimensiona imágenes manteniendo proporción
```bash
python resize_image.py imagen.jpg --width 800
python resize_image.py imagen.jpg --scale 0.5
```

### convert_image.py
Convierte imágenes entre formatos
```bash
python convert_image.py imagen.png jpg
```

### compress_image.py
Comprime imágenes reduciendo calidad
```bash
python compress_image.py imagen.jpg salida.jpg 85
```

### create_thumbnail.py
Crea miniaturas de imágenes
```bash
python create_thumbnail.py imagen.jpg thumb.jpg 128,128
```

### watermark_image.py
Añade marcas de agua a imágenes
```bash
python watermark_image.py imagen.jpg "Mi Marca" salida.jpg bottom-right
```

### rotate_image.py
Rota y voltea imágenes
```bash
python rotate_image.py imagen.jpg 90
python rotate_image.py imagen.jpg --flip horizontal
```

---

## 03 Text Tools

### count_text_stats.py
Cuenta palabras, líneas y caracteres
```bash
python count_text_stats.py archivo.txt
```

### find_replace.py
Busca y reemplaza texto en archivos
```bash
python find_replace.py archivo.txt "buscar" "reemplazar"
```

### remove_duplicates.py
Elimina líneas duplicadas
```bash
python remove_duplicates.py archivo.txt salida.txt
```

### sort_lines.py
Ordena líneas alfabéticamente
```bash
python sort_lines.py archivo.txt --reverse
```

### merge_text_files.py
Combina múltiples archivos de texto
```bash
python merge_text_files.py archivo1.txt archivo2.txt -o salida.txt
```

### split_text_file.py
Divide un archivo en archivos más pequeños
```bash
python split_text_file.py archivo.txt 100 carpeta_salida
```

---

## 04 Network Tools

### ping_hosts.py
Hace ping a múltiples hosts
```bash
python ping_hosts.py google.com 8.8.8.8
```

### port_scanner.py
Escanea puertos abiertos
```bash
python port_scanner.py google.com 1-1000
```

### ip_info.py
Obtiene información de geolocalización de IPs
```bash
python ip_info.py 8.8.8.8
```

### http_request.py
Hace peticiones HTTP GET/POST
```bash
python http_request.py https://api.github.com GET
```

### download_file.py
Descarga archivos desde URLs
```bash
python download_file.py https://ejemplo.com/archivo.zip
```

### dns_lookup.js
Consulta registros DNS
```bash
node dns_lookup.js google.com
```

---

## 05 File Management

### find_files.py
Busca archivos por patrón
```bash
python find_files.py "*.txt" C:\Users
```

### find_duplicates.py
Encuentra y elimina archivos duplicados
```bash
python find_duplicates.py directorio --delete
```

### batch_rename.py
Renombra archivos en lote
```bash
python batch_rename.py ./fotos "IMG_" "Foto_"
```

### organize_files.py
Organiza archivos por extensión
```bash
python organize_files.py directorio
```

### dir_size.py
Calcula tamaño de directorios
```bash
python dir_size.py directorio --subdirs
```

### backup_directory.sh
Crea backup comprimido de un directorio
```bash
./backup_directory.sh directorio backup.tar.gz
```

---

## 06 Data Conversion

### csv_to_json.py
Convierte CSV a JSON
```bash
python csv_to_json.py datos.csv salida.json
```

### json_to_csv.py
Convierte JSON a CSV
```bash
python json_to_csv.py datos.json salida.csv
```

### xml_to_json.py
Convierte XML a JSON
```bash
python xml_to_json.py datos.xml salida.json
```

### beautify_json.py
Formatea y embellece JSON
```bash
python beautify_json.py datos.json salida.json 2
```

### yaml_to_json.py / json_to_yaml.py
Convierte entre YAML y JSON
```bash
python yaml_to_json.py config.yaml
python json_to_yaml.py datos.json
```

### base_converter.py
Convierte entre bases numéricas
```bash
python base_converter.py 255 dec hex
```

---

## 07 Web Tools

### extract_links.py
Extrae todos los enlaces de una web
```bash
python extract_links.py https://ejemplo.com --output enlaces.txt
```

### download_images.py
Descarga todas las imágenes de una web
```bash
python download_images.py https://ejemplo.com carpeta_imagenes
```

### generate_sitemap.py
Genera sitemap XML de un sitio
```bash
python generate_sitemap.py https://ejemplo.com 50
```

### check_websites.py
Verifica el estado de múltiples sitios
```bash
python check_websites.py sitio1.com sitio2.com
python check_websites.py --file urls.txt
```

### extract_metadata.py
Extrae metadatos de páginas web
```bash
python extract_metadata.py https://ejemplo.com
```

### screenshot.js
Captura screenshots de páginas web
```bash
node screenshot.js https://ejemplo.com captura.png --full-page
```

---

## 08 System Tools

### Get-SystemInfo.ps1
Obtiene información completa del sistema (PowerShell)
```powershell
.\Get-SystemInfo.ps1
```

### monitor_system.py
Monitorea CPU y memoria en tiempo real
```bash
python monitor_system.py 1 60
```

### list_processes.py
Lista procesos con uso de recursos
```bash
python list_processes.py --memory 20
```

### kill_process.py
Termina procesos por nombre o PID
```bash
python kill_process.py chrome
python kill_process.py 1234
```

### clean_temp.py
Limpia archivos temporales
```bash
python clean_temp.py --dry-run
```

### Create-RestorePoint.ps1
Crea punto de restauración del sistema
```powershell
.\Create-RestorePoint.ps1 "Mi punto de restauración"
```

---

## 09 Media Tools

### extract_audio.py
Extrae audio de videos
```bash
python extract_audio.py video.mp4 audio.mp3
```

### convert_video.py
Convierte videos entre formatos
```bash
python convert_video.py video.avi salida.mp4 high
```

### media_info.py
Obtiene información detallada de archivos multimedia
```bash
python media_info.py video.mp4
```

### trim_video.py
Recorta videos
```bash
python trim_video.py video.mp4 00:01:30 --duration 00:00:30
```

### video_to_gif.py
Crea GIF animado desde video
```bash
python video_to_gif.py video.mp4 --start 10 --duration 5 --fps 15
```

### normalize_audio.py
Normaliza el volumen de archivos de audio
```bash
python normalize_audio.py audio.mp3 --level -20
```

---

## 10 Security Tools

### generate_password.py
Genera contraseñas seguras
```bash
python generate_password.py --length 16 --count 5
```

### hash_file.py
Calcula hash de archivos
```bash
python hash_file.py archivo.zip sha256
```

### encrypt_file.py
Cifra y descifra archivos con AES
```bash
python encrypt_file.py encrypt documento.txt documento.enc micontraseña
python encrypt_file.py decrypt documento.enc documento.txt micontraseña
```

### check_password.py
Verifica la fortaleza de contraseñas
```bash
python check_password.py "tu_contraseña"
python check_password.py --interactive
```

### scan_sensitive.py
Escanea archivos sensibles en directorios
```bash
python scan_sensitive.py directorio --depth 5
```

### secure_delete.py
Borrado seguro de archivos
```bash
python secure_delete.py archivo_secreto.txt 3
```

---

## 11 Development Tools

### format_code.py
Formatea código (Python, JavaScript, JSON)
```bash
python format_code.py archivo.py
python format_code.py datos.json
```

### count_lines.py
Cuenta líneas de código en proyectos
```bash
python count_lines.py directorio_proyecto
```

### generate_docs.py
Genera documentación desde docstrings
```bash
python generate_docs.py archivo.py documentacion.md
```

### find_todos.py
Encuentra TODOs y FIXMEs en el código
```bash
python find_todos.py directorio_proyecto
```

### generate_gitignore.py
Genera archivos .gitignore personalizados
```bash
python generate_gitignore.py python node visualstudiocode
```

### check_vulnerabilities.js
Verifica vulnerabilidades en dependencias de Node.js
```bash
node check_vulnerabilities.js
```

---

## 12 Database Tools

### backup_mysql.py
Backup de bases de datos MySQL
```bash
python backup_mysql.py localhost root password mydb ./backups
```

### sqlite_to_csv.py
Exporta tablas SQLite a CSV
```bash
python sqlite_to_csv.py database.db tabla salida.csv
python sqlite_to_csv.py database.db --list
```

### csv_to_sqlite.py
Importa CSV a SQLite
```bash
python csv_to_sqlite.py datos.csv database.db nombre_tabla
```

### query_sqlite.py
Ejecuta consultas SQL en SQLite
```bash
python query_sqlite.py database.db "SELECT * FROM tabla"
```

### db_schema.py
Obtiene información de esquema de SQLite
```bash
python db_schema.py database.db
```

### postgres_query.py
Conecta y ejecuta consultas en PostgreSQL
```bash
python postgres_query.py host database user password "SELECT * FROM tabla"
```

---

## 📦 Dependencias

### Python
```bash
pip install PyPDF2 pdf2image Pillow requests beautifulsoup4 psutil cryptography pyyaml
```

### Herramientas externas opcionales
- **FFmpeg**: Para herramientas multimedia (extract_audio, convert_video, etc.)
- **mysqldump**: Para backup_mysql.py
- **Black**: Para format_code.py (Python)
- **Prettier**: Para format_code.py (JavaScript)

### Node.js
```bash
npm install puppeteer
```

---

## 🚀 Uso General

Todas las herramientas incluyen ayuda integrada:
```bash
python herramienta.py
python herramienta.py --help
```

---

## 💡 Consejos

1. **Añade las rutas al PATH** para acceso rápido
2. **Crea alias** para las herramientas más usadas
3. **Usa scripts batch/shell** para combinar herramientas
4. **Revisa los permisos** antes de ejecutar herramientas de sistema

---

## 📝 Licencia

Herramientas de uso libre para propósitos educativos y personales.

---

## 🤝 Contribuciones

Estas herramientas son extensibles. Puedes:
- Añadir más funcionalidades
- Mejorar el manejo de errores
- Optimizar el rendimiento
- Crear nuevas herramientas siguiendo la estructura

---

**Última actualización**: Diciembre 2025
