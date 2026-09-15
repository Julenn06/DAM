namespace ariketa08
{
    public class Proyecto
    {
        public string Nombre { get; set; }
        public int DuracionDias { get; set; }
        public bool Finalizado { get; set; }


        public static List<Proyecto> proyectos = new List<Proyecto>
{
    new Proyecto { Nombre = "Alpha", DuracionDias = 120, Finalizado = true },
    new Proyecto { Nombre = "Beta", DuracionDias = 90, Finalizado = false },
    new Proyecto { Nombre = "Gamma", DuracionDias = 150, Finalizado = true }
};

    }
}
