/**
 * Copyright (C) 2000 - 2009 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of the GPL, you may
 * redistribute this Program in connection with Free/Libre Open Source Software ("FLOSS")
 * applications as described in Silverpeas's FLOSS exception. You should have received a copy of the
 * text describing the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.openoffice.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.silverpeas.openoffice.AuthenticationInfo;
import org.silverpeas.openoffice.Launcher;

/**
 * @author ehugonnet
 */
public class PasswordManager {

  private final static byte[] KEY = new byte[]{-23, -75, -2, -17, 79, -94, -125,
    -14};
  private final static Key decryptionKey = new SecretKeySpec(KEY, "DES");
  private final static String DIGITS = "0123456789abcdef";

  /**
   * Converts a hexadecimal String to clear password.
   *
   * @param encodedPassword password to decode.
   * @return resulting password
   * @throws UnsupportedEncodingException
   * @throws GeneralSecurityException
   */
  public static char[] decodePassword(String encodedPassword) throws UnsupportedEncodingException,
      GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
    byte[] bytes = new BigInteger(encodedPassword, 16).toByteArray();
    Logger.getLogger(Launcher.class.getName()).log(Level.INFO,
        "decrypted password byte array length : {0}", bytes.length);
    int nbCaracToRemove = (bytes.length) % 8;
    byte[] result = new byte[bytes.length - nbCaracToRemove];
    System.arraycopy(bytes, nbCaracToRemove, result, 0, bytes.length - nbCaracToRemove);
    return new String(cipher.doFinal(result), "UTF-8").toCharArray();
  }

  /**
   * Converts a password to a hexadecimal String containing the DES encrypted password.
   *
   * @param password the password to encrypt
   * @return resulting hexadecimal String
   * @throws UnsupportedEncodingException
   * @throws GeneralSecurityException
   */
  public static String encodePassword(String password) throws UnsupportedEncodingException,
      GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, decryptionKey);
    byte[] cipherText = cipher.doFinal(password.getBytes("UTF-8"));
    StringBuilder buf = new StringBuilder(cipherText.length);
    for (int i = 0; i != cipherText.length; i++) {
      int v = cipherText[i] & 0xff;
      buf.append(DIGITS.charAt(v >> 4));
      buf.append(DIGITS.charAt(v & 0xf));
    }
    return buf.toString();
  }

  /**
   * Build an authentication objec from arguments
   *
   * @param login
   * @param encodedPassword
   * @return the Authentication object
   */
  public static AuthenticationInfo extractAuthenticationInfo(String login, String encodedPassword) {
    try {
      char[] clearPwd;
      if (encodedPassword != null && !encodedPassword.isEmpty()) {
        clearPwd = decodePassword(encodedPassword);
      } else {
        clearPwd = null;
      }
      if (clearPwd == null) {
        clearPwd = new char[0];
      }
      String decodedLogin = URLDecoder.decode(login, "UTF-8");
      return new AuthenticationInfo(decodedLogin, clearPwd);
    } catch (GeneralSecurityException ex) {
      Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, "can't retrieve credentials", ex);
      System.exit(-1);
    } catch (UnsupportedEncodingException ex) {
      Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, "can't retrieve credentials", ex);
      System.exit(-1);
    }
    return null;
  }

  private static char[] promptForpassword() {
    Logger.getLogger(Launcher.class.getName()).log(Level.INFO,
        "No password provided, need to ask for one");
    return MessageDisplayer.displayPromptPassword();
  }
}
