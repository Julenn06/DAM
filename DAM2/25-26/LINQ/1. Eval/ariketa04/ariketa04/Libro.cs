using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa04
{
    public class Libro
    {
        public string Titulo;
        public int Paginas;

        public static List<Libro> Libros { get; } = new List<Libro>
        {
            new Libro { Titulo = "C#", Paginas = 250 },
            new Libro { Titulo = "LINQ", Paginas = 320 }
        };
    }
}