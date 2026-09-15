namespace ariketa04
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // ariketa 01
            int[] numeros = { 2, 3, 4, 5, 6 };
            int pares = numeros.Count(numero => numero % 2 == 0);
            Console.WriteLine($"Hay {pares} números pares en el array.");



            // ariketa 02
            int masde300 = Libro.Libros.Count(libro => libro.Paginas > 300);
            Console.WriteLine($"Hay {masde300} libros con más de 300 páginas.");



            // ariketa 03
            int menores = Persona.personas.Count(persona => persona.Edad < 18);
            Console.WriteLine($"Hay {menores} personas menores de edad.");



            // ariketa 04
            int sinStock = Producto.productos.Count(producto => producto.Stock == 0);
            Console.WriteLine($"Hay {sinStock} productos sin stock.");



            // ariketa 05
            string[] cadenas = { "hola", "mundo", "programación", "LINQ" };
            int masde5 = cadenas.Count(cadena => cadena.Length > 5);
            Console.WriteLine($"Hay {masde5} cadenas con más de 5 caracteres.");



            // ariketa 06
            int notamasde8 = Estudiante.estudiantes.Count(estudiante => estudiante.Nota > 8);
            Console.WriteLine($"Hay {notamasde8} estudiantes con nota mayor a 8.");



            // ariketa 07
            int sinpagar = Factura.facturas.Count(factura => !factura.Pagada);
            Console.WriteLine($"Hay {sinpagar} facturas sin pagar.");



            // ariketa 08
            string[] palabras = { "sol", "estrella", "universo", "agua", "idea" };
            int empiezaconvocal = palabras.Count(palabra => "aeiou".Contains(char.ToLower(palabra[0])));
            Console.WriteLine($"Hay {empiezaconvocal} palabras que empiezan con vocal.");



            // ariketa 09
            int[] listaNumeros = { 45, 102, 88, 150, 200 };
            int mayores100 = listaNumeros.Count(numero => numero > 100);
            Console.WriteLine($"Hay {mayores100} números mayores a 100.");



            // ariketa 10
            int menosde1200 = Empleado.empleados.Count(empleado => empleado.Salario < 1200);
            Console.WriteLine($"Hay {menosde1200} empleados con salario menor a 1200.");
        }
    }
}
