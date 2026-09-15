<div align="center">

# 🎓 GUÍA LINQ - RESUMEN PARA EXAMEN

### _La guía definitiva para dominar LINQ en C#_

[![LINQ](https://img.shields.io/badge/LINQ-C%23-blue?style=for-the-badge&logo=csharp)](https://docs.microsoft.com/es-es/dotnet/csharp/programming-guide/concepts/linq/)
[![.NET](https://img.shields.io/badge/.NET-512BD4?style=for-the-badge&logo=dotnet)](https://dotnet.microsoft.com/)

---

</div>

## 📖 Navegación Rápida

<table>
<tr>
<td width="33%" align="center">
<b>🔧 Operadores</b><br>
<a href="#-operadores-linq">Ver Operadores</a>
</td>
<td width="33%" align="center">
<b>💡 Lambda</b><br>
<a href="#-expresiones-lambda">Ver Lambda</a>
</td>
<td width="33%" align="center">
<b>📐 Sintaxis</b><br>
<a href="#-sintaxis-linq">Ver Sintaxis</a>
</td>
</tr>
<tr>
<td width="33%" align="center">
<b>📚 Ejemplos</b><br>
<a href="#-ejemplos-por-proyecto">Ver Ejemplos</a>
</td>
<td width="33%" align="center">
<b>📊 Tabla</b><br>
<a href="#-tabla-resumen">Ver Tabla</a>
</td>
<td width="33%" align="center">
<b>⚡ Tips</b><br>
<a href="#-tips-para-el-examen">Ver Tips</a>
</td>
</tr>
</table>

---


<div align="center">

## 🔧 OPERADORES LINQ

_Los operadores más importantes que debes conocer_

</div>

---

<details open>
<summary><h3>🔍 Any() - ¿Existe al menos uno?</h3></summary>

> **¿Qué hace?** Verifica si existe **al menos un elemento** que cumple una condición → `bool`

**📝 Sintaxis:**
```csharp
coleccion.Any(x => condicion)
```

**✅ Cuándo usarlo:**
- Validaciones rápidas
- Mejor rendimiento que `Count() > 0`
- Para verificar existencia sin contar todos

**💻 Ejemplos prácticos:**
```csharp
numeros.Any(n => n > 100)                                    // ¿Hay alguno mayor que 100?
pets.Any(p => p.Type == PetType.Dog && p.Weight >= 36)      // ¿Hay perros pesados?
palabras.Any(p => p.Contains("net"))                         // ¿Alguna contiene "net"?
```

</details>

---

<details open>
<summary><h3>✔️ All() - ¿Todos cumplen?</h3></summary>

> **¿Qué hace?** Verifica si **TODOS** los elementos cumplen una condición → `bool`

**📝 Sintaxis:**
```csharp
coleccion.All(x => condicion)
```

**✅ Cuándo usarlo:**
- Validaciones estrictas
- Verificar reglas de negocio
- Asegurar consistencia de datos

**💻 Ejemplos prácticos:**
```csharp
palabra.All(char.IsUpper)           // ¿Todas las letras son mayúsculas?
numeros.All(n => n % 2 == 0)        // ¿Todos son pares?
numeros.All(n => n > 0)             // ¿Todos son positivos?
```

<blockquote>
<b>💡 Diferencia clave:</b> <code>Any()</code> = al menos uno | <code>All()</code> = todos sin excepción
</blockquote>

</details>

---

<details open>
<summary><h3>🔽 Where() - Filtrar elementos</h3></summary>

> **¿Qué hace?** Filtra elementos según una condición → `IEnumerable<T>`

**📝 Sintaxis:**
```csharp
coleccion.Where(x => condicion)
```

**✅ Cuándo usarlo:**
- Obtener múltiples elementos filtrados
- Encadenar con otros operadores
- Base para consultas complejas

**💻 Ejemplos prácticos:**
```csharp
numeros.Where(n => n % 2 == 0)                    // Solo números pares
personas.Where(p => p.Edad >= 18)                 // Mayores de edad
productos.Where(p => p.Stock == 0)                // Sin stock
estudiantes.Where(e => e.Nota > 6).ToList()       // Aprobados
```

</details>

---

<details open>
<summary><h3>🔢 Count() - Contar elementos</h3></summary>

> **¿Qué hace?** Cuenta elementos (con/sin condición) → `int`

**📝 Sintaxis:**
```csharp
coleccion.Count(x => condicion)    // Con condición
coleccion.Count()                   // Sin condición
```

**✅ Cuándo usarlo:**
- Estadísticas y reportes
- Métricas de negocio
- Validar cantidades

**💻 Ejemplos prácticos:**
```csharp
numeros.Count(n => n % 2 == 0)                      // Contar pares
libros.Count(l => l.Paginas > 300)                  // Libros largos
personas.Count(p => p.Edad < 18)                    // Menores de edad
palabras.Count(p => "aeiou".Contains(p[0]))         // Empiezan con vocal
```

</details>

---

<details open>
<summary><h3>🎯 Contains() - Buscar valor exacto</h3></summary>

> **¿Qué hace?** Busca un **valor exacto** en la colección → `bool`

**📝 Sintaxis:**
```csharp
coleccion.Contains(valor)
```

**✅ Cuándo usarlo:**
- Buscar valores simples (int, string, char)
- Igualdad exacta
- Más simple que `Any()` para valores directos

**💻 Ejemplos prácticos:**
```csharp
numeros.Contains(10)          // ¿Existe el número 10?
titulos.Contains("LINQ")      // ¿Existe el título "LINQ"?
letras.Contains('A')          // ¿Existe la letra 'A'?
```

<blockquote>
<b>💡 Diferencia:</b> <code>Contains(valor)</code> para valores directos | <code>Any(x => condicion)</code> para condiciones complejas
</blockquote>

</details>

---

<details open>
<summary><h3>🎯 FirstOrDefault() - Primer elemento</h3></summary>

> **¿Qué hace?** Devuelve el primer elemento o `null` si no existe

**📝 Sintaxis:**
```csharp
coleccion.FirstOrDefault(x => condicion)
```

**✅ Cuándo usarlo:**
- Buscar un elemento específico
- Evitar excepciones (vs `First()`)
- Obtener el primero que cumple condición

**💻 Ejemplos prácticos:**
```csharp
pets.FirstOrDefault(p => p.Id == 5)           // Primera mascota con ID 5
numeros.FirstOrDefault(n => n > 100)          // Primer número mayor que 100
personas.FirstOrDefault(p => p.Edad < 18)     // Primera persona menor de edad
```

<blockquote>
⚠️ <b>Importante:</b> Siempre verificar null → <code>mascota?.Name</code> o <code>if (mascota != null)</code>
</blockquote>

</details>

---

<details open>
<summary><h3>📊 OrderBy() / OrderByDescending() - Ordenar</h3></summary>

> **¿Qué hace?** Ordena elementos ascendente ⬆️ o descendente ⬇️ → `IOrderedEnumerable<T>`

**📝 Sintaxis:**
```csharp
coleccion.OrderBy(x => x)              // Ascendente
coleccion.OrderByDescending(x => x)    // Descendente
```

**✅ Cuándo usarlo:**
- Mostrar datos ordenados
- Antes de obtener primero/último
- Rankings y clasificaciones

**💻 Ejemplos prácticos:**
```csharp
numeros.OrderBy(n => n)                       // Menor a mayor
numeros.OrderByDescending(n => n)             // Mayor a menor
personas.OrderBy(p => p.Edad)                 // Por edad
productos.OrderByDescending(p => p.Precio)    // Por precio (caro a barato)
palabras.OrderBy(p => p.Length)               // Por longitud

// Ordenación múltiple
personas.OrderBy(p => p.Edad).ThenBy(p => p.Nombre)
```

</details>

---

<details open>
<summary><h3>📈 Max() / Min() - Valores extremos</h3></summary>

> **¿Qué hace?** Devuelve el valor **máximo** o **mínimo** de una colección

**📝 Sintaxis:**
```csharp
coleccion.Max()                   // Máximo directo
coleccion.Max(x => x.Propiedad)  // Máximo de propiedad

coleccion.Min()                   // Mínimo directo
coleccion.Min(x => x.Propiedad)  // Mínimo de propiedad
```

**✅ Cuándo usarlo:**
- Encontrar valores extremos
- Estadísticas y análisis
- Comparaciones y validaciones

**💻 Ejemplos prácticos:**
```csharp
numeros.Max()                    // El número más grande
numeros.Min()                    // El número más pequeño
personas.Max(p => p.Edad)        // La edad máxima
productos.Min(p => p.Precio)     // El precio más barato
```

<blockquote>
💡 En strings compara <b>alfabéticamente</b>, no por longitud
</blockquote>

</details>

---

<details open>
<summary><h3>📊 Average() - Calcular promedio</h3></summary>

> **¿Qué hace?** Calcula el promedio (media aritmética) → `double`

**📝 Sintaxis:**
```csharp
coleccion.Average()                   // Promedio directo
coleccion.Average(x => x.Propiedad)  // Promedio de propiedad
```

**✅ Cuándo usarlo:**
- Medias y estadísticas
- Comparar valores contra promedio
- Análisis de datos

**💻 Ejemplos prácticos:**
```csharp
calificaciones.Average()                       // Promedio de notas
personas.Average(p => p.Edad)                  // Edad media
palabras.Average(p => p.Length)                // Longitud media

// Comparar con promedio
var promedio = numeros.Average();
var mayores = numeros.Where(n => n > promedio);
```

</details>

---

<details open>
<summary><h3>➕ Sum() - Sumar valores</h3></summary>

> **¿Qué hace?** Suma todos los valores de una colección

**📝 Sintaxis:**
```csharp
coleccion.Sum()                   // Suma todos
coleccion.Sum(x => x.Propiedad)  // Suma una propiedad
```

**✅ Cuándo usarlo:**
- Calcular totales
- Acumulaciones
- Agregaciones financieras

**💻 Ejemplos prácticos:**
```csharp
numeros.Sum()                                     // Suma total
numeros.Where(n => n % 2 == 0).Sum()              // Suma de pares
productos.Sum(p => p.Precio)                      // Total de precios
empleados.Where(e => e.Edad > 30).Sum(e => e.Salario)  // Salarios filtrados
```

</details>

---

<details open>
<summary><h3>🔄 Select() - Transformar elementos</h3></summary>

> **¿Qué hace?** Proyecta/transforma elementos → `IEnumerable<T>`

**📝 Sintaxis:**
```csharp
coleccion.Select(x => transformacion)
```

**✅ Cuándo usarlo:**
- Extraer propiedades específicas
- Transformar datos
- Crear objetos anónimos

**💻 Ejemplos prácticos:**
```csharp
personas.Select(p => p.Nombre)           // Solo nombres
numeros.Select(n => n * 2)               // Duplicar valores
productos.Select(p => p.Precio)          // Solo precios
palabras.Select(p => p.ToUpper())        // A mayúsculas

// Objetos anónimos
personas.Select(p => new { p.Nombre, p.Edad })
```

<blockquote>
<b>💡 Diferencia:</b> <code>Where()</code> filtra (reduce cantidad) | <code>Select()</code> transforma (mantiene cantidad)
</blockquote>

</details>

---

<details open>
<summary><h3>💾 ToList() / ToArray() - Materializar consulta</h3></summary>

> **¿Qué hace?** Materializa la consulta (la ejecuta) → `List<T>` o `T[]`

**📝 Sintaxis:**
```csharp
consulta.ToList()     // Lista
consulta.ToArray()    // Array
```

**✅ Cuándo usarlo:**
- Ejecutar inmediatamente
- Reutilizar resultados
- Evitar múltiples ejecuciones

**💻 Ejemplos prácticos:**
```csharp
numeros.Where(n => n > 5).ToList()               // Ejecuta y guarda
personas.OrderBy(p => p.Nombre).ToArray()        // Ejecuta como array

// Reutilizar sin re-ejecutar
var pares = numeros.Where(n => n % 2 == 0).ToList();
int cantidad = pares.Count();    // No re-ejecuta
int suma = pares.Sum();          // No re-ejecuta
```

</details>


<div align="center">

## 💡 EXPRESIONES LAMBDA

_La sintaxis mágica de LINQ_

</div>

> Las expresiones lambda son funciones anónimas que se usan en casi todos los operadores LINQ

### 📌 Sintaxis básica

```csharp
parametro => expresion
```

### 🎨 Tipos de expresiones lambda

<table>
<tr>
<th width="50%">Tipo</th>
<th width="50%">Ejemplo</th>
</tr>
<tr>
<td><b>Condición simple</b></td>
<td><code>x => x > 5</code></td>
</tr>
<tr>
<td><b>Verificar paridad</b></td>
<td><code>n => n % 2 == 0</code></td>
</tr>
<tr>
<td><b>Acceso a propiedad</b></td>
<td><code>p => p.Nombre</code></td>
</tr>
<tr>
<td><b>Múltiples condiciones</b></td>
<td><code>p => p.Edad >= 18 && p.Activo</code></td>
</tr>
<tr>
<td><b>Con métodos</b></td>
<td><code>s => s.Contains("a")</code></td>
</tr>
<tr>
<td><b>Transformación</b></td>
<td><code>n => n * 2</code></td>
</tr>
</table>

### 💻 Ejemplos completos

```csharp
// Filtrar
numeros.Where(n => n > 10)

// Buscar
personas.Any(p => p.Nombre == "Juan")

// Transformar
numeros.Select(n => n * 2)

// Ordenar
productos.OrderBy(p => p.Precio)

// Agregar
personas.Sum(p => p.Edad)
```

---


<div align="center">

## 📐 SINTAXIS LINQ

_Dos formas de escribir lo mismo_

</div>

<table>
<tr>
<td width="50%" align="center">

### 🔹 Sintaxis de Métodos
_Más común y directa_

```csharp
coleccion
  .Where(x => x > 5)
  .OrderBy(x => x)
  .ToList();
```

</td>
<td width="50%" align="center">

### 🔸 Sintaxis de Consulta
_Parecida a SQL_

```csharp
(from x in coleccion
 where x > 5
 orderby x
 select x).ToList();
```

</td>
</tr>
</table>

### 🎯 Comparación rápida

| Operación | Sintaxis Métodos | Sintaxis Consulta |
|-----------|------------------|-------------------|
| Filtrar | `.Where(x => x > 5)` | `where x > 5` |
| Ordenar ↑ | `.OrderBy(x => x)` | `orderby x` |
| Ordenar ↓ | `.OrderByDescending(x => x)` | `orderby x descending` |
| Transformar | `.Select(x => x * 2)` | `select x * 2` |

> **💡 Importante:** Los siguientes operadores **solo funcionan en sintaxis de métodos**:
> 
> `Count()` • `Sum()` • `Average()` • `Max()` • `Min()` • `Any()` • `All()` • `Contains()` • `FirstOrDefault()` • `ToList()` • `ToArray()`

---


<div align="center">

## 🗂️ EJEMPLOS POR PROYECTO

_Referencia rápida de qué operadores se usan en cada proyecto_

</div>

<details open>
<summary><b>📁 ARIKETA01 - FirstOrDefault()</b></summary>

```csharp
pets.FirstOrDefault(p => p.Id == 5)
```
_Buscar mascota por ID específico_

</details>

<details>
<summary><b>📁 ARIKETA02 - Any()</b></summary>

```csharp
pets.Any(p => p.Type == PetType.Dog && p.Weight >= 36)
```
_Verificar si existe perro con peso >= 36_

</details>

<details>
<summary><b>📁 ARIKETA04 - Count()</b></summary>

```csharp
numeros.Count(n => n % 2 == 0)      // Contar pares
libros.Count(l => l.Paginas > 300)  // Libros largos
```
_Contar elementos con condición_

</details>

<details>
<summary><b>📁 ARIKETA05 - Contains()</b></summary>

```csharp
numeros.Contains(10)                    // ¿Está el 10?
personas.Any(p => p.Nombre == "Luis")   // ¿Existe Luis?
```
_Buscar valores exactos_

</details>

<details>
<summary><b>📁 ARIKETA06 - OrderBy()</b></summary>

```csharp
numeros.OrderBy(n => n)                      // Ascendente
productos.OrderByDescending(p => p.Precio)   // Descendente
```
_Ordenar colecciones_

</details>

<details>
<summary><b>📁 ARIKETA07 - Max()/Min()</b></summary>

```csharp
numeros.Max()                    // Número más grande
personas.Max(p => p.Edad)        // Edad máxima
productos.Min(p => p.Precio)     // Precio más bajo
```
_Encontrar valores extremos_

</details>

<details>
<summary><b>📁 ARIKETA08 - Average()</b></summary>

```csharp
calificaciones.Average()         // Promedio de notas
personas.Average(p => p.Edad)    // Edad promedio
```
_Calcular promedios_

</details>

<details>
<summary><b>📁 ARIKETA09 - Sum()</b></summary>

```csharp
numeros.Sum()                              // Suma total
productos.Sum(p => p.Precio)               // Total precios
numeros.Where(n => n % 2 == 0).Sum()       // Suma de pares
```
_Sumar valores_

</details>

---

## 🎯 TABLA RESUMEN

| Operador | Retorna | Uso | Ejemplo |
|----------|---------|-----|---------|
| **Any()** | `bool` | ¿Existe al menos uno? | `numeros.Any(n => n > 100)` |
| **All()** | `bool` | ¿Todos cumplen? | `numeros.All(n => n > 0)` |
| **Where()** | `IEnumerable<T>` | Filtrar | `numeros.Where(n => n % 2 == 0)` |
| **Count()** | `int` | Contar | `numeros.Count(n => n > 5)` |
| **Contains()** | `bool` | Buscar valor exacto | `numeros.Contains(10)` |
| **FirstOrDefault()** | `T?` | Primer elemento o null | `lista.FirstOrDefault(x => x > 5)` |
| **OrderBy()** | `IOrderedEnumerable<T>` | Ordenar ↑ | `numeros.OrderBy(n => n)` |
| **OrderByDescending()** | `IOrderedEnumerable<T>` | Ordenar ↓ | `numeros.OrderByDescending(n => n)` |
| **Max()** | Tipo elemento | Valor máximo | `numeros.Max()` |
| **Min()** | Tipo elemento | Valor mínimo | `numeros.Min()` |
| **Average()** | `double` | Promedio | `numeros.Average()` |
| **Sum()** | Tipo numérico | Suma total | `numeros.Sum()` |
| **Select()** | `IEnumerable<T>` | Transformar | `personas.Select(p => p.Nombre)` |
| **ToList()** | `List<T>` | Materializar | `query.ToList()` |

---


<div align="center">

## ⚡ TIPS IMPORTANTES

</div>

<table>
<tr>
<td width="50%">

### 🔄 Diferencias clave

<details>
<summary><b>Any() vs All()</b></summary>

- **Any()** → ¿Al menos UNO?
- **All()** → ¿TODOS?

```csharp
numeros.Any(n => n > 100)  // ¿Alguno > 100?
numeros.All(n => n > 0)    // ¿Todos > 0?
```
</details>

<details>
<summary><b>Any() vs Contains()</b></summary>

- **Any()** → Con condición compleja
- **Contains()** → Valor exacto simple

```csharp
lista.Any(x => x.Edad > 18)  // Condición
lista.Contains(5)             // Valor exacto
```
</details>

<details>
<summary><b>Where() vs Count()</b></summary>

- **Where()** → Filtrar y usar después
- **Count()** → Solo necesitas cantidad

```csharp
var filtrados = lista.Where(x => x > 5)  // IEnumerable
var cantidad = lista.Count(x => x > 5)    // int
```
</details>

</td>
<td width="50%">

### 💡 Consejos prácticos

<blockquote>
<b>🎯 Encadenar operadores</b>
<br><br>
<code>
personas<br>
  .Where(p => p.Edad >= 18)<br>
  .OrderBy(p => p.Nombre)<br>
  .Select(p => p.Email)<br>
  .ToList();
</code>
</blockquote>

<blockquote>
<b>⚠️ Null Safety</b>
<br><br>
Usa <code>FirstOrDefault()</code> cuando el elemento puede no existir:
<br><br>
<code>
var resultado = lista.FirstOrDefault(x => x.Id == 999);
<br>
if (resultado != null) { ... }
</code>
</blockquote>

<blockquote>
<b>🚀 Performance</b>
<br><br>
<code>Any()</code> es más rápido que <code>Count() > 0</code>
<br><br>
<code>
// ✅ Bueno<br>
if (lista.Any()) { ... }
<br><br>
// ❌ Malo<br>
if (lista.Count() > 0) { ... }
</code>
</blockquote>

</td>
</tr>
</table>
