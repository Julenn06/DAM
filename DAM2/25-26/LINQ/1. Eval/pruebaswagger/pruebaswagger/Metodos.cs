namespace pruebaswagger
{
    public class Metodos
    {
        public string GetMensaje()
        {
            return "¡Hola desde la API!";
        }
        public string PostMensaje(string nombre)
        {
            return $"Recibido: {nombre}";
        }
        public int Sumar(int a, int b)
        {
            return a + b;
        }
        public int Restar(int a, int b)
        {
            return a - b;
        }
        public int Multiplicar(int a, int b)
        {
            return a * b;
        }
        public double Dividir(int a, int b)
        {
            if (b == 0) throw new DivideByZeroException("No se puede dividir por cero.");
            return (double)a / b;
        }
        public int Potencia(int a, int b)
        {
            return (int)Math.Pow(a, b);
        }
        public int Factorial(int n)
        {
            if (n < 0) throw new ArgumentException("El número debe ser no negativo.");
            if (n == 0) return 1;
            return n * Factorial(n - 1);
        }
        public bool EsPrimo(int n)
        {
            if (n <= 1) return false;
            for (int i = 2; i <= Math.Sqrt(n); i++)
            {
                if (n % i == 0) return false;
            }
            return true;
        }
        public int Fibonacci(int n)
        {
            if (n < 0) throw new ArgumentException("El número debe ser no negativo.");
            if (n == 0) return 0;
            if (n == 1) return 1;
            return Fibonacci(n - 1) + Fibonacci(n - 2);
        }
        public string InvertirCadena(string s)
        {
            if (s == null) throw new ArgumentNullException(nameof(s));
            char[] arr = s.ToCharArray();
            Array.Reverse(arr);
            return new string(arr);
        }
        public bool EsPalindromo(string s)
        {
            if (s == null) throw new ArgumentNullException(nameof(s));
            s = s.ToLower().Replace(" ", "");
            char[] arr = s.ToCharArray();
            Array.Reverse(arr);
            return s == new string(arr);
        }
        public int ContarVocales(string s)
        {
            if (s == null) throw new ArgumentNullException(nameof(s));
            int count = 0;
            foreach (char c in s.ToLower())
            {
                if ("aeiou".Contains(c)) count++;
            }
            return count;
        }
    }
}
