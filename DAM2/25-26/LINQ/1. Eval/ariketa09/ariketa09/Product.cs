namespace ariketa09
{
    public class Product
    {
        public required string Name; 
        public double Price; 
        public bool InStock;


        public static List<Product> products = new List<Product>
{
    new Product { Name = "Laptop", Price = 1200, InStock = true },
    new Product { Name = "Mouse", Price = 25, InStock = false },
    new Product { Name = "Keyboard", Price = 45, InStock = true }
};

    }
}
