# 📚 Repaso07 - Sistema de Gestión de Biblioteca con XML Avanzado

## 📋 Descripción

Sistema completo de gestión de biblioteca que demuestra el manejo avanzado de XML con **múltiples niveles de anidación**. El proyecto utiliza DOM (Document Object Model) para parsear y manipular un documento XML complejo que contiene información sobre autores, libros (con capítulos y reseñas), socios y sus préstamos.

## 🎯 Características Principales

### 📊 Estructura XML Compleja

- **5 niveles de anidación**: biblioteca → autores → libros → capítulos/reseñas → contenido
- **Múltiples relaciones**: Autores con múltiples libros, libros con múltiples capítulos y reseñas
- **Datos ricos**: 5 autores, 9 libros, múltiples premios, capítulos y reseñas
- **Información detallada**: Biografías, premios, valoraciones, stock, precios

### 🏗️ Arquitectura MVC Perfecta

```
Repaso07/
├── biblioteca.xml          # XML con datos complejos anidados
├── pom.xml                # Configuración Maven
└── src/
    ├── model/             # 8 POJOs (Autor, Libro, Capitulo, etc.)
    ├── controller/        # 5 controladores especializados
    └── view/              # Main con menú interactivo
```

### 🎨 Modelos (8 clases)

1. **Biblioteca** - Información de la biblioteca
2. **Autor** - Escritores con biografía y premios
3. **Premio** - Galardones recibidos por autores
4. **Libro** - Obras con información completa
5. **Capitulo** - Desglose de contenido de libros
6. **Resena** - Opiniones y valoraciones de usuarios
7. **Socio** - Miembros de la biblioteca
8. **Prestamo** - Historial de préstamos

### 🎮 Controladores (5 clases)

#### 1. **XMLController** (Core)

- Carga y parsea el documento XML completo
- Navegación por nodos DOM con múltiples niveles
- Búsqueda eficiente por IDs
- Métodos auxiliares para extraer contenido

#### 2. **AutorController**

- Listar todos los autores
- Ver detalles completos (biografía, premios, obras)
- Buscar por nombre, nacionalidad, género literario
- Listar autores premiados
- Estadísticas de autores

#### 3. **LibroController**

- Catálogo completo de libros
- Ficha detallada (capítulos, reseñas, valoraciones)
- Búsqueda por: título, ISBN, categoría, editorial
- Filtros: año, rango de años, idioma, precio
- Top libros mejor valorados
- Alertas de stock bajo
- Cálculo de inventario

#### 4. **SocioController**

- Gestión de socios
- Historial de préstamos por socio
- Búsqueda por nombre, DNI, tipo
- Listar socios con préstamos activos
- Estadísticas de socios

#### 5. **EstadisticasController**

- Resumen general completo
- Distribución por categorías
- Distribución por nacionalidades
- Autores más prolíficos
- Libros con más reseñas
- Análisis de stock
- Estadísticas por editorial
- Publicaciones por década

## 🔍 Operaciones Avanzadas XML

### Parsing Multinivel

```java
// Navegar 4 niveles: biblioteca → autores → autor → libros → libro
NodeList autoresNodes = document.getElementsByTagName("autor");
for (cada autor) {
    NodeList librosNodes = autorElement.getElementsByTagName("libro");
    for (cada libro) {
        NodeList capitulosNodes = libroElement.getElementsByTagName("capitulo");
        // ... más niveles
    }
}
```

### Manejo de Atributos y Elementos

- **Atributos**: `id`, `numero`
- **Elementos de texto**: nombres, títulos, descripciones
- **Elementos anidados**: premios, capítulos, reseñas
- **Listas dinámicas**: colecciones de objetos relacionados

### Transformaciones

- Conversión XML → Objetos Java (DOM to POJO)
- Preservación de relaciones padre-hijo
- Agregación de datos relacionales

## 📊 Ejemplo de Datos

### Estructura de un Autor

```xml
<autor id="A001">
    <nombre>Gabriel García Márquez</nombre>
    <nacionalidad>Colombiana</nacionalidad>
    <generoLiterario>Realismo mágico</generoLiterario>
    <premios>
        <premio>
            <nombre>Premio Nobel de Literatura</nombre>
            <anio>1982</anio>
        </premio>
    </premios>
    <libros>
        <libro id="L001">
            <titulo>Cien años de soledad</titulo>
            <capitulos>
                <capitulo numero="1">
                    <titulo>El origen de Macondo</titulo>
                    <resumen>...</resumen>
                </capitulo>
            </capitulos>
            <resenas>
                <resena>
                    <usuario>María González</usuario>
                    <puntuacion>5</puntuacion>
                </resena>
            </resenas>
        </libro>
    </libros>
</autor>
```

## 🎯 Menús Interactivos

### Menú Principal

1. 📖 Gestión de Autores (6 opciones)
2. 📚 Gestión de Libros (12 opciones)
3. 👥 Gestión de Socios (6 opciones)
4. 📊 Estadísticas (8 opciones)
5. 🔍 Búsquedas Avanzadas (8 opciones)

**Total: 40 operaciones disponibles**

## 🚀 Compilación y Ejecución

### Compilar

```bash
cd Repaso07
mvn clean compile
```

### Ejecutar

```bash
mvn exec:java
```

## 💡 Conceptos Demostrados

### XML Avanzado

- ✅ **Parsing DOM**: DocumentBuilder, Document, Element
- ✅ **Navegación multinivel**: getElementsByTagName recursivo
- ✅ **Atributos**: getAttribute()
- ✅ **Contenido**: getTextContent() con manejo de nulos
- ✅ **Normalización**: document.normalize()
- ✅ **Transformación**: DOM a objetos Java

### Java Moderno (Java 21)

- ✅ **Stream API**: filter, map, collect, sorted
- ✅ **Lambdas**: expresiones para filtrado
- ✅ **Method references**: comparadores
- ✅ **Collections**: ArrayList, HashMap
- ✅ **String formatting**: printf, format

### Patrones de Diseño

- ✅ **MVC**: Separación perfecta de responsabilidades
- ✅ **Controller Pattern**: Lógica de negocio separada
- ✅ **POJO**: Objetos simples sin lógica
- ✅ **Composition**: Relaciones entre objetos
- ✅ **Aggregation**: Listas de objetos relacionados

### Buenas Prácticas

- ✅ **No static methods**: Diseño orientado a objetos
- ✅ **Encapsulación**: Getters y setters
- ✅ **Separación de concerns**: Un controlador por entidad
- ✅ **Código limpio**: Métodos cortos y descriptivos
- ✅ **Documentación**: JavaDoc en todos los métodos públicos

## 📈 Estadísticas del Proyecto

- **Líneas de código**: ~2,500
- **Clases**: 14 (8 modelos, 5 controladores, 1 vista)
- **Métodos públicos**: 80+
- **Niveles XML**: 5
- **Datos de ejemplo**: 5 autores, 9 libros, 2 socios
- **Operaciones CRUD**: 40

## 🎓 Objetivos de Aprendizaje

Este proyecto demuestra:

1. Parsing completo de XML con DOM
2. Manejo de estructuras jerárquicas complejas
3. Navegación por múltiples niveles de anidación
4. Transformación XML → Objetos Java
5. Búsquedas y filtrados avanzados
6. Agregaciones y estadísticas
7. Arquitectura MVC profesional
8. Código no-static orientado a objetos

## 🔧 Tecnologías

- **Java 21**: Última versión LTS
- **Maven 3.11.0**: Gestión de dependencias
- **DOM API**: javax.xml.parsers.*
- **Transformer API**: javax.xml.transform.*
- **Scanner**: Entrada por consola
- **Stream API**: Procesamiento funcional

## 📝 Notas

- El XML contiene **400+ líneas** de datos estructurados
- Demuestra anidación de hasta **5 niveles**
- Incluye **múltiples tipos de nodos**: elementos, atributos, texto
- Utiliza **APIs estándar de Java** (sin dependencias externas)
- Código **100% orientado a objetos** (sin static excepto main)

---

**Autor**: Sistema de preparación para examen de Acceso a Datos  
**Fecha**: Noviembre 2024  
**Versión**: 1.0
