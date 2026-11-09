package format;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import model.Persona;

public final class JsonPersonas {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

	private JsonPersonas() {
	}

	public static void escribirArray(List<Persona> personas, File ruta) throws IOException {
		try (Writer out = new FileWriter(ruta)) {
			GSON.toJson(personas, out);
		}
	}

	public static List<Persona> leerArray(File ruta) throws IOException {
		try (Reader in = new FileReader(ruta)) {
			return Arrays.asList(GSON.fromJson(in, Persona[].class));
		}
	}

	public static void escribirEnvolvente(List<Persona> personas, File ruta) throws IOException {
		JsonObject root = new JsonObject();
		root.addProperty("tipo", "personas");
		root.add("datos", GSON.toJsonTree(personas));
		try (Writer out = new FileWriter(ruta)) {
			GSON.toJson(root, out);
		}
	}

	public static List<Persona> leerEnvolvente(File ruta) throws IOException {
		try (Reader in = new FileReader(ruta)) {
			JsonObject root = JsonParser.parseReader(in).getAsJsonObject();
			return Arrays.asList(GSON.fromJson(root.get("datos"), Persona[].class));
		}
	}

	public static void escribirObjeto(Persona persona, File ruta) throws IOException {
		try (Writer out = new FileWriter(ruta)) {
			GSON.toJson(persona, out);
		}
	}

	public static Persona leerObjeto(File ruta) throws IOException {
		try (Reader in = new FileReader(ruta)) {
			return GSON.fromJson(in, Persona.class);
		}
	}

	private static final class LocalDateAdapter extends TypeAdapter<LocalDate> {
		@Override
		public void write(JsonWriter out, LocalDate value) throws IOException {
			out.value(value == null ? null : value.toString());
		}

		@Override
		public LocalDate read(JsonReader in) throws IOException {
			String value = in.nextString();
			return value == null ? null : LocalDate.parse(value);
		}
	}
}