#!/usr/bin/env python3
"""
Menú interactivo para acceder a todas las herramientas
"""
import os
import sys

TOOLS_BY_CATEGORY = {
    "01_PDF_Tools": [
        ("Extraer texto de PDF", "extract_pdf_text.py"),
        ("Extraer imágenes de PDF", "extract_pdf_images.py"),
        ("Combinar PDFs", "merge_pdfs.py"),
        ("Dividir PDF", "split_pdf.py"),
        ("Comprimir PDF", "compress_pdf.py"),
        ("Rotar PDF", "rotate_pdf.py"),
        ("PDF a imágenes", "pdf_to_images.py"),
    ],
    "02_Image_Tools": [
        ("Redimensionar imagen", "resize_image.py"),
        ("Convertir formato de imagen", "convert_image.py"),
        ("Comprimir imagen", "compress_image.py"),
        ("Crear miniatura", "create_thumbnail.py"),
        ("Añadir marca de agua", "watermark_image.py"),
        ("Rotar/voltear imagen", "rotate_image.py"),
    ],
    "03_Text_Tools": [
        ("Contar estadísticas de texto", "count_text_stats.py"),
        ("Buscar y reemplazar", "find_replace.py"),
        ("Eliminar duplicados", "remove_duplicates.py"),
        ("Ordenar líneas", "sort_lines.py"),
        ("Combinar archivos", "merge_text_files.py"),
        ("Dividir archivo", "split_text_file.py"),
    ],
    "04_Network_Tools": [
        ("Ping a hosts", "ping_hosts.py"),
        ("Escanear puertos", "port_scanner.py"),
        ("Info de IP", "ip_info.py"),
        ("Petición HTTP", "http_request.py"),
        ("Descargar archivo", "download_file.py"),
        ("DNS lookup", "dns_lookup.js"),
    ],
    "05_File_Management": [
        ("Buscar archivos", "find_files.py"),
        ("Encontrar duplicados", "find_duplicates.py"),
        ("Renombrar en lote", "batch_rename.py"),
        ("Organizar por extensión", "organize_files.py"),
        ("Calcular tamaño", "dir_size.py"),
    ],
    "06_Data_Conversion": [
        ("CSV a JSON", "csv_to_json.py"),
        ("JSON a CSV", "json_to_csv.py"),
        ("XML a JSON", "xml_to_json.py"),
        ("Formatear JSON", "beautify_json.py"),
        ("YAML <-> JSON", "yaml_to_json.py"),
        ("Convertir bases numéricas", "base_converter.py"),
    ],
    "07_Web_Tools": [
        ("Extraer enlaces", "extract_links.py"),
        ("Descargar imágenes web", "download_images.py"),
        ("Generar sitemap", "generate_sitemap.py"),
        ("Verificar sitios", "check_websites.py"),
        ("Extraer metadatos", "extract_metadata.py"),
        ("Capturar screenshot", "screenshot.js"),
    ],
    "08_System_Tools": [
        ("Info del sistema", "Get-SystemInfo.ps1"),
        ("Monitorear sistema", "monitor_system.py"),
        ("Listar procesos", "list_processes.py"),
        ("Terminar proceso", "kill_process.py"),
        ("Limpiar temporales", "clean_temp.py"),
    ],
    "09_Media_Tools": [
        ("Extraer audio", "extract_audio.py"),
        ("Convertir video", "convert_video.py"),
        ("Info multimedia", "media_info.py"),
        ("Recortar video", "trim_video.py"),
        ("Video a GIF", "video_to_gif.py"),
        ("Normalizar audio", "normalize_audio.py"),
    ],
    "10_Security_Tools": [
        ("Generar contraseña", "generate_password.py"),
        ("Hash de archivo", "hash_file.py"),
        ("Cifrar/descifrar", "encrypt_file.py"),
        ("Verificar contraseña", "check_password.py"),
        ("Escanear archivos sensibles", "scan_sensitive.py"),
        ("Borrado seguro", "secure_delete.py"),
    ],
    "11_Development_Tools": [
        ("Formatear código", "format_code.py"),
        ("Contar líneas", "count_lines.py"),
        ("Generar documentación", "generate_docs.py"),
        ("Buscar TODOs", "find_todos.py"),
        ("Generar .gitignore", "generate_gitignore.py"),
        ("Verificar vulnerabilidades", "check_vulnerabilities.js"),
    ],
    "12_Database_Tools": [
        ("Backup MySQL", "backup_mysql.py"),
        ("SQLite a CSV", "sqlite_to_csv.py"),
        ("CSV a SQLite", "csv_to_sqlite.py"),
        ("Consultar SQLite", "query_sqlite.py"),
        ("Esquema de DB", "db_schema.py"),
        ("Consultar PostgreSQL", "postgres_query.py"),
    ],
}

def clear_screen():
    """Limpia la pantalla"""
    os.system('cls' if os.name == 'nt' else 'clear')

def show_main_menu():
    """Muestra el menú principal"""
    clear_screen()
    print("=" * 60)
    print("  🛠️  MENÚ DE HERRAMIENTAS  🛠️")
    print("=" * 60)
    print()
    
    categories = list(TOOLS_BY_CATEGORY.keys())
    
    for i, category in enumerate(categories, 1):
        category_name = category.replace('_', ' ').title()
        print(f"  {i:2d}. {category_name}")
    
    print(f"  {len(categories) + 1:2d}. Salir")
    print()
    print("=" * 60)

def show_category_menu(category):
    """Muestra el menú de una categoría"""
    clear_screen()
    category_name = category.replace('_', ' ').title()
    print("=" * 60)
    print(f"  📁 {category_name}")
    print("=" * 60)
    print()
    
    tools = TOOLS_BY_CATEGORY[category]
    
    for i, (name, _) in enumerate(tools, 1):
        print(f"  {i:2d}. {name}")
    
    print(f"  {len(tools) + 1:2d}. Volver al menú principal")
    print()
    print("=" * 60)

def run_tool(category, tool_file):
    """Ejecuta una herramienta"""
    tool_path = os.path.join(category, tool_file)
    
    if not os.path.exists(tool_path):
        print(f"\n✗ Error: {tool_path} no existe")
        input("\nPresiona Enter para continuar...")
        return
    
    clear_screen()
    print(f"Ejecutando: {tool_file}")
    print("=" * 60)
    print()
    
    # Determinar cómo ejecutar según la extensión
    if tool_file.endswith('.py'):
        os.system(f'python {tool_path}')
    elif tool_file.endswith('.js'):
        os.system(f'node {tool_path}')
    elif tool_file.endswith('.ps1'):
        os.system(f'powershell -ExecutionPolicy Bypass -File {tool_path}')
    elif tool_file.endswith('.sh'):
        os.system(f'bash {tool_path}')
    
    print()
    input("\nPresiona Enter para continuar...")

def main():
    """Función principal"""
    while True:
        show_main_menu()
        
        try:
            categories = list(TOOLS_BY_CATEGORY.keys())
            choice = input("Selecciona una opción: ").strip()
            
            if not choice.isdigit():
                continue
            
            choice = int(choice)
            
            if choice == len(categories) + 1:
                print("\n¡Hasta luego!")
                break
            
            if 1 <= choice <= len(categories):
                category = categories[choice - 1]
                
                while True:
                    show_category_menu(category)
                    
                    try:
                        tool_choice = input("Selecciona una herramienta: ").strip()
                        
                        if not tool_choice.isdigit():
                            continue
                        
                        tool_choice = int(tool_choice)
                        tools = TOOLS_BY_CATEGORY[category]
                        
                        if tool_choice == len(tools) + 1:
                            break
                        
                        if 1 <= tool_choice <= len(tools):
                            _, tool_file = tools[tool_choice - 1]
                            run_tool(category, tool_file)
                    
                    except (ValueError, IndexError):
                        pass
        
        except (ValueError, IndexError):
            pass

if __name__ == "__main__":
    main()
