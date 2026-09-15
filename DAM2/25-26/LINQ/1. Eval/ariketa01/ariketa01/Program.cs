<<<<<<< HEAD
﻿namespace ariketa01
{
    // Pet klasea eta PetType enumeratzailea (Ariketa 01-03)
    public enum PetType
    {
        Cat,
        Dog,
        Fish
    }

    public class Pet
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public PetType PetType { get; set; }
        public float Weight { get; set; }

        public Pet(int id, string name, PetType petType, float weight)
        {
            Id = id;
            Name = name;
            PetType = petType;
            Weight = weight;
        }

        public override string ToString()
        {
            return $"Pet(Id: {Id}, Izena: {Name}, Mota: {PetType}, Pisua: {Weight}kg)";
        }
    }

    // Student klasea (Ariketa 01-05)
    public class Student
    {
        public int StudentID { get; set; }
        public string StudentName { get; set; }
        public int Age { get; set; }

        public Student(int studentID, string studentName, int age)
        {
            StudentID = studentID;
            StudentName = studentName;
            Age = age;
        }

        public override string ToString()
        {
            return $"Student(ID: {StudentID}, Izena: {StudentName}, Adina: {Age})";
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("=== ARIKETA 01-01 ===");
            Ariketa01_01();

            Console.WriteLine("\n=== ARIKETA 01-02 ===");
            Ariketa01_02();

            Console.WriteLine("\n=== ARIKETA 01-04 ===");
            Ariketa01_04();

            Console.WriteLine("\n=== ARIKETA 01-05 ===");
            Ariketa01_05();

            Console.ReadKey();
        }

        // ARIKETA 01-01: IsAnyLargerThan100 eta IsAnyEven
        static void Ariketa01_01()
        {
            var numbers = new[] { 1, 4, 3, 99, 256, 2 };

            // LINQ erabili gabe
            bool isAnyLargerThan100 = IsAnyLargerThan100(numbers);
            Console.WriteLine($"100 baino handiagoa den bat badago? (LINQ gabe): {isAnyLargerThan100}");

            bool isAnyEven = IsAnyEven(numbers);
            Console.WriteLine($"Bikoitiren bat badago? (LINQ gabe): {isAnyEven}");

            // LINQ erabilita
            bool isAnyLargerThan100_LINQ = numbers.Any(n => n > 100);
            Console.WriteLine($"100 baino handiagoa den bat badago? (LINQ): {isAnyLargerThan100_LINQ}");

            bool isAnyEven_LINQ = numbers.Any(n => n % 2 == 0);
            Console.WriteLine($"Bikoitiren bat badago? (LINQ): {isAnyEven_LINQ}");
        }

        // LINQ erabili gabe
        static bool IsAnyLargerThan100(int[] numbers)
        {
            foreach (int number in numbers)
            {
                if (number > 100)
                {
                    return true;
                }
            }
            return false;
        }

        static bool IsAnyEven(int[] numbers)
        {
            foreach (int number in numbers)
            {
                if (number % 2 == 0)
                {
                    return true;
                }
            }
            return false;
        }

        // ARIKETA 01-02: Hitzen luzera
        static void Ariketa01_02()
        {
            var words = new[] { "a", "bb", "ccc", "dddd" };

            // LINQ erabili gabe
            bool hasWordLongerThan2_NoLINQ = HasWordLongerThan2(words);
            Console.WriteLine($"2 hizkki baino gehiago duen hitzik badago? (LINQ gabe): {hasWordLongerThan2_NoLINQ}");

            // LINQ erabilita
            bool hasWordLongerThan2_LINQ = words.Any(word => word.Length > 2);
            Console.WriteLine($"2 hizki baino gehiago duen hitzik badago? (LINQ): {hasWordLongerThan2_LINQ}");
        }

        static bool HasWordLongerThan2(string[] words)
        {
            foreach (string word in words)
            {
                if (word.Length > 2)
                {
                    return true;
                }
            }
            return false;
        }

        // ARIKETA 01-04: Pet txakurrak
        static void Ariketa01_04()
        {
            // Kasu ezberdinen probak
            Console.WriteLine("--- Zerrenda hutsa ---");
            var emptyPets = new Pet[0];
            TestPetDogFunction(emptyPets);

            Console.WriteLine("--- Zerrenda elementu batekin (txakurra) ---");
            var oneDogPets = new[] { new Pet(1, "Rex", PetType.Dog, 30f) };
            TestPetDogFunction(oneDogPets);

            Console.WriteLine("--- Zerrenda elementu batekin (katua) ---");
            var oneCatPets = new[] { new Pet(1, "Fluffy", PetType.Cat, 4f) };
            TestPetDogFunction(oneCatPets);

            Console.WriteLine("--- Zerrenda anitzeko elementuekin ---");
            var multiplePets = new[]
            {
                new Pet(1, "Hannibal", PetType.Fish, 1.1f),
                new Pet(2, "Anthony", PetType.Cat, 2f),
                new Pet(3, "Ed", PetType.Cat, 0.7f),
                new Pet(4, "Taiga", PetType.Dog, 35f),
                new Pet(5, "Rex", PetType.Dog, 40f)
            };
            TestPetDogFunction(multiplePets);

            Console.WriteLine("--- Zerrenda txakurrik gabe ---");
            var noDogPets = new[]
            {
                new Pet(1, "Hannibal", PetType.Fish, 1.1f),
                new Pet(2, "Anthony", PetType.Cat, 2f),
                new Pet(3, "Ed", PetType.Cat, 0.7f)
            };
            TestPetDogFunction(noDogPets);
        }

        static void TestPetDogFunction(IEnumerable<Pet> pets)
        {
            bool hasDog_NoLINQ = IsAnyPetDog(pets);
            bool hasDog_LINQ = pets.Any(pet => pet.PetType == PetType.Dog);

            Console.WriteLine($"  Txakurrik badago? (LINQ gabe): {hasDog_NoLINQ}");
            Console.WriteLine($"  Txakurrik badago? (LINQ): {hasDog_LINQ}");
        }

        static bool IsAnyPetDog(IEnumerable<Pet> pets)
        {
            foreach (Pet pet in pets)
            {
                if (pet.PetType == PetType.Dog)
                {
                    return true;
                }
            }
            return false;
        }

        // ARIKETA 01-05: Student adingabekoak
        static void Ariketa01_05()
        {
            Console.WriteLine("--- Zerrenda hutsa ---");
            var emptyStudents = new Student[0];
            TestStudentUnderAgeFunction(emptyStudents);

            Console.WriteLine("--- Zerrenda elementu batekin (adingabekoa) ---");
            var oneUnderageStudent = new[] { new Student(1, "Jon", 16) };
            TestStudentUnderAgeFunction(oneUnderageStudent);

            Console.WriteLine("--- Zerrenda elementu batekin (adinduna) ---");
            var oneAdultStudent = new[] { new Student(1, "Ane", 20) };
            TestStudentUnderAgeFunction(oneAdultStudent);

            Console.WriteLine("--- Zerrenda anitzeko elementuekin ---");
            var multipleStudents = new[]
            {
                new Student(1, "Jon", 16),
                new Student(2, "Ane", 20),
                new Student(3, "Mikel", 19),
                new Student(4, "Leire", 17),
                new Student(5, "Aitor", 22)
            };
            TestStudentUnderAgeFunction(multipleStudents);

            Console.WriteLine("--- Zerrenda adingabeko gabe ---");
            var noUnderageStudents = new[]
            {
                new Student(1, "Ane", 20),
                new Student(2, "Mikel", 19),
                new Student(3, "Aitor", 22)
            };
            TestStudentUnderAgeFunction(noUnderageStudents);
        }

        static void TestStudentUnderAgeFunction(IEnumerable<Student> students)
        {
            bool hasUnderage_NoLINQ = IsAnyStudentUnderAge(students);
            bool hasUnderage_LINQ = students.Any(student => student.Age < 18);

            Console.WriteLine($"  Adingabekorik badago? (LINQ gabe): {hasUnderage_NoLINQ}");
            Console.WriteLine($"  Adingabekorik badago? (LINQ): {hasUnderage_LINQ}");
        }

        static bool IsAnyStudentUnderAge(IEnumerable<Student> students)
        {
            foreach (Student student in students)
            {
                if (student.Age < 18)
                {
                    return true;
                }
            }
            return false;
        }
    }
}

=======
﻿namespace ariketak01
{
    internal class Programa
    {
        static void Main(string[] args)
        {
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

            Pet pet;
            for (int i = 0; i < pets.Length; i++)
            {
                pet = pets[i];
                Console.WriteLine($"{pet.Id}: {pet.Name} ({pet.Type}) - {pet.Weight}kg");
            }

            int id;
            do
            {
                Console.WriteLine("Elige un animal");
                string? input = Console.ReadLine();
                if (string.IsNullOrWhiteSpace(input) || !int.TryParse(input, out id))
                {
                    Console.WriteLine("Introduce un número válido.");
                    id = -1;
                }
            } while (id == -1);

            var selectedPet = pets.FirstOrDefault(p => p.Id == id);
            Console.WriteLine(selectedPet);

            for (int i = 0; i < pets.Length; i++)
            {
                if (id == pets[i].Id)
                {
                    Console.WriteLine($"El animal que has elegido es: {pets[i].Name} ({pets[i].Type}) - {pets[i].Weight}kg");
                }
            }
        }
    }
}
>>>>>>> 0155c7f7a4232964442530fffdcf57e5c5ca799d
