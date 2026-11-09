package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;

public class Hash {
    private static Hasher hasher = BCrypt.withDefaults();
    private static Verifyer verifier = BCrypt.verifyer();
    private static int cost = 12;

    public static String hasheatu(char[] pasahitza) {
        return hasher.hashToString(cost, pasahitza);
    }

    public static boolean konprobatu(char[] pasahitza, CharSequence hash) {
        return verifier.verify(pasahitza, hash).verified;
    }
}
