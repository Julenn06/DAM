# Ariketa01 - Base de datos y programa Java

## Descripción

Este proyecto es un ejercicio práctico de acceso a datos en Java, utilizando una base de datos MySQL. Se conecta a una base de datos llamada `ariketa01julenad` y realiza operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en las tablas `DEPARTAMENTOS` y `EMPLEADOS`. El programa demuestra el uso de diferentes tipos de sentencias JDBC: `Statement` para consultas simples, `PreparedStatement` para consultas parametrizadas y `CallableStatement` para llamar a procedimientos almacenados.

## Características

### Consultas
- **Mostrar todos los departamentos**: Lista todos los registros de la tabla `DEPARTAMENTOS`.
- **Mostrar empleados del departamento 10**: Filtra empleados por departamento.
- **Mostrar el empleado con el salario más alto**: Usa una subconsulta para encontrar el máximo.

### Operaciones
- **Añadir departamento MARKETING**: Inserta un nuevo departamento y empleados asociados, verificando duplicados.
- **Actualizar salarios**: Cambia los salarios de empleados en el departamento MARKETING a 1700€.
- **Eliminar departamento y empleados**: Borra empleados y departamento en orden para evitar errores de claves foráneas.

### Tecnologías
- **Java 21**: Lenguaje de programación.
- **MySQL**: Sistema de gestión de bases de datos.
- **XAMPP**: Entorno de desarrollo local para MySQL y phpMyAdmin.
- **Maven**: Herramienta de gestión de proyectos y dependencias.
- **JDBC**: API para conectar Java con bases de datos.

## Requisitos

- **XAMPP** (versión 8.x o superior): Incluye MySQL y phpMyAdmin.
- **Java 21**: Descárgalo de oracle.com o usa OpenJDK.
- **Maven** (versión 3.8+): Para compilar y ejecutar.
- **MySQL Connector/J** (8.0.36): Incluido automáticamente vía Maven.

## Instalación

### Paso 1: Configurar XAMPP
1. Descarga e instala XAMPP desde apachefriends.org.
2. Inicia los módulos Apache y MySQL desde el panel de control de XAMPP.

### Paso 2: Crear la base de datos
1. Abre phpMyAdmin en tu navegador (http://localhost/phpmyadmin).
2. Crea una nueva base de datos llamada `ariketa01julenad` (collation: utf8_general_ci).

### Paso 3: Ejecutar las sentencias SQL
Copia y pega las sentencias del archivo [consultas.txt](consultas.txt) en la pestaña SQL de phpMyAdmin (selecciona la BD `ariketa01julenad`):
- `CREATE TABLE` para crear las tablas.
- `INSERT` para añadir datos de ejemplo.
- `CREATE PROCEDURE` para los procedimientos almacenados.

Ejemplo de ejecución:
```sql
USE ariketa01julenad;
CREATE TABLE DEPARTAMENTOS (
    DEPT_NO INT PRIMARY KEY,
    DNOMBRE VARCHAR(50),
    LOC VARCHAR(50)
);
-- ... resto de sentencias
```

### Paso 4: Configurar el proyecto
1. Coloca los archivos en una carpeta (ej: `C:\Ariketa01`).
2. Abre una terminal y navega a la carpeta.
3. Compila con Maven:
   ```
   mvn clean compile
   ```

## Uso

### Ejecutar el programa
Desde la terminal:
```
mvn exec:java -Dexec.mainClass="main.Main"
```

### Qué hace el programa
1. **Conexión**: Se conecta a `jdbc:mysql://localhost:3307/ariketa01julenad` con usuario `root` (sin contraseña).
2. **Consultas con Statement**: Ejecuta SELECT simples.
3. **Consulta con PreparedStatement**: Usa parámetros para evitar inyección SQL.
4. **Consultas con CallableStatement**: Llama a procedimientos almacenados.
5. **Operaciones**: Añade, actualiza y elimina datos usando procedimientos.

### Ejemplo de salida
```
Conectado a la base de datos.

--- Consultas con Statement ---
1. Departamentos:
10 - CONTABILIDAD - MADRID
20 - INVESTIGACION - BARCELONA
...

2. Empleados del departamento 10:
GARCIA - DIRECTOR - 5000.0
...

3. Empleado con salario más alto:
GARCIA - DIRECTOR - 5000.0

--- Consulta 2 con PreparedStatement ---
Empleados del departamento 10:
GARCIA - DIRECTOR - 5000.0
...

--- Consultas con CallableStatement ---
1. Departamentos:
10 - CONTABILIDAD - MADRID
...

--- Operaciones adicionales ---
Departamento MARKETING y empleados añadidos.
Salarios actualizados a 1700.
Departamento MARKETING y empleados eliminados.
```

### Explicación de las consultas
- **Statement**: Ideal para consultas fijas sin parámetros.
- **PreparedStatement**: Mejora la seguridad y rendimiento para consultas repetidas.
- **CallableStatement**: Para procedimientos almacenados, encapsulando lógica en la BD.

## Estructura de archivos

- [src/main/Main.java](src/main/Main.java): Código fuente del programa.
- [consultas.txt](consultas.txt): Todas las sentencias SQL.
- [pom.xml](pom.xml): Configuración de Maven con dependencias.
- `README.md`: Esta guía.

## Solución de problemas

- **Error de conexión**: Verifica que XAMPP esté ejecutando MySQL en puerto 3307.
- **Driver no encontrado**: Asegúrate de que Maven descargó el conector (`mvn dependency:tree`).
- **Errores SQL**: Comprueba que los procedimientos estén creados en phpMyAdmin.
- **Datos vacíos**: Ejecuta los INSERT de [consultas.txt](consultas.txt).

## Licencia

Proyecto educativo, sin restricciones de uso.