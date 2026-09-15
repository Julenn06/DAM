
namespace Azterketa1EvalLINQ_Julen
{
    class Program
    {
        public class Producto { 
        
            public string Nombre { get; set; }
            public string Categoria { get; set; }
            public decimal Precio { get; set; }
            public int Stock {  get; set; }
        }

        public class Cliente { 
            public string Nombre { get; set; }
            public List<String> Compras {get; set; }
        }

        public static void Main(string[] args)
        {

            var productos = new List<Producto> { 
            
                new Producto { Nombre = "Laptop", Categoria = "Electronica", Precio = 1200, Stock = 5 },
                new Producto { Nombre = "Mouse", Categoria = "Electronica", Precio =25, Stock = 50 },
                new Producto { Nombre = "Teclado", Categoria = "Electronica", Precio = 45, Stock = 30 },
                new Producto { Nombre = "Silla", Categoria = "Muebles", Precio = 150, Stock = 10 },
                new Producto { Nombre = "Escritorio", Categoria = "Muebles", Precio = 300, Stock = 7 },
                new Producto { Nombre = "Cafetera", Categoria = "Electrodomestico", Precio = 80, Stock = 15 }
            };

            var clientes = new List<Cliente> { 
                new Cliente {Nombre ="Ana", Compras = new List<string>{"Laptop","Mouse","Cafe" } },
                new Cliente {Nombre ="Luis", Compras = new List<string>{"Teclado","Monitor","Cafe" } },
                new Cliente {Nombre ="Carla", Compras = new List<string>{"Silla","Escritorio" } },
                new Cliente {Nombre ="Pedro", Compras = new List<string>{"Cafe","Auriculares","Monitor" } }
            };

            //Ariketa 01
            Console.WriteLine("Ariketa 01:");
            Boolean ProdukturikEz = productos.Any(x=> x.Stock == 0);
            Console.WriteLine("Produkturik dago Stock gabe?: " + ProdukturikEz);


            //Ariketa 02
            Console.WriteLine("Ariketa 02:");
            Boolean guztiak20BainoGehiago = productos.All(x=> x.Precio > 20);
            Console.WriteLine("Produktu guztiak 20 Baino gehiago balio dute?: " + guztiak20BainoGehiago);


            //Ariketa 03
            Console.WriteLine("Ariketa 03:");
            decimal inbentarioOsoa = productos.Sum(x=> x.Precio);
            Console.WriteLine("Zenbat balio du inbentario osoa?: " + inbentarioOsoa);


            //Ariketa04
            Console.WriteLine("Ariketa 04:");
            Console.WriteLine("Produktu Gareztiena:" + productos.Max(x => x.Precio));
            Console.WriteLine("Produktu Merkeena:" + productos.Min(x => x.Precio));


            //Ariketa05
            Console.WriteLine("Ariketa 05:");
            int gehiago10 = productos.Count(x=> x.Stock > 10);
            Console.WriteLine("Zenbat Produktu daude 10 Baino gehiago duten Stock?: " + gehiago10);

            //Ariketa 06
            Console.WriteLine("Ariketa 06:");
            Console.WriteLine("Stock Batzabeste: " + productos.Average(x => x.Stock));
            Console.WriteLine("Stock Minimoa: " + productos.Min(x => x.Stock));
            Console.WriteLine("Stock Maximoa: " + productos.Max(x => x.Stock));
            Console.WriteLine("Zenbat Stock Baino Guxtiago: " + productos.Count(x=> x.Stock < productos.Average(x => x.Stock)));
            Console.WriteLine("Zenbat Stock Baino Gehiago: " + productos.Count(x => x.Stock > productos.Average(x => x.Stock)));
            Console.WriteLine("Zenbat Stock-aren berdina: " + productos.Count(x => x.Stock == productos.Average(x => x.Stock)));
            Console.WriteLine("Zenbat Minimoarn-aren berdina: " + productos.Count(x => x.Stock == productos.Min(x => x.Stock)));
            Console.WriteLine("Zenbat Maximoaren-aren berdina: " + productos.Count(x => x.Stock == productos.Max(x => x.Stock)));


            //Ariketa 07
            Console.WriteLine("Ariketa 07:");
            var laptopErosi = clientes.Any(x=> x.Compras.Any(x=> x =="Laptop"));
            Console.WriteLine("Norbaitek erosi du Laptop bat?: " + laptopErosi);


            //Ariketa 08
            Console.WriteLine("Ariketa 08");
            var premiumPack = new List<string> { "Laptop", "Mouse" };
            var producto1 = premiumPack[0];
            var producto2 = premiumPack[1];
            var premiumErosi = clientes.ElementAt(0).Equals(premiumPack);
            Console.WriteLine(premiumErosi);



            //Ariketa 09
            Console.WriteLine("Ariketa 09");
            int zenbat2BainoGehiago = clientes.Count(x => x.Compras.Count() > 2);
            Console.WriteLine("Zenbat pertsona erosi dute 2 produktu baino gehiago?: " + zenbat2BainoGehiago);


            //Ariketa 10
            Console.WriteLine("Ariketa 10");
            var cafeErosi = clientes.Sum(x => x.Compras.Count()) <= clientes.Count(x=> x.Compras.Contains("cafe"));
            Console.WriteLine("50% baino gehiago erosi dute kafea?:" + cafeErosi);
        }
    }
}
