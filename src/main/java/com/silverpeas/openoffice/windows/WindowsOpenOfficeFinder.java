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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.windows;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.silverpeas.openoffice.OpenOfficeFinder;
import com.silverpeas.openoffice.OpenOfficeNotFoundException;
import com.silverpeas.openoffice.util.MessageUtil;
import com.silverpeas.openoffice.util.RegistryKeyReader;

/**
 * @author Emvnd.sun.star.webdavel Hugonnet
 */
public class WindowsOpenOfficeFinder extends OpenOfficeFinder {

  Logger logger = Logger.getLogger(WindowsOpenOfficeFinder.class.getName());
  private static final String[] VERSIONS = new String[] { "3.1", "3.0", "2.4",
      "2.3" };
  private static final String GLOBAL_OPEN_OFFICE_FOLDER =
      "\"HKEY_LOCAL_MACHINE\\SOFTWARE\\OpenOffice.org\\OpenOffice.org\\";
  private static final String LOCAL_OPEN_OFFICE_FOLDER =
      "\"HKEY_CURRENT_USER\\SOFTWARE\\OpenOffice.org\\OpenOffice.org\\";

  public String getOpenOfficePath(String version) {
    String key = null;
    try {
      key = RegistryKeyReader.readKey(LOCAL_OPEN_OFFICE_FOLDER + version +
          "\" /v Path");
      if (key == null) {
        key = RegistryKeyReader.readKey(GLOBAL_OPEN_OFFICE_FOLDER + version +
            "\" /v Path");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.reading.registry"),
          e);
    }
    return key;
  }

  @Override
  public String findOpenOffice() throws OpenOfficeNotFoundException {
    for (String version : VERSIONS) {
      logger.log(Level.FINE, MessageUtil.getMessage("info.openoffice.search") +
          MessageUtil.getMessage("info.openoffice.version") + version);
      String result = getOpenOfficePath(version);
      if (result != null) {
        logger.log(Level.FINE, MessageUtil.getMessage("info.openoffice.version")
            + version + ' ' + MessageUtil.getMessage("info.openoffice.found"));
        return '"' + result + '"';
      }
    }
    throw new OpenOfficeNotFoundException();
  }
}
