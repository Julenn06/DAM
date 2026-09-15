using Microsoft.Extensions.Logging;
using Microsoft.Extensions.DependencyInjection;
using ElorMAUI.Services;

namespace ElorMAUI
{
    public static class MauiProgram
    {
        public static MauiApp CreateMauiApp()
        {
            var builder = MauiApp.CreateBuilder();
            builder
                .UseMauiApp<App>()
                .ConfigureFonts(fonts =>
                {
                    fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                });

            builder.Services.AddMauiBlazorWebView();

            var backendUrl = "http://localhost:8080";

            // Cliente con nombre
            builder.Services.AddHttpClient("BackendApi", client =>
            {
                client.BaseAddress = new Uri(backendUrl);
            });

            // Cliente predeterminado para inyección directa
            builder.Services.AddScoped(sp =>
                sp.GetRequiredService<IHttpClientFactory>().CreateClient("BackendApi"));

#if DEBUG
            builder.Services.AddBlazorWebViewDeveloperTools();
            builder.Logging.AddDebug();
            builder.UseMauiApp<App>().UseMauiMaps();

#endif
            builder.Services.AddSingleton<ElorMAUI.Services.IkastetxeService>();

            return builder.Build();
        }
    }
}