using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa04
{
    public class Estudiante
    {
        public string Nombre; public double Nota;
        public static List<Estudiante> estudiantes = new List<Estudiante>
{
    new Estudiante { Nombre = "Carlos", Nota = 7.5 },
    new Estudiante { Nombre = "Lucía", Nota = 9.0 },
    new Estudiante { Nombre = "Pedro", Nota = 8.5 }
};

    }
}