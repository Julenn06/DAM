namespace ariketa09 { 

    internal class Program { 
        static void Main(string[] args) {



            //Ariketa 01
            int[] numbers = { 1, 2, 3, 4, 5, 6 };

            var gehiketa = from n in numbers
                           where n % 2 == 0
                           select n;
            
            var totala = gehiketa.Sum();

            Console.WriteLine("Ariketa 01");
            Console.WriteLine($"Bikoitien Gehiketa: {totala}");



            //Ariketa 02
            var stockean = from p in Product.products
                           where p.InStock == true
                           select p;

            var totala2 = stockean.Sum(p => p.Price);

            Console.WriteLine("\n Ariketa 02");
            Console.WriteLine($"Stockean dauden produktuen prezio totala: {totala2}");



            //Ariketa 03
            List<int> grades = new List<int> { 4, 6, 7, 3, 8 };

                        var gehiketa2 = from g in grades
                           where g > 5
                           select g;

            var totala3 = gehiketa2.Sum();

            Console.WriteLine("\n Ariketa 03");
            Console.WriteLine($"5 baino gehiagoko noten gehiketa: {totala3}");



            //Ariketa 04
            var langileak = from e in Employee.employees
                            where e.Age > 30
                            select e;
            var totala4 = langileak.Sum(e => e.Salary);

            Console.WriteLine("\n Ariketa 04");
            Console.WriteLine($"30 urte baino gehiagoko langileen soldata totala: {totala4}");



            //Ariketa 05
            string[] words = { "mesa", "silla", "ventana", "puerta", "luz" };

            var gehiketa3 = from w in words
                            where w.Contains('a')
                            select w;

            var totala5 = gehiketa3.Sum(w => w.Length);

            Console.WriteLine("\n Ariketa 05");
            Console.WriteLine($"'a' hizkia duten hitzen luzera totala: {totala5}");



            //Ariketa 06
            double[] temps = { 15.5, 18.2, 20.1, 16.8, 22.0 };

            var batezBestekoa = temps.Average();
            var gehiketa4 = from t in temps
                            where t > batezBestekoa
                            select t;

            var totala6 = gehiketa4.Sum();

            Console.WriteLine("\n Ariketa 06");
            Console.WriteLine($"Batez besteko tenperatura baino handiagoak diren tenperaturen batura: {totala6}");
        }
    }
}