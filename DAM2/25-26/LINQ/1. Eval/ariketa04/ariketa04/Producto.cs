using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa04
{
    public class Producto
    {
        public string Nombre;
        public int Stock;

        public static List<Producto> productos = new List<Producto>
{
    new Producto { Nombre = "Teclado", Stock = 10 },
    new Producto { Nombre = "Ratón", Stock = 0 },
    new Producto { Nombre = "Monitor", Stock = 0 }
};

    }
}
