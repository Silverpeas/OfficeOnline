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
package com.silverpeas.openoffice.macosx;

import com.silverpeas.openoffice.OfficeFinder;
import com.silverpeas.openoffice.OfficeNotFoundException;
import com.silverpeas.openoffice.OpenOfficeFinder;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ehugonnet
 */
public class MacOsOfficeFinder implements OfficeFinder {

  static Logger logger = Logger.getLogger(MacOsOfficeFinder.class.getName());

  protected static final OpenOfficeFinder finder = new WhereisMacHelper();
  protected static final String OFFICE_PATH =
      "/Applications/Microsoft Office 2008/";
  protected static final String EXCEL_CMD = "open -a \"" + OFFICE_PATH +
      "Microsoft Excel.app/Contents/MacOS/Microsoft Excel\"";
  protected static final String WORD_CMD = "open -a \"" + OFFICE_PATH +
      "Microsoft Word.app/Contents/MacOS/Microsoft Word\"";
  protected static final String POWERPOINT_CMD = "open -a \"" + OFFICE_PATH +
      "Microsoft PowerPoint.app/Contents/MacOS/Microsoft PowerPoint\"";

  @Override
  public String findSpreadsheet() throws OfficeNotFoundException {
    if (isMsOfficePresent()) {
      return EXCEL_CMD;
    }
    return finder.findSpreadsheet();
  }

  @Override
  public String findPresentation() throws OfficeNotFoundException {
    if (isMsOfficePresent()) {
      return POWERPOINT_CMD;
    }
    return finder.findPresentation();
  }

  @Override
  public String findWordEditor() throws OfficeNotFoundException {
    if (isMsOfficePresent()) {
      return WORD_CMD;
    }
    return finder.findWordEditor();
  }

  @Override
  public String findOther() throws OfficeNotFoundException {
    return finder.findOther();
  }

  @Override
  public boolean isMicrosoftOffice2007() {
    return isMsOfficePresent();
  }

  protected boolean isMsOfficePresent() {
    File officeDir = new File(OFFICE_PATH);
    logger.log(Level.INFO, "Looking for file " + OFFICE_PATH + " but found " + officeDir.exists());
    return officeDir.exists();
  }

}
