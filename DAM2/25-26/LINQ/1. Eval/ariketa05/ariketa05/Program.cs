namespace ariketa05
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // arikera 01
            int[] numeros = { 5, 10, 15, 20 };
            Boolean dauka10 = numeros.Contains(10);
            Console.WriteLine("10 dauka? " + dauka10);



            // ariketa 02
            List<string> titulos = new List<string> { "C#", "LINQ", "ASP.NET" };
            Boolean daukaLINQ = titulos.Contains("LINQ");
            Console.WriteLine("LINQ dauka? " + daukaLINQ);



            // ariketa 03
            Boolean dagoLuis = Persona.personas.Any(p => p.Nombre == "Luis");
            Console.WriteLine("Luis dago? " + dagoLuis);



            // ariketa 04
            List<string> productos = new List<string> { "Teclado", "Monitor", "Ratón" };
            Boolean dagoMonitor = productos.Contains("Ratón");
            Console.WriteLine("Ratón dago? " + dagoMonitor);



            // ariketa 05
            char[] letras = { 'B', 'C', 'A', 'D' };
            Boolean dagoA = productos.Contains("A");
            Console.WriteLine("A dago? " + dagoA);



            // ariketa 06
            Boolean dagoLucia = Estudiante.estudiantes.Any(e => e.Nombre == "Lucía");
            Console.WriteLine("Lucía dago? " + dagoLucia);



            // ariketa 07
            List<int> ids = new List<int> { 1, 2, 3, 4 };
            Boolean dago3 = ids.Contains(3);
            Console.WriteLine("3 dago? " + dago3);



            // ariketa 08
            string[] palabras = { "internet", "red", "conexión", "netflix" };
            Boolean dagoNet = palabras.Any(p => p.Contains("net"));
            Console.WriteLine("net dauka? " + dagoNet);



            // ariketa 09
            List<int> valores = new List<int> { 50, 75, 100, 125 };
            Boolean dago100 = valores.Contains(100);
            Console.WriteLine("100 dago? " + dago100);



            // ariketa 10
            Boolean dagoJorge = Empleado.empleados.Any(e => e.Nombre == "Jorge");
            Console.WriteLine("Jorge dago? " + dagoJorge);

        }
    }
}