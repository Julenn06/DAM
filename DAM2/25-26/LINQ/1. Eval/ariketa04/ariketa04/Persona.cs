using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ariketa04
{
    public class Persona
    {
        public string Nombre; 
        public int Edad;


        public static List<Persona> personas = new List<Persona>
        {
            new Persona { Nombre = "Ana", Edad = 25 },
            new Persona { Nombre = "Luis", Edad = 16 },
            new Persona { Nombre = "Marta", Edad = 17 }
        };
    }
}