namespace ariketa07
{

    class Program
    {
        static void Main(string[] args)
        {
            //ariketa 01
            Console.WriteLine("Ariketa 01");
            int[] numeros = { 23, 45, 12, 67, 34, 89, 2 };
            var handiena = numeros.Max();
            var txikiena = numeros.Min();
            var altuenak = numeros.Max(n => n > 50);
            Console.WriteLine($"Zenbaki handiena: {handiena}");
            Console.WriteLine($"Zenbaki txikiena: {txikiena}");
            Console.WriteLine($"50 baino altuena den zenbakiak daude?: {altuenak}");



            //ariketa 02
            Console.WriteLine("\nAriketa 02");
            List<string> nombres = new List<string> { "Ana", "José", "María", "Pedro", "Luisa" };
            var izenlaburrak = nombres.Min();
            var izenluzeak = nombres.Max();
            var izenakA = nombres.All(n => n.Contains('a') || n.Contains('A'));
            Console.WriteLine($"Izen laburrena: {izenlaburrak}");
            Console.WriteLine($"Izen luzeena: {izenluzeak}");
            Console.WriteLine($"Izen guztiak daukate 'a' edo 'A'?: {izenakA}");



            //ariketa 03
            Console.WriteLine("\nAriketa 03");
            var gazteena = Persona.personas.Min(p => p.Edad);
            var nagusiena = Persona.personas.Max(p => p.Edad);
            var hamabostetikGora = Persona.personas.Count(p => p.Edad > 30);
            Console.WriteLine($"Gazteena: {gazteena}");
            Console.WriteLine($"Nagusiena: {nagusiena}");
            Console.WriteLine($"30 baino gehiago daudenak: {hamabostetikGora}");



            //ariketa 04
            Console.WriteLine("\nAriketa 04");
            double[] valores = { -3.5, 2.7, 0, 5.8, -1.2, 7.1 };
            var zbkHandiena = valores.Max();
            var zbkTxikiena = valores.Min();
            var negatiboak = valores.Any(v => v < 0);
            Console.WriteLine($"Zenbaki handiena: {zbkHandiena}");
            Console.WriteLine($"Zenbaki txikiena: {zbkTxikiena}");
            Console.WriteLine($"Zenbaki negatiboak daude?: {negatiboak}");



            //ariketa 05
            Console.WriteLine("\nAriketa 05");
            int[] numeros2 = { 10, 5, 15, 3, 20, 7 };
            var zbkHandiena2 = numeros2.Max();
            var zbkTxikiena2 = numeros2.Min();
            var ordenados = numeros2.OrderBy(n => n).ToArray();
            Console.WriteLine($"Zenbaki handiena: {zbkHandiena2}");
            Console.WriteLine($"Zenbaki txikiena: {zbkTxikiena2}");
            Console.WriteLine("Zenbakiak ordenatuta: " + string.Join(", ", ordenados));



            //ariketa 06
            Console.WriteLine("\nAriketa 06");
            var prezioGarestiena = Producto.productos.Max(p => p.Precio);
            var prezioMerkeena = Producto.productos.Min(p => p.Precio);
            var merkeak = Producto.productos.Any(p => p.Precio < 10);
            Console.WriteLine($"Prezio garestiena: {prezioGarestiena}");
            Console.WriteLine($"Prezio merkeena: {prezioMerkeena}");
            Console.WriteLine($"10 baino prezioak daude?: {merkeak}");



            //ariketa 07
            Console.WriteLine("\nAriketa 07");
            int[] numeros3 = { 4, 7, 10, 3, 8, 2 };
            var zbkHandiena3 = numeros3.Max();
            var zbkTxikiena3 = numeros3.Min();
            var bikoitiak = numeros3.Count(n => n % 2 == 0);
            Console.WriteLine($"Zenbaki handiena: {zbkHandiena3}");
            Console.WriteLine($"Zenbaki txikiena: {zbkTxikiena3}");
            Console.WriteLine($"Zenbaki bikoitiak: {bikoitiak}");



            //ariketa 08
            Console.WriteLine("\nAriketa 08");
            List<int> numeros4 = new List<int> { 1, 5, 10, 12, 7 };
            var zbkHandiena4 = numeros4.Max();
            var zbkTxikiena4 = numeros4.Min();
            var positiboak = numeros4.All(n => n > 0);
            Console.WriteLine($"Zenbaki handiena: {zbkHandiena4}");
            Console.WriteLine($"Zenbaki txikiena: {zbkTxikiena4}");
            Console.WriteLine($"Zenbaki guztiak positiboak dira?: {positiboak}");



            //ariketa 09
            Console.WriteLine("\nAriketa 09");
            int[] valores2 = { 5, 8, 12, 5 };
            var minzbk = valores2.Min();
            var guztiakAltukoak = valores2.All(v => v > minzbk);
            Console.WriteLine($"Zenbaki minimoa: {minzbk}");
            Console.WriteLine($"Zenbaki guztiak minimoa baino altuagoak dira?: {guztiakAltukoak}");



            //ariketa 10
            Console.WriteLine("\nAriketa 10");
            List<int> numeros5 = new List<int> { 4, 7, 10, 3, 8 };
            // Bikoitiak bakarrik
            var bikoitiak2 = numeros5.Where(n => n % 2 == 0).ToList();
            var bikoitiTxikiena = bikoitiak2.Min();
            Console.WriteLine("Zenbaki bikoitiak: " + string.Join(", ", bikoitiak2));
            Console.WriteLine($"Zenbaki bikoiti txikiena: {bikoitiTxikiena}");
        }
    }
}