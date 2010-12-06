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
package com.silverpeas.openoffice.util;

import java.io.File;

/**
 * @author Emmanuel Hugonnet
 */
public enum OsEnum {

  WINDOWS_XP("Application Data\\Mozilla\\Firefox\\Profiles"), WINDOWS_VISTA(
      "Appdata\\Roaming\\Mozilla\\Firefox"), WINDOWS_SEVEN("Appdata\\Roaming\\Mozilla\\Firefox"), LINUX(
      ".mozilla/firefox"), MAC_OSX(".mozilla/firefox");
  protected String profilesDir;

  OsEnum(String profilesDir) {
    this.profilesDir = profilesDir;
  }

  public static OsEnum getOS(String value) {
    if ("Windows Vista".equalsIgnoreCase(value)) {
      return WINDOWS_VISTA;
    }
    if ("Windows 7".equalsIgnoreCase(value)) {
      return WINDOWS_SEVEN;
    }
    if ("Windows XP".equalsIgnoreCase(value) || value.startsWith("Windows ")) {
      return WINDOWS_XP;
    }
    if ("Linux".equalsIgnoreCase(value)) {
      return LINUX;
    }
    return MAC_OSX;
  }

  public static OsEnum getOS() {
    return getOS(System.getProperty("os.name"));
  }

  public String getProfilesDirectory() {
    return System.getProperty("user.home") + File.separator + profilesDir;
  }
  
  
  public static boolean isWindows() {
    OsEnum currentOS = getOS();
    return currentOS == WINDOWS_SEVEN || currentOS == WINDOWS_XP || currentOS == WINDOWS_VISTA;
  }
  
  public static boolean isLinux() {
    return getOS() == LINUX;
  }
}
