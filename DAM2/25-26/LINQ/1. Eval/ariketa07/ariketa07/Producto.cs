using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa07
{
    public class Producto
    {
        public string Nombre { get; set; }
        public decimal Precio { get; set; }


        public static List<Producto> productos = new List<Producto>
{
    new Producto { Nombre = "Camiseta", Precio = 15.5m },
    new Producto { Nombre = "Pantalón", Precio = 35m },
    new Producto { Nombre = "Zapatos", Precio = 50m },
    new Producto { Nombre = "Calcetines", Precio = 5m }
};

    }
}
