/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.macosx;

import com.silverpeas.openoffice.OfficeFinder;
import com.silverpeas.openoffice.OfficeNotFoundException;
import com.silverpeas.openoffice.OpenOfficeFinder;
import java.io.File;

/**
 *
 * @author ehugonnet
 */
public class MacOsOfficeFinder implements OfficeFinder {

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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isMicrosoftOffice2007() {
    return isMsOfficePresent();
  }

  protected boolean isMsOfficePresent() {
    File officeDir = new File(OFFICE_PATH);
    return officeDir.exists();
  }
}
