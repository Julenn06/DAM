using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using ElorMAUI.Components.Model;
using Microsoft.Maui.Storage;

namespace ElorMAUI.Services
{
    public class IkastetxeService
    {
        public List<Centro> Centros { get; private set; } = new List<Centro>();

        public async Task LoadCentrosAsync()
        {
            if (Centros != null && Centros.Any())
                return; // Ya cargados

            try
            {
                using var stream = await FileSystem.OpenAppPackageFileAsync("ikastetxeak.json");
                using var reader = new StreamReader(stream);
                var json = await reader.ReadToEndAsync();

                if (!string.IsNullOrWhiteSpace(json) && json.TrimStart().StartsWith("["))
                {
                    Centros = System.Text.Json.JsonSerializer.Deserialize<List<Centro>>(json) ?? new List<Centro>();
                }
                else
                {
                    Centros = new List<Centro>();
                }
            }
            catch
            {
                Centros = new List<Centro>();
            }
        }
    }
}
