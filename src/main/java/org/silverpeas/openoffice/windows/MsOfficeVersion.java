/*
 * Copyright (C) 2000 - 2013 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection withWriter Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.openoffice.windows;

import org.silverpeas.openoffice.util.MsOfficeType;
import org.silverpeas.openoffice.util.RegistryApplicationKey;
import org.silverpeas.openoffice.util.RegistryKeyReader;

/**
 *
 * @author ehugonnet
 */
public enum MsOfficeVersion {

  Office95(7), Office97(8), Office2000(9), OfficeXP(10), Office2003(11),
  Office2007(12), Office2010(14);
  private final int numeralVersion;

  private MsOfficeVersion(int numeralVersion) {
    this.numeralVersion = numeralVersion;
  }

  public static MsOfficeVersion fromNumeralVersion(int version) {
    for (MsOfficeVersion officeVersion : MsOfficeVersion.values()) {
      if (version == officeVersion.numeralVersion) {
        return officeVersion;
      }
    }
    return Office2000;
  }

  public static MsOfficeVersion detectMsOffice(MsOfficeType type) {
    return getOfficeVersion(type.getRegistryApplicationKey());
  }

  protected static MsOfficeVersion getOfficeVersion(RegistryApplicationKey type) {
    String key = RegistryKeyReader.readKey(MsOfficeRegistryHelper.BASE_APPLICATION_KEY
        + type.getApplicationKey() + "\\CurVer\"");
    if (key != null) {
      int version = Integer.parseInt(key.substring(type.getApplicationKey().length() + 1));
      return MsOfficeVersion.fromNumeralVersion(version);
    }
    return MsOfficeVersion.Office2000;
  }

  public static boolean isOldOffice(MsOfficeType type) {
    MsOfficeVersion currentVersion = detectMsOffice(type);
    return OfficeXP == currentVersion || Office2000 == currentVersion || Office95 == currentVersion
        || Office97 == currentVersion;
  }
}
