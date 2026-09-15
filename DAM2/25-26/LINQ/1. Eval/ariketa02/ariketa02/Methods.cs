namespace ariketa02
{
    internal class Methods
    {
        public static bool IsAnyWordUpperCase(IEnumerable<string> words)
        {
            return words.Any(word => !string.IsNullOrEmpty(word) && word.All(char.IsUpper));
        }
        public static bool IsAnyDogThatWeighsMoreThan36(IEnumerable<Pet> pets)
        {
            return pets.Any(p => p.Type == PetType.Dog && p.Weight >= 36);
        }
        public static bool IsAnyStudentNamedMikelOver20(IEnumerable<Student> students)
        {
            return students.Any(s => s.Name == "Mikel" && s.Age > 20);
        }
        public static bool isAnyStudentNamedJorgeWithId5(IEnumerable<Student> students)
        {
            return students.Any(s => s.Id == 5 && s.Name == "Jorge");
        }
    }
}
