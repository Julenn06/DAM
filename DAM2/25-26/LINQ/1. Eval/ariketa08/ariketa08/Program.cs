namespace ariketa08
{
    class Program
    {
        static void Main(string[] args)
        {
            //Ariketa 01
            double[] calificaciones = { 5.5, 7.8, 9.0, 6.7, 8.2 };

            var avg = calificaciones.Average();
            Console.WriteLine("Ariketa 01");
            Console.WriteLine($"Batez besteko kalifikazioa: {avg}");



            //Ariketa 02
            var adintxikia = (from p in Persona.personas
                            where p.Edad <= 18
                            select p).FirstOrDefault();

            var adinGehiena = (from p in Persona.personas
                                orderby p.Edad descending
                                select p).FirstOrDefault();

            var avg2 = Persona.personas.Average(p => p.Edad);


            Console.WriteLine("\n Ariketa 02");
            Console.WriteLine($"Adin Txikia: {adintxikia?.Nombre} ({adintxikia?.Edad} urte)");
            Console.WriteLine($"Adin Gehiena: {adinGehiena?.Nombre} ({adinGehiena?.Edad} urte)");
            if (avg2 > 18)
            {
                Console.WriteLine("Adin batez bestekoa 18 baino handiagoa da.");
            }
            else
            {
                Console.WriteLine("Adin batez bestekoa 18 baino txikiagoa da.");
            }



            //Ariketa 03
            List<string> palabras = new List<string> { "perro", "gato", "elefante", "zorro" };

            var avg3 = palabras.Average(p => p.Length);

            var ordenar = (from p in palabras
                           orderby p.Length
                           select p).ToList();

            var hitzLaburrena = ordenar[0];

            var hitzLuzeena = ordenar[ordenar.Count - 1];

            var daukaZ = (from p in palabras
                           where p.Contains('z')
                           select p).ToList();

            Console.WriteLine("\n Ariketa 03");
            Console.WriteLine($"Batez besteko hitz luzera: {avg3}");
            Console.WriteLine($"Hitz laburrena: {hitzLaburrena}");
            Console.WriteLine($"Hitz luzeena: {hitzLuzeena}");

            if (daukaZ.Count != 0)
            {
                Console.WriteLine($"Z hizkia duten hitzak: {string.Join(", ", daukaZ)}");
            }
            else
            {
                Console.WriteLine("Ez dago Z hizkia duen hitzik");
            }



            //Ariketa 04
            int[] numeros4 = { 1, 2, 3, 4, 5, 6, 7, 8 };
            var bikoitiak = (from n in numeros4
                             where n % 2 == 0
                             select n).ToList();

            var avg4 = bikoitiak.Average();

            var txikiena = (from n in bikoitiak
                            orderby n
                            select n).FirstOrDefault();

            var nagusiena = (from n in bikoitiak
                            orderby n descending
                            select n).FirstOrDefault();


            Console.WriteLine("\n Ariketa 04");
            Console.WriteLine($"Bikoitiak: {string.Join(", ", bikoitiak)}");
            Console.WriteLine($"Bikoitien batez bestekoa: {avg4}");
            Console.WriteLine($"Zenbaki txikiena: {txikiena}");
            Console.WriteLine($"Zenbaki nagusiena: {nagusiena}");



            //Ariketa 05
            var estudiantesNotaMayorA6 = (from e in Estudiante.estudiantes
                                         where e.Nota > 6
                                         select e).ToList();

            var avgnotamayora6 = estudiantesNotaMayorA6.Average(e => e.Nota);

            Console.WriteLine("\n Ariketa 05");
            Console.WriteLine($"6 baino nota handiagoa duten ikasleak: {string.Join(", ", estudiantesNotaMayorA6.Select(e => e.Nombre))}");
            Console.WriteLine($"Haien batez besteko nota: {avgnotamayora6}");



            //Ariketa 06
            var proyectosFinalizados = (from p in Proyecto.proyectos
                                        where p.Finalizado
                                        select p).ToList();

            var avgDuracion = proyectosFinalizados.Average(p => p.DuracionDias);

            Console.WriteLine("\n Ariketa 06");
            Console.WriteLine($"Proiektu amaituak: {string.Join(", ", proyectosFinalizados.Select(p => p.Nombre))}");
            Console.WriteLine($"Proiektu amaituen batez besteko iraupena: {avgDuracion} egun");



            //Ariketa 07
            double[] ingresos = { 2500, 1800, 3000, 1200, 2700 };

            var avg7 = ingresos.Average();
            var threshold = avg7 * 0.5;

            var gutxiago = (from i in ingresos
                            where i < threshold
                            select i).ToList();

            Console.WriteLine("\n Ariketa 07");
            Console.WriteLine($"Batez besteko diru sarrera: {avg7}");
            Console.WriteLine($"Muga (%50): {threshold}");
            if (gutxiago.Count != 0)
            {
                Console.WriteLine($"Diru sarrera batez bestekoaren %50 baino gutxiago dutenak: {string.Join(", ", gutxiago)}");
            }
            else
            {
                Console.WriteLine("Ez dago batez bestekoaren %50 baino gutxiago duen diru sarrerarik");
            }
        }
    }
}