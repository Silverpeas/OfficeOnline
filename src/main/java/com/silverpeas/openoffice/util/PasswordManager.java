/**
 * Copyright (C) 2000 - 2009 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.openoffice.util;

import com.silverpeas.openoffice.AuthenticationInfo;
import com.silverpeas.openoffice.Launcher;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ehugonnet
 */
public class PasswordManager {

  private final static byte[] KEY = new byte[] { -23, -75, -2, -17, 79, -94, -125,
      -14 };
  private final static Key decryptionKey = new SecretKeySpec(KEY, "DES");
  private final static String DIGITS = "0123456789abcdef";

  /**
   * Converts a hexadecimal String to clear password.
   * @param hex hexadecimal String to convert
   * @return resulting password
   */
  public static String decodePassword(String encodedPassword) throws
      UnsupportedEncodingException, GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
    byte[] bytes = new BigInteger(encodedPassword, 16).toByteArray();
    Logger.getLogger(Launcher.class.getName()).log(Level.INFO, "decrypted password byte array length : {0}", bytes.length);
    int nbCaracToRemove = (bytes.length) % 8;
    byte[] result = new byte[bytes.length - nbCaracToRemove];
    System.arraycopy(bytes, nbCaracToRemove, result, 0, bytes.length -
        nbCaracToRemove);
    return new String(cipher.doFinal(result), "UTF-8");
  }

  /**
   * Converts a password to a hexadecimal String containing the DES encrypted password.
   * @param password the password to encrypt
   * @return resulting hexadecimal String
   */
  public static String encodePassword(String password) throws
      UnsupportedEncodingException, GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, decryptionKey);
    byte[] cipherText = cipher.doFinal(password.getBytes("UTF-8"));
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i != cipherText.length; i++) {
      int v = cipherText[i] & 0xff;
      buf.append(DIGITS.charAt(v >> 4));
      buf.append(DIGITS.charAt(v & 0xf));
    }
    return buf.toString();
  }

  /**
   * Build an authentication objec from arguments
   * @param args arguments passed through JNLP
   * @return the Authentication object
   */
  public static AuthenticationInfo extractAuthenticationInfo(String login, String encodedPassword) {
    String clearPwd = null;
    String decodedLogin = login;
    try {
      clearPwd = decodePassword(encodedPassword);
      if(clearPwd.isEmpty()) {
        clearPwd = promptForpassword();
      }
      decodedLogin = URLDecoder.decode(login, "UTF-8");
    } catch (Exception e) {
      Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, "can't retrieve credentials", e);
      System.exit(-1);
    }

    return new AuthenticationInfo(decodedLogin, clearPwd);
  }

  private static String promptForpassword() {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
