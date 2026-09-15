namespace ariketa07
{
    public class Persona
    {
        public string Nombre { get; set; }
        public int Edad { get; set; }

        public static List<Persona> personas = new List<Persona>
        {
            new Persona { Nombre = "Carlos", Edad = 25 },
            new Persona { Nombre = "Lucía", Edad = 32 },
            new Persona { Nombre = "Pedro", Edad = 29 },
            new Persona { Nombre = "María", Edad = 45 }
        };
    }
}
