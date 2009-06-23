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
 *
 * @author Emvnd.sun.star.webdavel Hugonnet
 */
public class WindowsOpenOfficeFinder extends OpenOfficeFinder {

  Logger logger = Logger.getLogger(WindowsOpenOfficeFinder.class.getName());
  private static final String[] VERSIONS = new String[]{"3.1", "3.0", "2.4",
    "2.3"};
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
