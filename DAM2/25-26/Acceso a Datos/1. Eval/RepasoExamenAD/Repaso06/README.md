# Repaso06 - Sistema Académico SUPER COMPLETO 🎓

## 📋 Descripción

**EL PROYECTO MÁS COMPLETO** para el examen de Acceso a Datos. Sistema de gestión académica con **TODO** lo que necesitas saber:

- ✅ **5 Modelos** completos con relaciones
- ✅ **6 Controladores** con CRUD completo
- ✅ **70+ operaciones** diferentes
- ✅ **Múltiples filtros** y búsquedas avanzadas
- ✅ **Estadísticas** y reportes
- ✅ **Arquitectura MVC** profesional
- ✅ **Firebase Firestore** con relaciones complejas

---

## 🏗️ Arquitectura del Sistema

```
Repaso06/
├── pom.xml                           # Maven + Firebase Admin SDK 9.2.0
├── serviceAccountKey.json            # Credenciales Firebase
└── src/
    ├── model/                        # 5 Modelos de datos
    │   ├── Alumno.java              # Datos completos alumno (15 campos)
    │   ├── Curso.java               # Información curso (14 campos)
    │   ├── Profesor.java            # Datos profesor (15 campos)
    │   ├── Asignatura.java          # Info asignatura (11 campos)
    │   └── Matricula.java           # Calificaciones (13 campos)
    │
    ├── controller/                   # 6 Controladores
    │   ├── DBConnection.java        # Singleton Firebase
    │   ├── AlumnoController.java    # 20 operaciones
    │   ├── CursoController.java     # 18 operaciones
    │   ├── ProfesorController.java  # 13 operaciones
    │   ├── AsignaturaController.java # 11 operaciones
    │   ├── MatriculaController.java  # 12 operaciones
    │   └── EstadisticasController.java # 5 reportes
    │
    └── view/
        └── Main.java                 # Menú con 7 secciones principales
```

---

## 📊 Modelos de Datos

### 1. Alumno (15 campos)

```java
- id, nombre, apellidos, dni, email, telefono
- fechaNacimiento, direccion, ciudad, codigoPostal
- idCurso (referencia), estado (ACTIVO/BAJA/SUSPENDIDO)
- fechaMatriculacion, notaMedia, creditosAprobados
```

### 2. Curso (14 campos)

```java
- id, codigo, nombre, descripcion, duracionHoras
- nivel (BASICO/MEDIO/SUPERIOR), turno (MAÑANA/TARDE/NOCHE)
- plazasDisponibles, plazasTotales
- fechaInicio, fechaFin, precio
- idCoordinador (referencia), activo
```

### 3. Profesor (15 campos)

```java
- id, nombre, apellidos, dni, email, telefono
- fechaNacimiento, especialidad, departamento
- tipoContrato (FIJO/INTERINO/TEMPORAL)
- fechaContratacion, salario, titulacion
- añosExperiencia, activo
```

### 4. Asignatura (11 campos)

```java
- id, codigo, nombre, descripcion
- creditos, horasSemanales, tipo (OBLIGATORIA/OPTATIVA/PROYECTO)
- idCurso, idProfesor (referencias)
- aula, horario, activa
```

### 5. Matricula (13 campos)

```java
- id, idAlumno, idAsignatura (referencias)
- fechaMatricula, notaParcial1, notaParcial2, notaParcial3
- notaFinal, calificacion (APROBADO/NOTABLE/etc)
- convocatoria, fechaExamen, convalidada, observaciones
```

---

## 🔥 Operaciones Disponibles

### 👨‍🎓 GESTIÓN DE ALUMNOS (15 opciones)

1. Crear nuevo alumno
2. Ver todos los alumnos
3. Buscar por ID
4. Buscar por DNI
5. Buscar por email
6. Buscar por nombre (coincidencia parcial)
7. Actualizar datos
8. Eliminar alumno
9. Filtrar por curso
10. Filtrar por ciudad
11. Filtrar por estado (ACTIVO/BAJA/SUSPENDIDO)
12. Filtrar por nota media mínima
13. Filtrar por créditos aprobados
14. Top N alumnos por nota
15. Alumnos sin curso asignado

### 📚 GESTIÓN DE CURSOS (12 opciones)

1. Crear curso
2. Ver todos
3. Buscar por ID
4. Buscar por código
5. Actualizar
6. Eliminar
7. Filtrar por nivel (BASICO/MEDIO/SUPERIOR)
8. Filtrar por turno (MAÑANA/TARDE/NOCHE)
9. Filtrar activos/inactivos
10. Cursos con plazas disponibles
11. Top N cursos más caros
12. Top N cursos con mayor ocupación

### 👨‍🏫 GESTIÓN DE PROFESORES (10 opciones)

1. Crear profesor
2. Ver todos
3. Buscar por ID
4. Buscar por DNI
5. Actualizar
6. Eliminar
7. Filtrar por departamento
8. Filtrar por especialidad
9. Filtrar por tipo de contrato
10. Filtrar por salario mínimo

### 📖 GESTIÓN DE ASIGNATURAS (7 opciones)

1. Crear asignatura
2. Ver todas
3. Buscar por ID
4. Buscar por código
5. Filtrar por curso
6. Filtrar por profesor
7. Filtrar por tipo (OBLIGATORIA/OPTATIVA)

### 📝 GESTIÓN DE MATRÍCULAS (8 opciones)

1. Crear matrícula
2. Ver todas
3. Actualizar calificaciones
4. Filtrar por alumno
5. Filtrar por asignatura
6. Ver aprobadas
7. Ver suspensas
8. Ver convalidadas

### 📊 ESTADÍSTICAS Y REPORTES (5 opciones)

1. Resumen general (totales de todo)
2. Estadísticas de alumnos (notas, edades, top 5)
3. Estadísticas de cursos (precios, ocupación, top 3)
4. Estadísticas de profesores (salarios, contratos)
5. Estadísticas de matrículas (tasa aprobados, distribución)

### 🔍 BÚSQUEDAS AVANZADAS (6 opciones)

1. Alumnos por rango de edad
2. Cursos por rango de precio
3. Profesores por experiencia mínima
4. Asignaturas por créditos mínimos
5. Matrículas por nota mínima
6. Matrículas por convocatoria

---

## 🚀 Compilar y Ejecutar

### 1. Configurar Firebase

```bash
# 1. Crea proyecto en Firebase Console
# 2. Descarga serviceAccountKey.json
# 3. Colócalo en la raíz de Repaso06/
```

### 2. Compilar

```powershell
cd Repaso06
mvn clean compile
```

### 3. Ejecutar

```powershell
mvn exec:java -Dexec.mainClass="view.Main"
```

---

## 📚 Conceptos Cubiertos para el Examen

### ✅ Firestore (NoSQL)

- Conexión con Firebase Admin SDK
- Operaciones CRUD completas
- Queries con `whereEqualTo()`, `whereGreaterThan()`, etc.
- Ordenación con `orderBy()` y `limit()`
- Relaciones entre colecciones (referencias de documentos)
- Conversión Document ↔ POJO

### ✅ Arquitectura MVC

- **Model**: POJOs con getters/setters y métodos auxiliares
- **View**: Menús interactivos con Scanner
- **Controller**: Lógica de negocio y acceso a datos

### ✅ Operaciones Avanzadas

- Filtros múltiples y combinados
- Búsquedas por coincidencia parcial
- Ordenación y limitación de resultados
- Cálculos y estadísticas
- Agregaciones de datos

### ✅ Java Buenas Prácticas

- Singleton pattern (DBConnection)
- Try-catch para manejo de errores
- Streams y lambdas (Java 8+)
- Formateo de salida con `String.format()`
- Conversión de tipos (Long→int, Double→double)

### ✅ Manejo de Fechas

- `java.util.Date`
- `SimpleDateFormat` con patrón "dd/MM/yyyy"
- Conversión String ↔ Date

---

## 🎯 Ejemplo de Uso

```
🎓 SISTEMA DE GESTIÓN ACADÉMICA - REPASO06 🎓

╔═══════════════════════════════════════════════════════════╗
║                  MENÚ PRINCIPAL                           ║
╚═══════════════════════════════════════════════════════════╝
1. 👨‍🎓 Gestión de Alumnos
2. 📚 Gestión de Cursos
3. 👨‍🏫 Gestión de Profesores
4. 📖 Gestión de Asignaturas
5. 📝 Gestión de Matrículas y Calificaciones
6. 📊 Estadísticas y Reportes
7. 🔍 Búsquedas Avanzadas
0. 🚪 Salir
```

---

## 🏆 Ventajas de este Proyecto

✅ **MÁS COMPLETO** que Repaso05  
✅ **5 Modelos** vs 2 en Repaso05  
✅ **70+ operaciones** vs 30 en Repaso05  
✅ **Relaciones complejas** (Curso→Profesor, Asignatura→Curso→Profesor)  
✅ **Sistema de calificaciones** completo  
✅ **Estadísticas avanzadas** con cálculos  
✅ **Búsquedas por rangos** (edad, precio, nota)  
✅ **Múltiples estados** y tipos enumerados

---

## 📝 Notas para el Examen

### Importante recordar:

1. **Firestore es NoSQL** → No hay JOINs, usamos referencias de IDs
2. **ApiFuture<>** → Necesita `.get()` para obtener resultado
3. **QuerySnapshot** → Iterar con `.getDocuments()`
4. **DocumentSnapshot** → `.getString()`, `.getLong()`, `.getDouble()`, `.getDate()`, `.getBoolean()`
5. **Conversión Long→int**: `longValue.intValue()`
6. **Null checks**: Siempre verificar `!= null` antes de convertir

### Errores comunes a evitar:

❌ Olvidar `.get()` en ApiFuture  
❌ No hacer null checks en conversiones  
❌ No cerrar Scanner  
❌ No inicializar Firebase antes de usar controllers

---

## 💯 Resumen de Complejidad

| Aspecto             | Repaso05   | **Repaso06**      |
| ------------------- | ---------- | ----------------- |
| Modelos             | 2          | **5**             |
| Controladores       | 4          | **6**             |
| Operaciones totales | ~30        | **70+**           |
| Filtros             | 8          | **20+**           |
| Estadísticas        | Básicas    | **Avanzadas**     |
| Relaciones          | Simple (1) | **Complejas (3)** |

---

## ✨ ¡TODO LISTO PARA EL EXAMEN!

Este es el proyecto **MÁS COMPLETO** de todos. Cubre:

- ✅ Firestore completo
- ✅ MVC profesional
- ✅ CRUD extenso
- ✅ Filtros avanzados
- ✅ Relaciones complejas
- ✅ Estadísticas
- ✅ Buenas prácticas
