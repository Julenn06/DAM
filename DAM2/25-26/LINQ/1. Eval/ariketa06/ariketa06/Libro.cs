using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa06
{
    internal class Libro
    {
        public string Titulo { get; set; }

        public static Libro[] libros = {
            new Libro { Titulo = "Aprende Java" },
            new Libro { Titulo = "C# Básico" },
            new Libro { Titulo = "Python Avanzado" }
        };
    }
}