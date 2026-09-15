using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa04
{
    public class Factura
    {
        public int Id; public bool Pagada;
        public static List<Factura> facturas = new List<Factura>
        {
            new Factura { Id = 1, Pagada = true },
            new Factura { Id = 2, Pagada = false },
            new Factura { Id = 3, Pagada = false }
        };
    }
}
