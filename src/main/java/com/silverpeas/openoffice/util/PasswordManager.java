/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import com.silverpeas.openoffice.AuthenticationInfo;
import com.silverpeas.openoffice.Launcher;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ehugonnet
 */
public class PasswordManager {

  private final static byte[] KEY = new byte[]{-23, -75, -2, -17, 79, -94, -125,
    -14};
  private final static Key decryptionKey = new SecretKeySpec(KEY, "DES");
  private final static String DIGITS = "0123456789abcdef";

  /**
   * Converts a hexadecimal String to clear password.
   * @param hex hexadecimal String to convert
   * @return	resulting password
   */
  public static String decodePassword(String encodedPassword) throws
      UnsupportedEncodingException, GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
    byte[] bytes = new BigInteger(encodedPassword, 16).toByteArray();
    Logger.getLogger(Launcher.class.getName()).log(Level.INFO,
        "decrypted password byte array length : " + bytes.length);
    int nbCaracToRemove = (bytes.length) % 8;
    byte[] result = new byte[bytes.length - nbCaracToRemove];
    System.arraycopy(bytes, nbCaracToRemove, result, 0, bytes.length -
        nbCaracToRemove);
    return new String(cipher.doFinal(result), "UTF-8");
  }

  /**
   * Converts a password to a hexadecimal String containing the DES encrypted password.
   * @param password the password to encrypt
   * @return	resulting hexadecimal String
   */
  public static String encodePassword(String password) throws
      UnsupportedEncodingException, GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, decryptionKey);
    byte[] cipherText = cipher.doFinal(password.getBytes("UTF-8"));
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i != cipherText.length; i++) {
      int v = cipherText[i] & 0xff;
      buf.append(DIGITS.charAt(v >> 4));
      buf.append(DIGITS.charAt(v & 0xf));
    }
    return buf.toString();
  }

  /**
   * Build an authentication objec from arguments
   * @param args	arguments passed through JNLP
   * @return the Authentication object
   */
  public static AuthenticationInfo extractAuthenticationInfo(String login,
      String encodedPassword) {
    String clearPwd = null;
    try {      
      clearPwd = decodePassword(encodedPassword);
    } catch (Exception e) {
      Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE,
          "can't retrieve credentials", e);
      System.exit(-1);
    }
    return new AuthenticationInfo(login, clearPwd);
  }
}
