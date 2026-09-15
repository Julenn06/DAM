namespace ariketa00
{
    internal class Programa
    {
        public static async Task Main(string[] args)
        {
            int numero = 12;

            do
            {
                if (numero >= 10)
                    Console.WriteLine("Mayor que 10");
                else
                    Console.WriteLine("Menor que 10");

                do
                {
                    Console.WriteLine("Nuevo numero:");
                    string? input = Console.ReadLine();
                    if (string.IsNullOrWhiteSpace(input) || !int.TryParse(input, out numero))
                    {
                        Console.WriteLine("Por favor, introduce un número válido.");
                        numero = -1;
                    }
                } while (numero == -1);
            } while (numero != 0);


            Console.WriteLine("Fin del programa.");
            await Task.Delay(2000);
            Console.WriteLine("Aun No");
            await Task.Delay(1000);


            var numeros = new[] { 10, 2, 4, 1, 9, 7, 3, 5, 8, 15 };
            Console.WriteLine("Lista de numeros:");
            for (int i = 0; i < numeros.Length; i++)
            {
                Console.WriteLine(numeros[i]);
            }
            await Task.Delay(1000);

            var pares = new List<int>();
            var impares = new List<int>();

            Console.WriteLine("Numeros pares e impares:");
            for (int j = 0; j < numeros.Length; j++)
            {
                if (numeros[j] % 2 == 0)
                {
                    pares.Add(numeros[j]);
                }
                else
                {
                    impares.Add(numeros[j]);
                }
            }

            Console.WriteLine("Numeros pares:");
            foreach (var numeroPar in pares)
            {
                Console.WriteLine(numeroPar);
            }

            Console.WriteLine("Numeros impares:");
            foreach (var numeroImpar in impares)
            {
                Console.WriteLine(numeroImpar);
            }
        }
    }
}