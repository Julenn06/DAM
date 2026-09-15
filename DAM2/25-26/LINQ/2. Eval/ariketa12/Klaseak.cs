namespace ariketa12
{
    public class IkasleaOharrak
    {
        public required string Izena { get; set; }
        public required List<int> Oharrak { get; set; }
    }

    public class IkasleaLanak
    {
        public required string Izena { get; set; }
        public required List<DateTime> EntregaDatak { get; set; }
    }

    internal class Ikaslea
    {
        public required string Izena { get; set; }
        public required List<string> Ikastaroak { get; set; }
    }

    internal class Ikaslea2
    {
        public required string Izena { get; set; }
        public DateTime Jaioteguna { get; set; }
        public required List<double> Oharrak { get; set; }
    }

    internal class Ikaslea3
    {
        public required string Izena { get; set; }
        public required List<bool> Asistentzia { get; set; }
        public required List<double> Oharrak { get; set; }
    }
}
