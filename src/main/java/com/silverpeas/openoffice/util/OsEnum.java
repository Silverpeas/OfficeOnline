/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import java.io.File;

/**
 *
 * @author Emmanuel Hugonnet
 */
public enum OsEnum {

  WINDOWS_XP("Application Data\\Mozilla\\Firefox\\Profiles"),
  WINDOWS_VISTA( "Appdata\\Roaming\\Mozilla\\Firefox"),
  LINUX( ".mozilla/firefox"),
  MAC_OSX( ".mozilla/firefox");
  
  protected String profilesDir;
  
  OsEnum(String profilesDir) {
    this.profilesDir = profilesDir;
  }

  public static OsEnum getOS(String value) {    
    if ("Windows Vista".equalsIgnoreCase(value)) {
      return WINDOWS_VISTA;
    }
    if ("Windows XP".equalsIgnoreCase(value) || value.startsWith("Windows ")) {
      return WINDOWS_XP;
    }
    if ("Linux".equalsIgnoreCase(value)) {
      return LINUX;
    }
    return MAC_OSX;
  }
  
  public String getProfilesDirectory() {
    return System.getProperty("user.home") + File.separator + profilesDir;
  }
}
