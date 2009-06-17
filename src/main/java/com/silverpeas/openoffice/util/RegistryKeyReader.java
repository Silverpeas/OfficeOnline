package com.silverpeas.openoffice.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class RegistryKeyReader {

  private static final String REGQUERY_UTIL = "reg query ";
  private static final String REGSTR_TOKEN = "REG_SZ";
  static Logger logger = Logger.getLogger(RegistryKeyReader.class.getName());

  public static String readKey(String regKey) {
    try {
      Process process = Runtime.getRuntime().exec(REGQUERY_UTIL + regKey);
      StreamReader reader = new StreamReader(process.getInputStream());
      reader.start();
      process.waitFor();
      reader.join();
      String result = reader.getResult();
      int p = result.indexOf(REGSTR_TOKEN);
      if (p == -1) {
        return null;
      }
      return result.substring(p + REGSTR_TOKEN.length()).trim();
    } catch (Exception e) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.reading.registry"), e);
    }
    return null;
  }
}
