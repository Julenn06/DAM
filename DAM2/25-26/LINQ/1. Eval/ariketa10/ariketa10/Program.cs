
// 10-01 Ariketa
var productos = new[]
{
	new { Nombre = "Teclado", Stock = 15, Precio = 25.5, Promocion = true },
	new { Nombre = "Ratón", Stock = 8, Precio = 18.0, Promocion = true },
	new { Nombre = "Monitor", Stock = 5, Precio = 150.0, Promocion = false },
	new { Nombre = "Auriculares", Stock = 20, Precio = 45.0, Promocion = true }
};
var totalPromo = productos
	.Where(p => p.Promocion && p.Stock > 10)
	.Select(p => p.Stock * p.Precio)
	.DefaultIfEmpty(0)
	.Sum();
Console.WriteLine($"10-01: Guztizko balioa: {totalPromo}");

// 10-02 Ariketa
var citas = new List<(DateTime Fecha, string Paciente, bool Asistio)>
{
	(new DateTime(2025, 10, 1), "Juan", true),
	(new DateTime(2025, 10, 3), "Lucía", false),
	(new DateTime(2025, 9, 28), "Pedro", true),
	(new DateTime(2025, 10, 5), "María", true)
};
var ahora = DateTime.Now;
var mes = ahora.Month;
var año = ahora.Year;
var citasMes = citas.Where(c => c.Fecha.Month == mes && c.Fecha.Year == año).ToList();
var todosAsistieron = citasMes.Any() ? citasMes.All(c => c.Asistio) : false;
Console.WriteLine($"10-02: Paziente guztiak etorri dira? {todosAsistieron}");

// 10-03 Ariketa
var usuarios = new List<(string Nombre, int Edad, bool Activo, bool SesionReciente)>
{
	("Laura", 35, true, true),
	("Tomás", 28, true, false),
	("Elena", 40, false, true),
	("David", 32, true, true),
	("Sara", 29, true, true)
};
var resultadoUsuarios = usuarios
	.Where(u => u.Edad > 30 && u.Activo && u.SesionReciente)
	.Select(u => u.Nombre)
	.OrderBy(n => n)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine($"10-03: Erabiltzaileak: {string.Join(", ", resultadoUsuarios)}");

// 10-04 Ariketa
var estudiantes = new[]
{
	new { Nombre = "Alberto", Notas = new[] { 8.5, 9.2, 7.8 }, Matriculado = true },
	new { Nombre = "Bea", Notas = new[] { 6.0, 7.0, 8.0 }, Matriculado = true },
	new { Nombre = "Carlos", Notas = new[] { 9.5, 9.8, 10.0 }, Matriculado = false },
	new { Nombre = "Diana", Notas = new[] { 9.1, 8.9, 9.0 }, Matriculado = true }
};
var medias = estudiantes
	.Where(e => e.Matriculado && e.Notas.Any(n => n > 9))
	.Select(e => e.Notas.Average());
var resultadoEstudiantes = medias.DefaultIfEmpty(0).Average();
Console.WriteLine($"10-04: Batezbestekoen batezbestekoa: {resultadoEstudiantes}");

// 10-05 Ariketa
var empleados = new List<(string Nombre, DateTime FechaIngreso, double Salario, bool Activo)>
{
	("Ana", new DateTime(2018, 5, 1), 3200, true),
	("Luis", new DateTime(2021, 3, 15), 2800, true),
	("Marta", new DateTime(2019, 7, 10), 4000, false),
	("Carlos", new DateTime(2017, 1, 20), 3500, true),
	("Sofía", new DateTime(2022, 11, 5), 3000, true)
};
var empleadosFiltrados = empleados
	.Where(e => e.FechaIngreso.Year < 2020 && e.Activo)
	.Select(e => new { e.Nombre, Anios = (DateTime.Now.Year - e.FechaIngreso.Year) })
	.DefaultIfEmpty(new { Nombre = "0", Anios = 0 })
	.ToList();
Console.WriteLine("10-05: Langileak eta urteak: " + string.Join(", ", empleadosFiltrados.Select(e => $"{e.Nombre} ({e.Anios})")));

// 10-06 Ariketa
var facturas = new List<(string Numero, DateTime Fecha, double Importe, bool Pagada)>
{
	("F001", DateTime.Now.AddDays(-10), 500, false),
	("F002", DateTime.Now.AddDays(-70), 300, false),
	("F003", DateTime.Now.AddDays(-30), 700, true),
	("F004", DateTime.Now.AddDays(-20), 800, false),
	("F005", DateTime.Now.AddDays(-5), 200, false)
};
var mediaFacturas = facturas.Average(f => f.Importe);
var facturasFiltradas = facturas
	.Where(f => !f.Pagada && (DateTime.Now - f.Fecha).TotalDays <= 60 && f.Importe > mediaFacturas)
	.Select(f => f.Numero)
	.ToList();
Console.WriteLine("10-06: Fakturak: " + (facturasFiltradas.Any() ? string.Join(", ", facturasFiltradas) : "0"));

// 10-07 Ariketa
var proyectos = new[]
{
	new { Nombre = "Alpha", Dias = 45, Exitoso = true, Completado = true },
	new { Nombre = "Beta", Dias = 20, Exitoso = true, Completado = true },
	new { Nombre = "Gamma", Dias = 60, Exitoso = false, Completado = true },
	new { Nombre = "Delta", Dias = 35, Exitoso = true, Completado = false },
	new { Nombre = "Epsilon", Dias = 50, Exitoso = true, Completado = true }
};
var totalDias = proyectos
	.Where(p => p.Dias > 30 && p.Exitoso && p.Completado)
	.Select(p => p.Dias)
	.DefaultIfEmpty(0)
	.Sum();
Console.WriteLine($"10-07: Egun kopuru totala: {totalDias}");

// 10-08 Ariketa
var alumnos8 = new List<(string Nombre, List<bool> Asistencias, List<double> Notas)>
{
	("Lucía", new List<bool> { true, true, true }, new List<double> { 8.5, 9.1 }),
	("Mario", new List<bool> { true, false, true }, new List<double> { 9.5, 9.8 }),
	("Elena", new List<bool> { true, true, true }, new List<double> { 7.0, 8.0 }),
	("Carlos", new List<bool> { true, true, true }, new List<double> { 9.2, 9.7 })
};
var alumnosFiltrados8 = alumnos8
	.Where(a => a.Notas.Any(n => n >= 9) && a.Asistencias.All(x => x))
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-08: Ikasleak: " + string.Join(", ", alumnosFiltrados8));

// 10-09 Ariketa
var alumnos9 = new List<(string Nombre, List<double> Notas, bool Becado)>
{
	("Lucía", new List<double> { 8.5, 9.1, 9.0 }, true),
	("Mario", new List<double> { 7.0, 6.5, 8.0 }, false),
	("Elena", new List<double> { 9.5, 9.8, 10.0 }, true),
	("Carlos", new List<double> { 6.0, 7.0, 6.5 }, true)
};
var mediaGlobal = alumnos9.Select(a => a.Notas.Average()).Average();
var alumnosFiltrados9 = alumnos9
	.Where(a => a.Becado && a.Notas.Average() > mediaGlobal)
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-09: Ikasle bikainak: " + string.Join(", ", alumnosFiltrados9));

// 10-10 Ariketa
var alumnos10 = new List<(string Nombre, List<bool> Participaciones, bool TareasEntregadas)>
{
	("Lucía", new List<bool> { true, true, true, true, false }, true),
	("Mario", new List<bool> { true, false, false, true, false }, true),
	("Elena", new List<bool> { true, true, true, true, true }, false),
	("Carlos", new List<bool> { true, true, false, true, true }, true)
};
var alumnosFiltrados10 = alumnos10
	.Where(a => a.TareasEntregadas && a.Participaciones.Count > 0 && a.Participaciones.Count(x => x) >= 0.8 * a.Participaciones.Count)
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-10: Parte-hartzaileak: " + string.Join(", ", alumnosFiltrados10));

// 10-11 Ariketa
var alumnos11 = new List<(string Nombre, List<double> Trimestres, bool SolicitoTutoria)>
{
	("Lucía", new List<double> { 7.0, 8.0, 9.0 }, false),
	("Mario", new List<double> { 6.5, 6.0, 7.0 }, true),
	("Elena", new List<double> { 8.0, 8.5, 8.5 }, false),
	("Carlos", new List<double> { 5.0, 6.5, 7.5 }, false)
};
var alumnosFiltrados11 = alumnos11
	.Where(a => !a.SolicitoTutoria && a.Trimestres.First() < a.Trimestres.Last())
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-11: Aurrerapena dutenak: " + string.Join(", ", alumnosFiltrados11));

// 10-12 Ariketa
var alumnos12 = new List<(string Nombre, List<double> Notas, List<bool> Asistencias, bool RiesgoAcademico)>
{
	("Lucía", new List<double> { 4.5, 4.0, 5.0 }, new List<bool> { true, true, true, true, true }, true),
	("Mario", new List<double> { 6.0, 5.5, 6.5 }, new List<bool> { true, false, true, true, true }, false),
	("Elena", new List<double> { 3.0, 4.0, 4.5 }, new List<bool> { true, true, true, true, false }, true),
	("Carlos", new List<double> { 4.0, 4.5, 4.8 }, new List<bool> { true, true, true, true, true }, true)
};
var alumnosFiltrados12 = alumnos12
	.Where(a => a.RiesgoAcademico && a.Notas.Average() < 5 && a.Asistencias.Count(x => x) >= 0.9 * a.Asistencias.Count)
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-12: Arriskuan daudenak: " + string.Join(", ", alumnosFiltrados12));

// 10-13 Ariketa
var alumnos13 = new List<(string Nombre, List<bool> Asistencias, List<double> Notas)>
{
	("Lucía", new List<bool> { true, true, false }, new List<double> { 9.0, 9.5 }),
	("Mario", new List<bool> { true, true, true }, new List<double> { 8.0, 8.2 }),
	("Elena", new List<bool> { true, false, true }, new List<double> { 9.1, 9.3 }),
	("Carlos", new List<bool> { true, true, true }, new List<double> { 9.0, 9.0 })
};
var alumnosFiltrados13 = alumnos13
	.Where(a => a.Asistencias.Any(x => !x) && a.Notas.Average() > 8.5)
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-13: Irregularrak eta altuak: " + string.Join(", ", alumnosFiltrados13));

// 10-14 Ariketa
var alumnos14 = new List<(string Nombre, List<DateTime> Entregas, bool Activo)>
{
	("Lucía", new List<DateTime> { DateTime.Now.AddDays(-10), DateTime.Now.AddDays(-30) }, true),
	("Mario", new List<DateTime> { DateTime.Now.AddDays(-20) }, true),
	("Elena", new List<DateTime> { DateTime.Now.AddDays(-5) }, false),
	("Carlos", new List<DateTime> { DateTime.Now.AddDays(-2), DateTime.Now.AddDays(-1) }, true)
};
var alumnosFiltrados14 = alumnos14
	.Where(a => a.Activo && a.Entregas.Any(e => (DateTime.Now - e).TotalDays <= 15))
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-14: Azkenaldian entregatu dutenak: " + string.Join(", ", alumnosFiltrados14));

// 10-15 Ariketa
var alumnos15 = new List<(string Nombre, int Tareas, int Participaciones)>
{
	("Lucía", 5, 10),
	("Mario", 3, 7),
	("Elena", 6, 4),
	("Carlos", 2, 12)
};
var alumnosFiltrados15 = alumnos15
	.OrderByDescending(a => a.Tareas + a.Participaciones)
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-15: Esfortzuaren ranking-a: " + string.Join(", ", alumnosFiltrados15));

// 10-16 Ariketa
var alumnos16 = new List<(string Nombre, List<bool> Tareas, List<double> Notas)>
{
	("Lucía", new List<bool> { true, false, true }, new List<double> { 5.5, 6.0 }),
	("Mario", new List<bool> { true, true, true }, new List<double> { 7.0, 8.0 }),
	("Elena", new List<bool> { false, false, true }, new List<double> { 4.5, 5.0 }),
	("Carlos", new List<bool> { true, true, false }, new List<double> { 6.5, 6.0 })
};
var alumnosFiltrados16 = alumnos16
	.Where(a => a.Tareas.Any(x => !x) && a.Notas.Average() <= 6)
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-16: Atazak osatu gabe eta baxuak: " + string.Join(", ", alumnosFiltrados16));

// 10-17 Ariketa
var alumnos17 = new List<(string Nombre, List<DateTime> Entregas)>
{
	("Lucía", new List<DateTime> { new DateTime(2025, 10, 4), new DateTime(2025, 10, 6) }),
	("Mario", new List<DateTime> { new DateTime(2025, 10, 2) }),
	("Elena", new List<DateTime> { new DateTime(2025, 10, 5) }),
	("Carlos", new List<DateTime> { new DateTime(2025, 10, 3) })
};
var alumnosFiltrados17 = alumnos17
	.Where(a => a.Entregas.Any(e => e.DayOfWeek == DayOfWeek.Saturday || e.DayOfWeek == DayOfWeek.Sunday))
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-17: Jaiegunetan entregatu dutenak: " + string.Join(", ", alumnosFiltrados17));

// 10-18 Ariketa
var alumnos18 = new List<(string Nombre, List<double> Notas)>
{
	("Lucía", new List<double> { 8.5, 8.7, 8.6 }),
	("Mario", new List<double> { 7.0, 9.0, 6.5 }),
	("Elena", new List<double> { 9.0, 9.5, 9.2 }),
	("Carlos", new List<double> { 6.0, 7.5, 8.0 })
};
var alumnosFiltrados18 = alumnos18
	.Where(a => a.Notas.Max() - a.Notas.Min() <= 1)
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-18: Orekatua dutenak: " + string.Join(", ", alumnosFiltrados18));

// 10-19 Ariketa
var alumnos19 = new List<(string Nombre, List<double> Notas)>
{
	("Lucía", new List<double> { 9.0, 9.5, 8.6 }),
	("Mario", new List<double> { 8.0, 8.7, 9.0 }),
	("Elena", new List<double> { 9.1, 9.3, 9.2 }),
	("Carlos", new List<double> { 7.5, 8.0, 8.5 })
};
var alumnosFiltrados19 = alumnos19
	.Where(a => a.Notas.All(n => n > 8.5))
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-19: Bikainak guztietan: " + string.Join(", ", alumnosFiltrados19));

// 10-20 Ariketa
var alumnos20 = new List<(string Nombre, List<double> Notas)>
{
	("Lucía", new List<double> { 6.0, 7.5, 8.5 }),
	("Mario", new List<double> { 7.0, 7.5, 8.0 }),
	("Elena", new List<double> { 5.0, 6.0, 7.5 }),
	("Carlos", new List<double> { 8.0, 8.5, 9.0 })
};
var alumnosFiltrados20 = alumnos20
	.Where(a => a.Notas.Last() - a.Notas.First() > 2)
	.Select(a => a.Nombre)
	.DefaultIfEmpty("0")
	.ToList();
Console.WriteLine("10-20: Azken ebaluazioan hobetu dutenak: " + string.Join(", ", alumnosFiltrados20));
