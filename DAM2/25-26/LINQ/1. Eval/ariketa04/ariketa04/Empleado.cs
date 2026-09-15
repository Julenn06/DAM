using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa04
{
    public class Empleado
    {
        public string Nombre; public double Salario;

        public static List<Empleado> empleados = new List<Empleado>
{
    new Empleado { Nombre = "Marta", Salario = 1100 },
    new Empleado { Nombre = "Jorge", Salario = 1500 },
    new Empleado { Nombre = "Laura", Salario = 1000 }
};

    }
}
