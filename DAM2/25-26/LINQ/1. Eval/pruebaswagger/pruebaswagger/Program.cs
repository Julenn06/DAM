using Microsoft.OpenApi.Models;
using pruebaswagger;

var builder = WebApplication.CreateBuilder(args);

// Configuración de servicios
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();

// Configuración de CORS para Angular
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAngular", policy =>
    {
        policy.WithOrigins("http://localhost:4200", "http://localhost:4201", "http://localhost:5173")
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

// Inyección de dependencias
builder.Services.AddScoped<Metodos>();        // Registrar la clase base
builder.Services.AddScoped<MetodosService>(); // Registrar el controlador
// Configuración de Swagger
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo
    {
        Version = "v1",
        Title = "PruebaSwagger API",
        Description = "API completa con todos los métodos matemáticos y de cadenas",
        Contact = new OpenApiContact
        {
            Name = "Tu Nombre",
            Email = "tu@email.com"
        }
    });
});

var app = builder.Build();

// Pipeline de configuración HTTP
app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/swagger/v1/swagger.json", "PruebaSwagger API V1");
    c.RoutePrefix = string.Empty; // Swagger UI en la raíz
    c.DocExpansion(Swashbuckle.AspNetCore.SwaggerUI.DocExpansion.None);
});

// Habilitar CORS antes de la autorización
app.UseCors("AllowAngular");
app.UseAuthorization();

// ¡Aquí es donde se conecta automáticamente tu MetodosService!
app.MapControllers();

// Endpoint de bienvenida
app.MapGet("/", () => "¡Bienvenido a PruebaSwagger API! 🚀 Todos los métodos están disponibles en /api/metodosservice/");

app.Run();

// Program.cs limpio - toda la lógica está en MetodosService.cs
