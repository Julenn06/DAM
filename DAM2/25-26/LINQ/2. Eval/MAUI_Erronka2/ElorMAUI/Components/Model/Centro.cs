using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace ElorMAUI.Components.Model
{

    public class Centro
    {
        public int? CCEN { get; set; }
        public string NOM { get; set; }
        public string NOME { get; set; }
        public string DGENRC { get; set; }
        public string DGENRE { get; set; }
        public string GENR { get; set; }
        public int? MUNI { get; set; }
        public string DMUNIC { get; set; }
        public string DMUNIE { get; set; }
        public string DTERRC { get; set; }
        public string DTERRE { get; set; }
        public int? DEPE { get; set; }
        public string DTITUC { get; set; }
        public string DTITUE { get; set; }
        public string DOMI { get; set; }
        public int? CPOS { get; set; }
        public int? TEL1 { get; set; }
        public int? TFAX { get; set; }
        public string EMAIL { get; set; }
        public string PAGINA { get; set; }
        public double? COOR_X { get; set; }
        public double? COOR_Y { get; set; }
        public double? LATITUD { get; set; }
        public double? LONGITUD { get; set; }
    }



}

