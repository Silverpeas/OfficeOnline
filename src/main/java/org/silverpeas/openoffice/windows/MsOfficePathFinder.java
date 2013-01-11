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
package org.silverpeas.openoffice.windows;

import org.silverpeas.openoffice.OfficeFinder;
import org.silverpeas.openoffice.OfficeNotFoundException;
import org.silverpeas.openoffice.OpenOfficeFinder;
import java.io.File;

/**
 * @author ehugonnet
 */
public class MsOfficePathFinder implements OfficeFinder {

  private boolean isOpenOffice = true;
  static final OpenOfficeFinder openOffice = new WindowsOpenOfficeFinder();
  public static String basePath = "C:\\Program Files\\Microsoft Office\\OFFICE11";
  private static final String EXCEL = "EXCEL.EXE";
  private static final String WORDS = "WINWORD.EXE";
  private static final String POWERPOINT = "POWERPNT.EXE";

  public MsOfficePathFinder() {
  }

  @Override
  public String findSpreadsheet() throws OfficeNotFoundException {
    if (exists(EXCEL)) {
      isOpenOffice = false;
      return basePath + File.separator + EXCEL;
    }
    isOpenOffice = true;
    return openOffice.findSpreadsheet();
  }

  @Override
  public String findPresentation() throws OfficeNotFoundException {
    if (exists(POWERPOINT)) {
      isOpenOffice = false;
      return basePath + File.separator + POWERPOINT;
    }
    isOpenOffice = true;
    return openOffice.findPresentation();
  }

  @Override
  public String findWordEditor() throws OfficeNotFoundException {
    if (exists(WORDS)) {
      isOpenOffice = false;
      return basePath + File.separator + WORDS + " /m";
    }
    isOpenOffice = true;
    return openOffice.findWordEditor();
  }

  @Override
  public String findOther() throws OfficeNotFoundException {
    isOpenOffice = true;
    return openOffice.findOther();
  }

  @Override
  public boolean isMicrosoftOffice() {
    return !isOpenOffice;
  }

  protected boolean exists(String exe) {
    File executable = new File(basePath, exe);
    return executable.exists() && executable.isFile();
  }

}
