# Repaso de Acceso a Datos

Proyecto-guia de examen en Java 17. Todos los ejemplos trabajan con `Persona`, para que se vea que el formato cambia pero el modelo y la lógica no.

## Ejecutar con Firebase

Desde esta carpeta:

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.13.9-hotspot"
mvn clean compile
mvn exec:java
mvn exec:java -Dexec.args="swing"
```

La aplicación se conecta mediante `DBConnection` y crea `datos-firebase/` con:

| Archivo | Técnica | Qué estudiar |
|---|---|---|
| `personas.txt` | `BufferedReader` / `BufferedWriter` | lectura línea a línea y tabla con `\|` |
| `personas.dat` | `DataInputStream` / `DataOutputStream` | orden exacto y tipos (`int`, `UTF`) |
| `personas-array.json` | Gson | array de objetos |
| `personas-envelope.json` | Gson + `JsonObject` | objeto raíz con campo `datos` |
| `personas.xml` | DOM + Transformer | crear y guardar nodos |

## Menú `switch-case`

Al ejecutar `mvn exec:java` se muestra un menú de consola. Cada opción es un `case` distinto en `app.Main`:

| Caso | Operación |
|---:|---|
| 1 | Listar documentos de `personas` |
| 2 | Buscar un documento por ID |
| 3 | Crear documento en Firebase |
| 4 | Actualizar documento en Firebase |
| 5 | Eliminar documento de Firebase |
| 6 | Firebase -> TXT |
| 7 | Firebase -> DAT |
| 8 | Firebase -> JSON array |
| 9 | Firebase -> JSON envolvente |
| 10 | Firebase -> JSON objeto |
| 11 | Firebase -> XML con elementos |
| 12 | Firebase -> XML con atributos |
| 13 | TXT -> Firebase |
| 14 | DAT -> Firebase |
| 15 | JSON array -> Firebase |
| 16 | JSON envolvente -> Firebase |
| 17 | JSON objeto -> Firebase |
| 18 | XML con elementos -> Firebase |
| 19 | XML con atributos -> Firebase |
| 20 | Crear subdocumento |
| 21 | Listar subdocumentos y sus campos |
| 22 | Buscar subdocumento |
| 23 | Actualizar subdocumento |
| 24 | Eliminar subdocumento |
| 25 | Leer campos de los documentos |
| 26 | Leer IDs de subcolección |
| 27 | Abrir la tabla WindowBuilder/Swing con datos de Firebase |
| 0 | Salir |

La ventana Swing también tiene botones propios:

- `Añadir`: abre un formulario con campos ID, nombre y fecha; `Guardar` crea el documento en Firebase y `Salir` cierra sin guardar.
- `Editar / actualizar`: abre un desplegable con los IDs de Firebase, carga los datos en los campos editables y permite `Guardar` o `Salir`.
- `Eliminar`: borra la persona seleccionada de Firebase y refresca la tabla.
- `TXT`, `JSON`, `XML` y `DAT`: convierten las filas actuales de la tabla a cada formato dentro de `datos-firebase/`.

## Mapa de todos los cruces

El origen y el destino siempre pasan por `FirebasePersonasRepository`:

```text
TXT  -> Firebase
DAT  -> Firebase
JSON -> Firebase
XML  -> Firebase
Firebase -> TXT / DAT / JSON / XML / Swing JTable
```

Todos los cruces utilizan Firebase como almacenamiento. Los ficheros solo son formatos de importación o exportación.

## Firebase / Firestore

1. Crea un proyecto Firebase y habilita Firestore.
2. Descarga la clave de cuenta de servicio como `serviceAccountKey.json` en esta carpeta. No la subas a Git.
La colección es `personas`; cada documento usa el ID `P01`, `P02`, etc. y estos campos:

```text
personas (colección)
  P01 (documento)
    nombre: "Ane Etxeberria"       (campo)
    fechaNacimiento: "2002-04-17"  (campo)
    edad: 24                        (campo derivado)
```

`FirebasePersonasRepository` contiene el CRUD completo: `crear`, `listar`, `buscarPorId`, `actualizar` y `eliminar`. También contiene lecturas de documentos de una colección, campos de cada documento y documentos de una subcolección.

## MVC

- `model`: clases `Persona` y `Libro`.
- `firebase`: conexión y repositorio Firestore.
- `format`: un adaptador por formato.
- `service`: cruces entre formatos y repositorio.
- `app`: menú `switch-case` y llamadas directas a Firebase.
- `view`: salida de consola y `JTable` de Swing.
- `app`: puntos de entrada.

## Preguntas de examen que cubre

- Diferencia entre `FileReader`, `BufferedReader` y `Files`.
- Por qué un `.dat` tipado necesita leer en el mismo orden en que se escribió.
- Diferencia entre `DataInputStream` y otros formatos estructurados.
- Constructor vacío y Gson para convertir POJOs.
- DOM para crear/navegar XML y XPath para consultar nodos/campos.
- Firestore: colección -> documento -> campo; además de `get()`, `getString()` y `document(id)`.
- CRUD de Firestore y separación MVC.
- MVC y cómo llenar un `DefaultTableModel` para una `JTable`.

La clase `Libro` está incluida para repetir exactamente los adaptadores con `isbn`, `titulo`, `autor` y `paginas` si el ejercicio usa libros en vez de personas.