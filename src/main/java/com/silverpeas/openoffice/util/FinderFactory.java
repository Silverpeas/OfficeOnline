/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import com.silverpeas.openoffice.OfficeFinder;
import com.silverpeas.openoffice.linux.WhereisHelper;
import com.silverpeas.openoffice.macosx.MacOsOfficeFinder;
import com.silverpeas.openoffice.macosx.WhereisMacHelper;
import com.silverpeas.openoffice.windows.MsOfficeRegistryHelper;
import com.silverpeas.openoffice.windows.WindowsOpenOfficeFinder;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class FinderFactory {

  public static OfficeFinder getFinder(MsOfficeType contentType) {
    OsEnum os = OsEnum.getOS(System.getProperty("os.name"));
    switch (os) {
      case WINDOWS_XP:
      case WINDOWS_VISTA:
        if (contentType.isMsOfficeCompatible()) {
          return new MsOfficeRegistryHelper();
        }
        return new WindowsOpenOfficeFinder();
      case LINUX:
        return new WhereisHelper();
      case MAC_OSX:
        return new WhereisMacHelper();
      default:
        return new WhereisHelper();
    }
  }
}
