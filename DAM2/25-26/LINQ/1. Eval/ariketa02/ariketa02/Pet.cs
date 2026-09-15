namespace ariketa02
{
    internal class Pet(int id, string name, PetType type, float weight)
    {
        public int Id { get; } = id;
        public string Name { get; } = name;
        public PetType Type { get; } = type;
        public float Weight { get; } = weight;
    }
}