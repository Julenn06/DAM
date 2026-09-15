using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa06
{
    internal class Producto
    {
        public string Nombre { get; set; }
        public double Precio { get; set; }

        public static Producto[] productos = {
            new Producto { Nombre = "Pan", Precio = 1.5 },
            new Producto { Nombre = "Queso", Precio = 12 },
            new Producto { Nombre = "Vino", Precio = 20 }
        };
    }
}