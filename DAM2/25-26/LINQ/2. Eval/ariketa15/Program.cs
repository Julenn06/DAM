using ariketa15;

Console.WriteLine("=== LINQ GroupBy Ariketak ===\n");

Ariketa_15_01();
Ariketa_15_02();
Ariketa_15_03();
Ariketa_15_04();

Console.ReadLine();

// 15-01 Ariketa: Kurtsoaren arabera taldekatu eta estatistikak
// ============================================================
void Ariketa_15_01()
{
    Console.WriteLine("=== 15-01: Ikasleen Notak ===\n");

    List<Ikaslea> ikasleak =
            [
                new() { Izena = "Ana", Ikasgaia = "Matematikak", Nota = 8.5 },
                new() { Izena = "Luis", Ikasgaia = "Matematikak", Nota = 4.2 },
                new() { Izena = "Marta", Ikasgaia = "Historia", Nota = 6.7 },
                new() { Izena = "Pedro", Ikasgaia = "Historia", Nota = 9.1 },
                new() { Izena = "Lucía", Ikasgaia = "Hizkuntza", Nota = 7.5 },
                new() { Izena = "Carlos", Ikasgaia = "Hizkuntza", Nota = 5.0 }
            ];

    // Ikasgaiaren arabera taldekatu
    var estatistikak = ikasleak
        .GroupBy(static a => a.Ikasgaia)
        .Select(static g => new
        {
            Ikasgaia = g.Key,
            IkasleKopurua = g.Count(),
            NotaMinimaoa = g.Min(static a => a.Nota),
            NotaMaximoa = g.Max(static a => a.Nota),
            NotaBatezbestekoa = g.Average(static a => a.Nota),
            DenakGaindituta = g.All(static a => a.Nota >= 5)
        })
        .ToList();

    // Emaitzak erakutsi
    foreach (var stat in estatistikak)
    {
        Console.WriteLine($"Ikasgaia: {stat.Ikasgaia}");
        Console.WriteLine($"  Ikasle kopurua: {stat.IkasleKopurua}");
        Console.WriteLine($"  Nota minimoa: {stat.NotaMinimaoa:F2}");
        Console.WriteLine($"  Nota maximoa: {stat.NotaMaximoa:F2}");
        Console.WriteLine($"  Batez bestekoa: {stat.NotaBatezbestekoa:F2}");
        Console.WriteLine($"  Denak gaindituta (≥5): {(stat.DenakGaindituta ? "Bai" : "Ez")}");
        Console.WriteLine();
    }
}

// ============================================================
// 15-02 Ariketa: Jaiotze urtearen arabera taldekatu
// ============================================================
void Ariketa_15_02()
{
    Console.WriteLine("=== 15-02: Jaiotze urtearen arabera ===\n");

    List<IkasleaData> ikasleak =
            [
                new() { Izena = "Ana", JaiotzeData = new DateTime(DateTime.Now.Year - 20, 5, 12) },
                new() { Izena = "Luis", JaiotzeData = new DateTime(DateTime.Now.Year - 21, 3, 8) },
                new() { Izena = "Marta", JaiotzeData = new DateTime(DateTime.Now.Year - 20, 11, 2) },
                new() { Izena = "Pedro", JaiotzeData = new DateTime(DateTime.Now.Year - 22, 7, 19) },
                new() { Izena = "Lucía", JaiotzeData = new DateTime(DateTime.Now.Year - 21, 1, 25) }
            ];

    // Jaiotze urtearen arabera taldekatu
    var taldeakUrteenArabera = ikasleak
        .GroupBy(static a => a.JaiotzeData.Year)
        .OrderBy(static g => g.Key)
        .Select(static g => new
        {
            Urtea = g.Key,
            Izenak = g.OrderBy(static a => a.Izena).Select(static a => a.Izena).ToList(),
            Lehenengoa = g.OrderBy(static a => a.Izena).First().Izena,
            Azkena = g.OrderBy(static a => a.Izena).Last().Izena
        })
        .ToList();

    // Emaitzak erakutsi
    foreach (var taldea in taldeakUrteenArabera)
    {
        Console.WriteLine($"Urtea: {taldea.Urtea}");
        Console.WriteLine($"  Izenak (alfabetikoki): {string.Join(", ", taldea.Izenak)}");
        Console.WriteLine($"  Lehenengoa: {taldea.Lehenengoa}");
        Console.WriteLine($"  Azkena: {taldea.Azkena}");
        Console.WriteLine();
    }
}

// ============================================================
// 15-03 Ariketa: Skip, Take eta baldintza logikoak
// ============================================================
void Ariketa_15_03()
{
    Console.WriteLine("=== 15-03: Skip, Take eta baldintza logikoak ===\n");

    List<Nota> notak =
            [
                new() { Ikasgaia = "Matematikak", Balioa = 8 },
                new() { Ikasgaia = "Matematikak", Balioa = 3 },
                new() { Ikasgaia = "Matematikak", Balioa = 6 },
                new() { Ikasgaia = "Historia", Balioa = 4 },
                new() { Ikasgaia = "Historia", Balioa = 7 },
                new() { Ikasgaia = "Historia", Balioa = 2 },
                new() { Ikasgaia = "Hizkuntza", Balioa = 9 },
                new() { Ikasgaia = "Hizkuntza", Balioa = 5 },
                new() { Ikasgaia = "Hizkuntza", Balioa = 4 }
            ];

    // Ikasgaiaren arabera taldekatu
    var taldeak = notak
        .GroupBy(static n => n.Ikasgaia)
        .Select(static g => new
        {
            Ikasgaia = g.Key,
            // Gainditutako lehenengo bi notak (>=5)
            GainditutakoBiLehenak = g.Where(static n => n.Balioa >= 5)
                                    .OrderByDescending(static n => n.Balioa)
                                    .Take(2)
                                    .ToList(),
            // Gaindituta ez duten azkenengo bi notak (<5)
            GainditutaEzDutenBiAzkenak = g.Where(static n => n.Balioa < 5)
                                    .OrderBy(static n => n.Balioa)
                                    .Take(2)
                                    .ToList()
        })
        .ToList();

    // Emaitzak erakutsi
    foreach (var taldea in taldeak)
    {
        Console.WriteLine($"Ikasgaia: {taldea.Ikasgaia}");

        Console.Write("  Gainditutako lehenengo bi notak: ");
        if (taldea.GainditutakoBiLehenak.Count != 0)
        {
            Console.WriteLine(string.Join(", ", taldea.GainditutakoBiLehenak.Select(static n => n.Balioa)));
        }
        else
        {
            Console.WriteLine("Ez dago");
        }

        Console.Write("  Gaindituta ez duten azkenengo bi notak: ");
        if (taldea.GainditutaEzDutenBiAzkenak.Count != 0)
        {
            Console.WriteLine(string.Join(", ", taldea.GainditutaEzDutenBiAzkenak.Select(static n => n.Balioa)));
        }
        else
        {
            Console.WriteLine("Ez dago");
        }

        Console.WriteLine();
    }
}

// ============================================================
// 15-04 Ariketa: Dendaren estatistikak
// ============================================================
void Ariketa_15_04()
{
    Console.WriteLine("=== 15-04: Dendaren Estatistikak ===\n");

    List<Salmenta> salmentak =
            [
                new() { Produktua = "Sagarra", Kategoria = "Frutak", Prezioa = 0.5m, Kantitatea = 10 },
                new() { Produktua = "Platanoa", Kategoria = "Frutak", Prezioa = 0.3m, Kantitatea = 15 },
                new() { Produktua = "Esnea", Kategoria = "Esnekiak", Prezioa = 1.2m, Kantitatea = 5 },
                new() { Produktua = "Gazta", Kategoria = "Esnekiak", Prezioa = 3.5m, Kantitatea = 2 },
                new() { Produktua = "Ogia", Kategoria = "Okindegikoak", Prezioa = 1.0m, Kantitatea = 7 }
            ];

    // Kategoriaren arabera taldekatu
    var estatistikak = salmentak
        .GroupBy(static v => v.Kategoria)
        .Select(static g => new
        {
            Kategoria = g.Key,
            PrezioBaxuena = g.Min(static v => v.Prezioa),
            PrezioAltuena = g.Max(static v => v.Prezioa),
            PrezioBatezbestekoa = g.Average(static v => v.Prezioa),
            StockOsoa = g.Sum(static v => v.Kantitatea),
            ProduktuKopurua = g.Count()
        })
        .ToList();

    // Emaitzak erakutsi
    foreach (var stat in estatistikak)
    {
        Console.WriteLine($"Kategoria: {stat.Kategoria}");
        Console.WriteLine($"  Prezio baxuena: {stat.PrezioBaxuena:C2}");
        Console.WriteLine($"  Prezio altuena: {stat.PrezioAltuena:C2}");
        Console.WriteLine($"  Batez besteko prezioa: {stat.PrezioBatezbestekoa:C2}");
        Console.WriteLine($"  Stock kopuru osoa: {stat.StockOsoa}");
        Console.WriteLine($"  Produkto kopurua: {stat.ProduktuKopurua}");
        Console.WriteLine();
    }
}
namespace ariketa15
{

    // ============================================================
    // KLASEAK
    // ============================================================

    internal class Ikaslea
    {
        public required string Izena { get; set; }
        public required string Ikasgaia { get; set; }
        public double Nota { get; set; }
    }

    internal class IkasleaData
    {
        public required string Izena { get; set; }
        public DateTime JaiotzeData { get; set; }
    }

    internal class Nota
    {
        public required string Ikasgaia { get; set; }
        public int Balioa { get; set; }
    }

    internal class Salmenta
    {
        public required string Produktua { get; set; }
        public required string Kategoria { get; set; }
        public decimal Prezioa { get; set; }
        public int Kantitatea { get; set; }
    }
}