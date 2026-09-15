namespace ariketa02
{
    internal class Student(int id, string name, int age)
    {
        public int Id { get; } = id;
        public string Name { get; } = name;
        public int Age { get; } = age;
    }
}