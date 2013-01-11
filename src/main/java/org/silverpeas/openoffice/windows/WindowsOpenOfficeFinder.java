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
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.openoffice.windows;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.silverpeas.openoffice.OpenOfficeFinder;
import org.silverpeas.openoffice.OpenOfficeNotFoundException;
import org.silverpeas.openoffice.util.MessageUtil;
import org.silverpeas.openoffice.util.RegistryKeyReader;


public class WindowsOpenOfficeFinder extends OpenOfficeFinder {
  private static final OpenOfficeFinder libreOfficeFinder = new WindowsLibreOfficeFinder();
  static final Logger logger = Logger.getLogger(WindowsOpenOfficeFinder.class.getName());
  private static final String[] VERSIONS = new String[]{"3.4.1", "3.3", "3.2", "3.1", "3.0", "2.4",
    "2.3"};
  private static final String GLOBAL_OPEN_OFFICE_FOLDER =
      "\"HKEY_LOCAL_MACHINE\\SOFTWARE\\OpenOffice.org\\OpenOffice.org\\";
  private static final String GLOBAL_OPEN_OFFICE_FOLDER_64 =
      "\"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\OpenOffice.org\\OpenOffice.org\\";
  private static final String LOCAL_OPEN_OFFICE_FOLDER_64 =
      "\"HKEY_CURRENT_USER\\SOFTWARE\\Wow6432Node\\OpenOffice.org\\OpenOffice.org\\";
  private static final String LOCAL_OPEN_OFFICE_FOLDER =
      "\"HKEY_CURRENT_USER\\SOFTWARE\\OpenOffice.org\\OpenOffice.org\\";

  public String getOpenOfficePath(String version) {
    String key = null;
    try {
      key = RegistryKeyReader.readKey(LOCAL_OPEN_OFFICE_FOLDER + version + "\" /v Path");
      if (key == null) {
        key = RegistryKeyReader.readKey(GLOBAL_OPEN_OFFICE_FOLDER + version + "\" /v Path");
      }
      if (key == null) {
        key = RegistryKeyReader.readKey(LOCAL_OPEN_OFFICE_FOLDER_64 + version + "\" /v Path");
      }
      if (key == null) {
        key = RegistryKeyReader.readKey(GLOBAL_OPEN_OFFICE_FOLDER_64 + version + "\" /v Path");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.reading.registry"), e);
    }
    return key;
  }

  @Override
  public String findOpenOffice() throws OpenOfficeNotFoundException {
    for (String version : VERSIONS) {
      logger.log(Level.FINE, "{0}{1}{2}", new Object[]{MessageUtil.getMessage(
            "info.openoffice.search"), MessageUtil.getMessage("info.openoffice.version"), version});
      String result = getOpenOfficePath(version);
      if (result != null) {
        logger.log(Level.FINE, "{0}{1}{2}{3}", new Object[]{MessageUtil.getMessage(
              "info.openoffice.version"), version, ' ', MessageUtil.getMessage(
              "info.openoffice.found")});
        return '"' + result + '"';
      }
    }    
    return libreOfficeFinder.findOpenOffice();
  }
}
