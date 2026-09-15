namespace ariketa08
{
    public class Persona
    {
        public string Nombre { get; set; }
        public int Edad { get; set; }

        public static List<Persona> personas = new List<Persona>
{
    new Persona { Nombre = "Ana", Edad = 20 },
    new Persona { Nombre = "Luis", Edad = 17 },
    new Persona { Nombre = "Marta", Edad = 30 }
};

    }
}