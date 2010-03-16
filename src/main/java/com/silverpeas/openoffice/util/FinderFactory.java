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
package com.silverpeas.openoffice.util;

import com.silverpeas.openoffice.OfficeFinder;
import com.silverpeas.openoffice.linux.WhereisHelper;
import com.silverpeas.openoffice.macosx.MacOsOfficeFinder;
import com.silverpeas.openoffice.windows.MsOfficeRegistryHelper;
import com.silverpeas.openoffice.windows.WindowsOpenOfficeFinder;

/**
 * @author Emmanuel Hugonnet
 */
public class FinderFactory {

  public static OfficeFinder getFinder(final MsOfficeType contentType) {
    OsEnum os = OsEnum.getOS(System.getProperty("os.name"));
    switch (os) {
      case WINDOWS_XP:
      case WINDOWS_VISTA:
      case WINDOWS_SEVEN:
        if (contentType.isMsOfficeCompatible()) {
          return new MsOfficeRegistryHelper();
        }
        return new WindowsOpenOfficeFinder();
      case LINUX:
        return new WhereisHelper();
      case MAC_OSX:
        return new MacOsOfficeFinder();
      default:
        return new WhereisHelper();
    }
  }
}
