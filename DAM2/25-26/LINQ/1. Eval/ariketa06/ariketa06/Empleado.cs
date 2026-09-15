using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa06
{
    public class Empleado
    {
        public string Nombre { get; set; }

         public static Empleado[] empleados = {
             new Empleado { Nombre = "Ana" },
             new Empleado { Nombre = "Roberto" },
             new Empleado { Nombre = "Luis" }
        };
    }
}
