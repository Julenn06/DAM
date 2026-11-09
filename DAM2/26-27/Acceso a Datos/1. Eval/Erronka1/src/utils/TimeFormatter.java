package utils;

public class TimeFormatter {
	public static String segunduak(int segunduak) {
		int min = (int) Math.floor(segunduak / 60);
		int seg = (int) Math.floor(segunduak % 60);
		return String.format("%02d:%02d", min, seg);
	}
}
