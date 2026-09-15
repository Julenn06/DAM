using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa08
{
    public class Estudiante
    {
        public string Nombre { get; set; }
        public double Nota { get; set; }

        public static List<Estudiante> estudiantes = new List<Estudiante>
{
    new Estudiante { Nombre = "Juan", Nota = 7.5 },
    new Estudiante { Nombre = "Ana", Nota = 5.0 },
    new Estudiante { Nombre = "Luis", Nota = 8.0 },
    new Estudiante { Nombre = "Marta", Nota = 6.0 }
};

    }
}
