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
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.openoffice.windows;

import org.silverpeas.openoffice.OfficeFinder;
import org.silverpeas.openoffice.OfficeNotFoundException;
import org.silverpeas.openoffice.util.RegistryKeyReader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.silverpeas.openoffice.util.RegistryApplicationKey;

/**
 * @author Emmanuel Hugonnet
 */
public class MsOfficeRegistryHelper implements OfficeFinder {

  static final Logger logger = Logger.getLogger(MsOfficeRegistryHelper.class.getName());
  static final OfficeFinder msOfficeFinder = new MsOfficePathFinder();
  static final String BASE_KEY_OFFICE = "\"HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Office\\";
  public static final String BASE_APPLICATION_KEY = "\"HKEY_LOCAL_MACHINE\\Software\\Classes\\";
  static final String BASE_KEY_64_CLSID =
      "\"HKEY_LOCAL_MACHINE\\Software\\Wow6432Node\\Classes\\CLSID\\";
  static final String BASE_KEY_CLSID = "\"HKEY_LOCAL_MACHINE\\Software\\Classes\\CLSID\\";
  static final Pattern AUTOMATION = Pattern.compile(
      "\\s*/[aA][uU][tT][oO][mM][aA][tT][iI][oO][nN]\\s*");

  protected String getClsid(RegistryApplicationKey type) {
    return RegistryKeyReader.readKey(BASE_APPLICATION_KEY + type.getApplicationKey() + "\\CLSID\"");
  }

 

  protected String getPath(RegistryApplicationKey type) {
    String clsid = getClsid(type);
    if (clsid != null) {
      String path = RegistryKeyReader.readKey(BASE_KEY_CLSID + clsid + "\\LocalServer32\"");
      if (path == null) {
        path = RegistryKeyReader.readKey(BASE_KEY_64_CLSID + clsid + "\\LocalServer32\"");
      }
      if (path != null) {
        String extractedPath = extractPath(path);
        if (!extractedPath.startsWith("\"")) {
          extractedPath = '"' + extractedPath + '"';
        }
        return extractedPath;
      }
    }
    return null;
  }

  protected String extractPath(String path) {
    String result = path;
    Matcher matcher = AUTOMATION.matcher(path);
    if (matcher.find()) {
      result = path.substring(0, matcher.start());
    }
    return result;
  }

  @Override
  public String findSpreadsheet() throws OfficeNotFoundException {
    String msPath = getPath(RegistryApplicationKey.EXCEL);
    if (msPath == null) {
      msPath = msOfficeFinder.findSpreadsheet();
    }
    return msPath;
  }

  @Override
  public String findPresentation() throws OfficeNotFoundException {
    String msPath = getPath(RegistryApplicationKey.POWERPOINT);
    if (msPath == null) {
      msPath = msOfficeFinder.findPresentation();
    }
    return msPath;
  }

  @Override
  public String findWordEditor() throws OfficeNotFoundException {
    String msPath = getPath(RegistryApplicationKey.WORD);
    if (msPath == null) {
      msPath = msOfficeFinder.findWordEditor();
    } else {
      msPath = msPath + " /m";
    }
    return msPath;
  }

  @Override
  public String findOther() throws OfficeNotFoundException {
    return msOfficeFinder.findOther();
  }

  @Override
  public boolean isMicrosoftOffice() {
    return getPath(RegistryApplicationKey.EXCEL) != null || getPath(
        RegistryApplicationKey.POWERPOINT) != null || getPath(RegistryApplicationKey.WORD) != null;
  }
}
