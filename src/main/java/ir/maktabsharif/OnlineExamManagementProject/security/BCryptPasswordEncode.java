package ir.maktabsharif.OnlineExamManagementProject.security;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptPasswordEncode {

        public static String encodeBCryptPassword(String password) {

            BCrypt.Hasher hasher = BCrypt.withDefaults();

            return hasher.hashToString(12, password.toCharArray());

        }

        public static boolean verifyBCryptPassword(String password, String hashedPassword) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword.toCharArray());
            return result.verified;
        }

    }


