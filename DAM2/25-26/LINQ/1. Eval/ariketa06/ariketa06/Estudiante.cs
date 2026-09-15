using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa06
{
    public class Estudiante
    {
        public string Nombre { get; set; }
        public double Nota { get; set; }

        public static Estudiante[] estudiantes = {
            new Estudiante { Nombre = "Lucía", Nota = 9.2 },
            new Estudiante { Nombre = "Pedro", Nota = 7.5 },
            new Estudiante { Nombre = "María", Nota = 8.6 }
        };
    }
}
