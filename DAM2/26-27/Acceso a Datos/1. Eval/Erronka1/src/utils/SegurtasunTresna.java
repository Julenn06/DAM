package utils;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SegurtasunTresna {
	private static final String ALGORITMO = "AES";

	private static final String KLABE_SEKRETUA = "ElorGym-2026#!";

	private static SecretKeySpec lortuKlabea() {
		try {
			byte[] klabebyteak = KLABE_SEKRETUA.getBytes("UTF-8");
			// AES-256 behar ditu 32 bytes
			byte[] klabepreparatua = Arrays.copyOf(klabebyteak, 32);
			return new SecretKeySpec(klabepreparatua, ALGORITMO);
		} catch (Exception e) {
			throw new RuntimeException("Errorea klabea prestatzean", e);
		}
	}

	public static Cipher lortuZifratzailea() throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITMO);
		cipher.init(Cipher.ENCRYPT_MODE, lortuKlabea());
		return cipher;
	}

	public static Cipher lortuDezifratzailea() throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITMO);
		cipher.init(Cipher.DECRYPT_MODE, lortuKlabea());
		return cipher;
	}
}
