namespace ariketa05
{
    public class Estudiante
    {
        public string Nombre;
        public double Nota;

        public static List<Estudiante> estudiantes = new List<Estudiante>
            {
                new Estudiante { Nombre = "Carlos", Nota = 7.5 },
                new Estudiante { Nombre = "Lucía", Nota = 9.0 }
            };
    }
}