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
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
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
