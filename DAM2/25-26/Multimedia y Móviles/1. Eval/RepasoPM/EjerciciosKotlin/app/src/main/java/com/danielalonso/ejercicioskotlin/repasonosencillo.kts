package com.danielalonso.ejercicioskotlin

// ------------------------- BLOQUE 1: Sintaxis y variables -------------------------

/*
1.1 Convierte una cantidad en euros a dólares, libras y yenes.
     - Usa constantes y formatea a dos decimales.
*/
val EUR_TO_USD = 1.09
val EUR_TO_GBP = 0.86
val EUR_TO_JPY = 156.23

fun convertirEuros(cantidadEuros: Double): String {
    val usd = cantidadEuros * EUR_TO_USD
    val gbp = cantidadEuros * EUR_TO_GBP
    val jpy = cantidadEuros * EUR_TO_JPY
    return "€%.2f = $%.2f (USD), £%.2f (GBP), ¥%.2f (JPY)".format(cantidadEuros, usd, gbp, jpy)
}

/*
1.2 Muestra una tabla con columnas (Producto, Precio €, IVA %), calcula el precio final formateado.
*/
data class Producto(val nombre: String, val precioEuros: Double, val ivaPercent: Double)

fun precioFinalConIva(p: Producto): String {
    val final = p.precioEuros * (1 + p.ivaPercent / 100.0)
    return "${p.nombre}: €%.2f + ${p.ivaPercent}% IVA = €%.2f".format(p.precioEuros, final)
}

/*
1.3 Pide una fecha (día, mes, año) y valida si es correcta usando operadores lógicos y prioridad.
     - Implementación como función pura. Ejemplo de uso en main().
*/
fun esFechaValida(dia: Int, mes: Int, anio: Int): Boolean {
    if (anio <= 0) return false
    if (mes !in 1..12) return false
    val diasEnMes = when (mes) {
        1,3,5,7,8,10,12 -> 31
        4,6,9,11 -> 30
        2 -> if ((anio % 400 == 0) || (anio % 4 == 0 && anio % 100 != 0)) 29 else 28
        else -> 0
    }
    return dia in 1..diasEnMes
}

// ---------------------- BLOQUE 2: Condicionales y bucles -------------------------

/*
2.1 Genera 20 números aleatorios (1–100) y muestra los múltiplos de 3 o 5.
*/
fun multiplosDe3o5Aleatorios(): List<Int> {
    val nums = List(20) { (1..100).random() }
    println("Generados: $nums")
    return nums.filter { it % 3 == 0 || it % 5 == 0 }
}

/*
2.2 Calcula el número de cifras de un entero sin convertirlo a String.
*/
fun numeroDeCifras(nOriginal: Int): Int {
    var n = kotlin.math.abs(nOriginal)
    if (n == 0) return 1
    var cifras = 0
    while (n > 0) {
        cifras++
        n /= 10
    }
    return cifras
}

/*
2.3 Simula un login con tres intentos de usuario/contraseña antes de bloquear.
     - Implementado como función que recibe credenciales reales y una lista de intentos,
       para evitar bloqueo al ejecutar en entornos no interactivos.
*/
data class Credenciales(val usuario: String, val password: String)

fun simularLogin(credReal: Credenciales, intentos: List<Credenciales>): Boolean {
    var intentosRestantes = 3
    for (intento in intentos) {
        if (intentosRestantes <= 0) break
        if (intento == credReal) {
            println("Acceso concedido a ${intento.usuario}")
            return true
        } else {
            intentosRestantes--
            println("Credenciales incorrectas. Intentos restantes: $intentosRestantes")
        }
    }
    if (intentosRestantes <= 0) {
        println("Cuenta bloqueada tras 3 intentos fallidos.")
    } else {
        println("No se completaron todos los intentos.")
    }
    return false
}

// -------------------------- BLOQUE 3: Funciones --------------------------

/*
3.1 Función genérica que devuelva el máximo de una lista comparable (Int, Double, String).
*/
fun <T : Comparable<T>> maximoDeLista(lista: List<T>): T? {
    if (lista.isEmpty()) return null
    var max = lista[0]
    for (i in 1 until lista.size) {
        if (lista[i] > max) max = lista[i]
    }
    return max
}

/*
3.2 Función de orden superior que reciba una lista y una lambda filtro.
*/
fun <T> filtrarLista(lista: List<T>, filtro: (T) -> Boolean): List<T> {
    val resultado = mutableListOf<T>()
    for (e in lista) if (filtro(e)) resultado.add(e)
    return resultado
}

/*
3.3 Función tail-recursiva que calcule el n-ésimo Fibonacci.
*/
tailrec fun fibonacciTail(n: Int, a: Long = 0, b: Long = 1): Long {
    require(n >= 0) { "n debe ser >= 0" }
    return when (n) {
        0 -> a
        1 -> b
        else -> fibonacciTail(n - 1, b, a + b)
    }
}

// -------------------- BLOQUE 4: Listas y colecciones --------------------

/*
4.1 Mapa mutable de alumnos (nombre → nota). Permite añadir, modificar y eliminar hasta “salir”.
     - Implementado como función gestionable; ejemplo no interactivo en main().
*/
fun gestionarAlumnosDemo(): MutableMap<String, Double> {
    val mapa = mutableMapOf<String, Double>(
        "Ana" to 8.5,
        "Luis" to 6.0
    )
    // ejemplos de operaciones:
    mapa["Carlos"] = 9.0          // añadir
    mapa["Ana"] = 9.2             // modificar
    mapa.remove("Luis")           // eliminar
    return mapa
}

/*
4.2 Dada una lista de palabras, cuenta su frecuencia con groupingBy y eachCount().
*/
fun contarFrecuencia(palabras: List<String>): Map<String, Int> =
    palabras.groupingBy { it }.eachCount()

/*
4.3 Ordena una lista de números ascendente y descendente sin usar sort.
     - Implementamos una versión simple de insertion sort para listas inmutables.
*/
fun ordenarAscSinSort(lista: List<Int>): List<Int> {
    val res = lista.toMutableList()
    for (i in 1 until res.size) {
        val key = res[i]
        var j = i - 1
        while (j >= 0 && res[j] > key) {
            res[j + 1] = res[j]
            j--
        }
        res[j + 1] = key
    }
    return res.toList()
}

fun ordenarDescSinSort(lista: List<Int>): List<Int> {
    return ordenarAscSinSort(lista).reversed()
}

// ---------------------- BLOQUE 5: Clases y objetos ----------------------

/*
5.1 Clase CuentaBancaria con saldo, titular y operaciones depositar() y retirar().
*/
class CuentaBancaria(val titular: String, initialSaldo: Double = 0.0) {
    var saldo: Double = initialSaldo
        private set

    fun depositar(cantidad: Double) {
        require(cantidad >= 0) { "Cantidad a depositar debe ser >= 0" }
        saldo += cantidad
    }

    fun retirar(cantidad: Double): Boolean {
        require(cantidad >= 0) { "Cantidad a retirar debe ser >= 0" }
        return if (cantidad <= saldo) {
            saldo -= cantidad
            true
        } else {
            false
        }
    }

    override fun toString(): String = "Cuenta(titular='$titular', saldo=€%.2f)".format(saldo)
}

/*
5.2 Clase Biblioteca con lista de Libro (título, autor, prestado) y funciones prestar/devolver/listar.
*/
data class Libro(val titulo: String, val autor: String, var prestado: Boolean = false)

class Biblioteca(private val libros: MutableList<Libro> = mutableListOf()) {
    fun agregar(libro: Libro) = libros.add(libro)
    fun prestar(titulo: String): Boolean {
        val libro = libros.find { it.titulo == titulo && !it.prestado } ?: return false
        libro.prestado = true
        return true
    }
    fun devolver(titulo: String): Boolean {
        val libro = libros.find { it.titulo == titulo && it.prestado } ?: return false
        libro.prestado = false
        return true
    }
    fun listar(): List<Libro> = libros.toList()
}

/*
5.3 Clase Fracción con sobrecarga de operadores +, –, *, / y simplificación del resultado.
*/
class Fraccion(n: Int, d: Int) {
    val num: Int
    val den: Int

    init {
        require(d != 0) { "Denominador no puede ser 0" }
        val sign = if (d < 0) -1 else 1
        val g = gcd(kotlin.math.abs(n), kotlin.math.abs(d))
        num = sign * n / g
        den = kotlin.math.abs(d) / g
    }

    private fun gcd(a: Int, b: Int): Int {
        var x = a
        var y = b
        while (y != 0) {
            val t = x % y
            x = y
            y = t
        }
        return x
    }

    operator fun plus(other: Fraccion): Fraccion =
        Fraccion(this.num * other.den + other.num * this.den, this.den * other.den)

    operator fun minus(other: Fraccion): Fraccion =
        Fraccion(this.num * other.den - other.num * this.den, this.den * other.den)

    operator fun times(other: Fraccion): Fraccion =
        Fraccion(this.num * other.num, this.den * other.den)

    operator fun div(other: Fraccion): Fraccion =
        Fraccion(this.num * other.den, this.den * other.num)

    override fun toString(): String = if (den == 1) "$num" else "$num/$den"
}

// -------------------- BLOQUE 6: Herencia e interfaces --------------------

/*
6.1 Jerarquía Figura → (Círculo, Rectángulo, Triángulo) con método área() polimórfico.
*/
sealed class Figura {
    abstract fun area(): Double
}

class Circulo(val radio: Double) : Figura() {
    override fun area(): Double = Math.PI * radio * radio
}

class Rectangulo(val base: Double, val altura: Double) : Figura() {
    override fun area(): Double = base * altura
}

class Triangulo(val base: Double, val altura: Double) : Figura() {
    override fun area(): Double = base * altura / 2.0
}

/*
6.2 Interfaz Notificable con enviarNotificacion(msg). Implementaciones: Email y SMS.
*/
interface Notificable {
    fun enviarNotificacion(mensaje: String)
}

class Email(val direccion: String) : Notificable {
    override fun enviarNotificacion(mensaje: String) {
        println("Enviando email a $direccion: $mensaje")
    }
}

class SMS(val telefono: String) : Notificable {
    override fun enviarNotificacion(mensaje: String) {
        println("Enviando SMS a $telefono: $mensaje")
    }
}

/*
6.3 Jerarquía Empleado → (Desarrollador, Gerente) con método calcularBonus() distinto.
*/
abstract class Empleado(val nombre: String, val salarioBase: Double) {
    abstract fun calcularBonus(): Double
}

class Desarrollador(nombre: String, salarioBase: Double, val proyectos: Int) :
    Empleado(nombre, salarioBase) {
    override fun calcularBonus(): Double = salarioBase * 0.05 + proyectos * 200
}

class Gerente(nombre: String, salarioBase: Double, val equipos: Int) :
    Empleado(nombre, salarioBase) {
    override fun calcularBonus(): Double = salarioBase * 0.1 + equipos * 500
}

// ----------------------- BLOQUE 7: Null Safety -----------------------

/*
7.1 Función parseIntSeguro(String?) → Int?, devuelve null si no puede convertir.
*/
fun parseIntSeguro(s: String?): Int? = s?.toIntOrNull()

/*
7.2 Lista de Persona?; filtra con let las no nulas y mayores de 18 años.
*/
data class Persona(val nombre: String, val edad: Int)

fun filtrarMayores(personas: List<Persona?>): List<Persona> {
    val resultado = mutableListOf<Persona>()
    for (p in personas) {
        p?.let {
            if (it.edad >= 18) resultado.add(it)
        }
    }
    return resultado
}

/*
7.3 Mapa (String → Int?); suma los valores no nulos usando !! solo donde sea seguro.
     - Ejemplo: comprobamos nulidad antes de usar !! para sumarlos.
*/
fun sumarValoresNoNulos(mapa: Map<String, Int?>): Int {
    var suma = 0
    for ((_, v) in mapa) {
        if (v != null) {
            // aquí es seguro usar !!
            suma += v!!
        }
    }
    return suma
}

// ---------------- BLOQUE 8: Data Classes y objetos object ----------------

/*
8.1 Data class Pedido con id, cliente y total; ordénala por total descendente.
*/
data class Pedido(val id: Int, val cliente: String, val total: Double)

/*
8.2 Objeto GestorPedidos con lista de Pedido y funciones agregar(), buscarPorCliente(), mostrarTotales().
*/
object GestorPedidos {
    private val pedidos = mutableListOf<Pedido>()

    fun agregar(p: Pedido) = pedidos.add(p)

    fun buscarPorCliente(cliente: String): List<Pedido> =
        pedidos.filter { it.cliente.equals(cliente, ignoreCase = true) }

    fun mostrarTotales(): Double = pedidos.sumOf { it.total }

    fun listarOrdenadosPorTotalDesc(): List<Pedido> =
        pedidos.sortedByDescending { it.total } // uso de sortedByDescending aquí para conveniencia
}

/*
8.3 Clase Sesion con companion object que genera identificadores únicos con un contador interno.
*/
class Sesion(val usuario: String) {
    val id: Int = newId()
    companion object {
        private var contador = 0
        private fun newId(): Int {
            contador += 1
            return contador
        }

        // helper público para obtener próximo id sin crear sesión (opcional)
        fun peekNextId(): Int = contador + 1
    }

    override fun toString(): String = "Sesion(id=$id, usuario='$usuario')"
}



    println("---- BLOQUE 1 ----")
    println(convertirEuros(50.0))
    val productos = listOf(
        Producto("Camisa", 25.0, 21.0),
        Producto("Libro", 12.5, 4.0)
    )
    productos.forEach { println(precioFinalConIva(it)) }
    println("Fecha 29/2/2024 válida? ${esFechaValida(29, 2, 2024)}")
    println("Fecha 31/4/2021 válida? ${esFechaValida(31, 4, 2021)}")

    println("\n---- BLOQUE 2 ----")
    val mult = multiplosDe3o5Aleatorios()
    println("Múltiplos de 3 o 5: $mult")
    println("Cifras de -12345 = ${numeroDeCifras(-12345)}")
    val credReal = Credenciales("admin", "s3cr3t")
    val intentos = listOf(
        Credenciales("admin", "123"),
        Credenciales("admin", "nope"),
        Credenciales("admin", "s3cr3t")
    )
    simularLogin(credReal, intentos)

    println("\n---- BLOQUE 3 ----")
    println("Máximo [3,7,2] = ${maximoDeLista(listOf(3,7,2))}")
    println("Filtrar pares de 1..10 = ${filtrarLista((1..10).toList()) { it % 2 == 0 }}")
    println("Fibonacci(10) = ${fibonacciTail(10)}")

    println("\n---- BLOQUE 4 ----")
    println("Gestión alumnos demo: ${gestionarAlumnosDemo()}")
    val palabras = listOf("hola","adios","hola","bien")
    println("Frecuencias: ${contarFrecuencia(palabras)}")
    val nums = listOf(5,2,9,1,7)
    println("Asc: ${ordenarAscSinSort(nums)} Desc: ${ordenarDescSinSort(nums)}")

    println("\n---- BLOQUE 5 ----")
    val cuenta = CuentaBancaria("María", 100.0)
    cuenta.depositar(50.0)
    println(cuenta)
    println("Retirar 30: ${cuenta.retirar(30.0)} -> $cuenta")
    val bib = Biblioteca(mutableListOf(Libro("1984","Orwell")))
    bib.agregar(Libro("El Quijote","Cervantes"))
    println("Lista biblioteca: ${bib.listar()}")
    println("Prestar 1984 -> ${bib.prestar("1984")}; devolver 1984 -> ${bib.devolver("1984")}")
    val f1 = Fraccion(2,4)
    val f2 = Fraccion(3,5)
    println("Fracciones: $f1 + $f2 = ${f1 + f2}")

    println("\n---- BLOQUE 6 ----")
    val figuras: List<Figura> = listOf(Circulo(2.0), Rectangulo(3.0,4.0), Triangulo(3.0,4.0))
    figuras.forEach { println("Área: ${it.area()}") }
    val email = Email("x@dominio.com")
    val sms = SMS("+34123456")
    email.enviarNotificacion("Hola por email")
    sms.enviarNotificacion("Hola por SMS")
    val dev = Desarrollador("Luis", 2000.0, 3)
    val ger = Gerente("Ana", 3500.0, 2)
    println("Bonus Dev: ${dev.calcularBonus()}  Bonus Ger: ${ger.calcularBonus()}")

    println("\n---- BLOQUE 7 ----")
    println("parseIntSeguro('123') = ${parseIntSeguro("123")}  parseIntSeguro('abc') = ${parseIntSeguro("abc")}")
    val listaPersonas: List<Persona?> = listOf(Persona("A",20), null, Persona("B",17))
    println("Mayores de 18: ${filtrarMayores(listaPersonas)}")
    val mapa: Map<String, Int?> = mapOf("a" to 1, "b" to null, "c" to 5)
    println("Suma no nulos: ${sumarValoresNoNulos(mapa)}")

    println("\n---- BLOQUE 8 ----")
    val p1 = Pedido(1, "Juan", 120.0)
    val p2 = Pedido(2, "Ana", 50.0)
    val p3 = Pedido(3, "Juan", 300.0)
    GestorPedidos.agregar(p1); GestorPedidos.agregar(p2); GestorPedidos.agregar(p3)
    println("Pedidos de Juan: ${GestorPedidos.buscarPorCliente("Juan")}")
    println("Total pedidos: ${GestorPedidos.mostrarTotales()}")
    println("Pedidos ordenados por total desc: ${GestorPedidos.listarOrdenadosPorTotalDesc()}")
    val s1 = Sesion("userA"); val s2 = Sesion("userB")
    println("Sesiones: $s1 , $s2 ; próximo id = ${Sesion.peekNextId()}")


