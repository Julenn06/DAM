using static ariketa02.Methods;

namespace ariketa02
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("03-01 Buscar si alguna palabra está en mayúsculas");
            var wordsNoUppercase = new string[] {
                "quick","brown","fox"
            };
            Console.WriteLine(IsAnyWordUpperCase(wordsNoUppercase));
            var wordsWithUppercase = new string[] {
                "quick","brown","FOX"
            };
            Console.WriteLine(IsAnyWordUpperCase(wordsWithUppercase));

            var pets = new[] {
                new Pet(1, "Hannibal", PetType.Fish, 1.1f),
                new Pet(2, "Anthony", PetType.Cat, 2f),
                new Pet(3, "Ed", PetType.Cat, 0.7f),
                new Pet(4, "Taiga", PetType.Dog, 35f),
                new Pet(5, "Rex", PetType.Dog, 40f),
                new Pet(6, "Lucky", PetType.Dog, 5f),
                new Pet(7, "Storm", PetType.Cat, 0.9f),
                new Pet(8, "Nyan", PetType.Cat, 2.2f)
            };

            var students = new[] {
                new Student(1, "Ane", 22),
                new Student(2, "Mikel", 21),
                new Student(3, "Jon", 19),
                new Student(4, "Julen", 21),
                new Student(5, "Jorge", 18)
            };

            Console.WriteLine("03-02 Buscar el perro que pese mas de 36");
            bool existePerroPesado = IsAnyDogThatWeighsMoreThan36(pets);
            Console.WriteLine(existePerroPesado);
            if (!existePerroPesado)
            {
                Console.WriteLine("No se encontraron perros que pesen 36 o más.");
            }
            else
            {
                foreach (var dog in pets.Where(p => p.Type == PetType.Dog && p.Weight >= 36))
                {
                    Console.WriteLine($"{dog.Name}, {dog.Weight}");
                }
            }

            Console.WriteLine("03-03.01 Buscar User Mikel Edad +20");
            bool existeMikelMayor20 = IsAnyStudentNamedMikelOver20(students);
            Console.WriteLine(existeMikelMayor20);

            Console.WriteLine("03-03.02 Buscar User Jorge ID 5");
            bool existeJorgeId5 = isAnyStudentNamedJorgeWithId5(students);
            Console.WriteLine(existeJorgeId5);

        }
    }
}