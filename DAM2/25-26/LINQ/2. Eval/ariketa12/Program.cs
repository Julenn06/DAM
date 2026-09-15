using ariketa12;

// Ariketa 01
List<IkasleaOharrak> alumnos =
[
    new() {Izena = "Pedro", Oharrak = [7, 8, 9]},
    new() {Izena = "Luzia", Oharrak = [4, 6, 5]},
    new() {Izena = "Carlos", Oharrak = [10, 9, 8]}
];

Console.WriteLine("Ariketa 01");

// Nota guztien batez bestekoa
var notaGuztiak = alumnos.SelectMany(a => a.Oharrak);
double batezBestekoa = notaGuztiak.Average();
Console.WriteLine($"Batez besteko nota: {batezBestekoa:F2}");

// Gutxieneko nota
int gutxienekoa = notaGuztiak.Min();
Console.WriteLine($"Gutxieneko nota: {gutxienekoa}");

// Gehieneko nota
int gehienekoa = notaGuztiak.Max();
Console.WriteLine($"Gehieneko nota: {gehienekoa}");

// 5 baino txikiagoa duen nota egiaztatu
bool badenBostBainoTxikiagoa = notaGuztiak.Any(n => n < 5);
Console.WriteLine($"5 baino nota txikiagoa dago: {(badenBostBainoTxikiagoa ? "Bai" : "Ez")}");

// Ariketa 02
List<IkasleaLanak> alumnos2 =
[
    new() {Izena = "Sofia", EntregaDatak = [new(DateTime.Now.Year, 3, 10), new(DateTime.Now.Year, 5, 20)]},
    new() {Izena = "Diego", EntregaDatak = [new(DateTime.Now.Year, 2, 15)]},
    new() {Izena = "Laura", EntregaDatak = [new(DateTime.Now.Year, 6, 5), new(DateTime.Now.Year, 7, 1)]}
];

Console.WriteLine("\nAriketa 02");

// Entrega-data guztiak lortu eta ordenatu
List<DateTime> dataGuztiak = [.. alumnos2.SelectMany(a => a.EntregaDatak).OrderBy(d => d)];

// Lehena eta azkena erakutsi
var lehena = dataGuztiak.First();
var azkena = dataGuztiak.Last();
Console.WriteLine($"Lehenengo entrega-data: {lehena:dd/MM/yyyy}");
Console.WriteLine($"Azken entrega-data: {azkena:dd/MM/yyyy}");

// Ikasle guztiek aurten gutxienez lan bat eman duten egiaztatu
int aurtengoUrtea = DateTime.Now.Year;
bool denekauretenLanaBatEman = alumnos2.All(a => a.EntregaDatak.Any(d => d.Year == aurtengoUrtea));
Console.WriteLine($"Ikasle guztiek aurten gutxienez lan bat eman dute: {(denekauretenLanaBatEman ? "Bai" : "Ez")}");

// Ariketa 03
List<Ikaslea> alumnos3 =
[
    new() {Izena = "Ana", Ikastaroak = ["Matematika", "Historia", "Fisika"]},
    new() {Izena = "Luis", Ikastaroak = ["Biologia", "Kimika", "Matematika"]},
    new() {Izena = "Marta", Ikastaroak = ["Historia", "Hizkuntza"]}
];

Console.WriteLine("\nAriketa 03");

// Izenak alfabetikoki ordenatuta
List<string> izenGuztiak = [.. alumnos3.Select(a => a.Izena).OrderBy(i => i)];

// Zenbatu ikastaro guztiak
int ikastaroGuztiak = alumnos3.SelectMany(a => a.Ikastaroak).Count();

Console.WriteLine($"Ikasle guztien izenak: {string.Join(", ", izenGuztiak)}");

Console.WriteLine($"Ikastaro guztien kopurua: {ikastaroGuztiak}");

// Ariketa 04
List<Ikaslea2> alumnos4 =
[
    new() {Izena = "Luzia", Jaioteguna = new DateTime(2004, 3, 15), Oharrak = [9.5, 9.8, 10]},
    new() {Izena = "Carlos", Jaioteguna = new DateTime(2006, 5, 20), Oharrak = [7.5, 8.0, 6.5]},
    new() {Izena = "Marta", Jaioteguna = new DateTime(2003, 11, 2), Oharrak = [9.0, 9.2, 9.1]},
    new() {Izena = "Alonzo", Jaioteguna = new DateTime(2002, 1, 10), Oharrak = [10, 10]}
];

Console.WriteLine("\nAriketa 04");

// Baldintza guztiak betetzen dituen ikasle bakarra
var ikasleBakarra = alumnos4
    .SingleOrDefault(i =>
        i.Oharrak.Average() > 9 &&
        i.Oharrak.All(n => n >= 5) &&
        i.Jaioteguna.Year < 2005 &&
        !i.Izena.ToLower(System.Globalization.CultureInfo.CurrentCulture).Contains('z')
    );

if (ikasleBakarra != null)
{
    Console.WriteLine($"Baldintzak betetzen dituen ikaslea: {ikasleBakarra.Izena}");
}
else
{
    Console.WriteLine("Ez dago baldintza guztiak betetzen dituen ikaslerik.");
}

// 3 irakasgai baino gehiago suspendituta duen ikaslerik badago?
bool hiruBainoGehiagoSuspendituta = alumnos4
    .Any(i => i.Oharrak.Count(n => n < 5) > 3);

Console.WriteLine(hiruBainoGehiagoSuspendituta
    ? "Badago 3 irakasgai baino gehiago gainditu gabe dituen ikasleren bat."
    : "Ez dago 3 irakasgai baino gehiago gainditu gabe dituen ikaslerik.");

// Ikasle kopuru osoa
Console.WriteLine($"Ikasle kopuru osoa: {alumnos4.Count}");

// Batez besteko nota orokorra (2 hamartarrekin)
double batezBestekoOrokorra = alumnos4
    .SelectMany(i => i.Oharrak)
    .Average();

Console.WriteLine($"Batez besteko nota orokorra: {batezBestekoOrokorra:F2}");

// Nota altuena eta baxuena
List<double> notaGuztiak2 = [.. alumnos4.SelectMany(i => i.Oharrak)];

Console.WriteLine($"Notarik altuena: {notaGuztiak2.Max()}");
Console.WriteLine($"Notarik baxuena: {notaGuztiak2.Min()}");

// Notarik altuena duen ikaslearen izena
var ikasleNotaAltuenarekin = alumnos4
    .SelectMany(i => i.Oharrak, (ikaslea, nota) => new { ikaslea.Izena, nota })
    .OrderByDescending(x => x.nota)
    .First();

Console.WriteLine($"Notarik altuena duen ikaslea: {ikasleNotaAltuenarekin.Izena}");



// Ariketa 05
List<Ikaslea3> alumnos5 =
[
    new() {Izena = "Alberto", Asistentzia = [true, true, true], Oharrak = [9.5, 8.0, 7.5]},
    new() {Izena = "Mario", Asistentzia = [true, true, true], Oharrak = [10, 9.5, 9.8]},
    new() {Izena = "Luzia", Asistentzia = [false, false], Oharrak = [6.0, 7.0, 8.0]}
];
