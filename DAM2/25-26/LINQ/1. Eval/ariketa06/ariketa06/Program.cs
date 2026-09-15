namespace ariketa06
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // ariketa 01
            string[] nombres = { "Carlos", "Ana", "Luis", "Bea" };
            var izenakOrdenatuta = from nombre in nombres
                                   orderby nombre
                                   select nombre;
            Console.WriteLine("Ariketa 01:");
            foreach (var nombre in izenakOrdenatuta)
            {
                Console.WriteLine(nombre);
            }



            // ariketa 02
            int[] edades = { 25, 40, 18, 35, 60 };
            var adinOrdenatutak = from edad in edades
                                  orderby edad
                                  select edad;
            Console.WriteLine("Ariketa 02:");
            foreach (var edad in adinOrdenatutak)
            {
                Console.WriteLine(edad);
            }
            var handienak = edades.Count(edad => edad > 30);
            Console.WriteLine($"30 baino gehiagoko adinak: {handienak}");



            // ariketa 03
            Console.WriteLine("Ariketa 03:");
            var ordenatuPrezioekin = from producto in Producto.productos
                                     orderby producto.Precio
                                     select producto;
            foreach (var producto in ordenatuPrezioekin)
            {
                Console.WriteLine($"{producto.Nombre}: {producto.Precio}");
            }
            Boolean guztiak = false;
            var prezioHandiak = Producto.productos.Count(producto => producto.Precio > 10);
            if (prezioHandiak == Producto.productos.Length)
            {
                guztiak = true;
            }
            else
            {
                guztiak = false;
            }
            Console.WriteLine($"10 baino gehiagoko prezioa duten produktuak: {guztiak}");



            // ariketa 04
            string[] ciudades = { "Bilbao", "Madrid", "Barcelona", "Sevilla" };
            var hirienLuzera = from ciudad in ciudades
                               orderby ciudad.Length
                               select ciudad;
            Console.WriteLine("Ariketa 04:");
            foreach (var ciudad in hirienLuzera)
            {
                Console.WriteLine(ciudad);
            }



            // ariketa 05
            int[] numeros = { 2, 4, 6, 8, 10 };
            var ordenatuZenbakiak = from numero in numeros
                                    orderby numero descending
                                    select numero;
            Console.WriteLine("Ariketa 05:");
            foreach (var numero in ordenatuZenbakiak)
            {
                Console.WriteLine(numero);
            }
            Boolean bikoitiak = false;
            var bikoitiKopurua = numeros.Count(numero => numero % 2 == 0);
            if (bikoitiKopurua == numeros.Length)
            {
                bikoitiak = true;
            }
            else
            {
                bikoitiak = false;
            }
            Console.WriteLine($"Zenbaki guztiak bikoitiak dira: {bikoitiak}");



            // ariketa 06
            Console.WriteLine("Ariketa 06:");
            var ordenatuLangileak = from empleado in Empleado.empleados
                                    orderby empleado.Nombre
                                    select empleado;
            foreach (var empleado in ordenatuLangileak)
            {
                Console.WriteLine(empleado.Nombre);
            }
            var luzeraHandiak = Empleado.empleados.Count(empleado => empleado.Nombre.Length >= 5);
            Console.WriteLine($"5 hizki edo gehiagoko izenak: {luzeraHandiak}");



            // ariketa 07
            string[] palabras = { "sol", "luz", "cielo", "estrella" };
            var ordenatualfabetikoki = from palabra in palabras
                                       orderby palabra
                                       select palabra;
            Console.WriteLine("Ariketa 07:");
            foreach (var palabra in ordenatualfabetikoki)
            {
                Console.WriteLine(palabra);
            }
            Boolean zDaukate = false;
            var zKopurua = palabras.Count(palabra => palabra.Contains('z'));
            if (zKopurua > 0)
            {
                zDaukate = true;
            }
            else
            {
                zDaukate = false;
            }
            Console.WriteLine($"Z hizkia duten hitzak daude: {zDaukate}");



            // ariketa 08
            double[] temperaturas = { 15.5, 22.3, 0.0, 18.7 };
            var ordenatuTenperaturak = from temperatura in temperaturas
                                       orderby temperatura
                                       select temperatura;
            Console.WriteLine("Ariketa 08:");
            foreach (var temperatura in ordenatuTenperaturak)
            {
                Console.WriteLine(temperatura);
            }
            Boolean positiboak = false;
            var mayorAcero = temperaturas.Count(temperatura => temperatura > 0);
            if (mayorAcero == temperaturas.Length)
            {
                positiboak = true;
            }
            else
            {
                positiboak = false;
            }
            Console.WriteLine($"Guztiak 0 baino gehiagoko tenperaturak dira: {positiboak}");



            // ariketa 09
            Console.WriteLine("Ariketa 09:");
            var ordenatuLiburuak = from libro in Libro.libros
                                   orderby libro.Titulo
                                   select libro;
            foreach (var libro in ordenatuLiburuak)
            {
                Console.WriteLine(libro.Titulo);
            }
            Boolean cDaukate = false;
            var cKopurua = Libro.libros.Count(libro => libro.Titulo.Contains("C#"));
            if (cKopurua > 0)
            {
                cDaukate = true;
            }
            else
            {
                cDaukate = false;
            }
            Console.WriteLine($"C# duten liburuak daude: {cDaukate}");



            // ariketa 10
            Console.WriteLine("Ariketa 10:");
            var ordenatuIkasleak = from estudiante in Estudiante.estudiantes
                                   orderby estudiante.Nota
                                   select estudiante;
            foreach (var estudiante in ordenatuIkasleak)
            {
                Console.WriteLine($"{estudiante.Nombre}: {estudiante.Nota}");
            }
        }
    }
}