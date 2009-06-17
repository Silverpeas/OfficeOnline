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
  protected static final String OFFICE_PATH = "/Applications/Microsoft Office 2008/";
  protected static final String EXCEL_CMD = "open -a \"" + OFFICE_PATH + "Microsoft Excel.app/Contents/MacOS/Microsoft Excel\"";
  protected static final String WORD_CMD = "open -a \"" + OFFICE_PATH + "Microsoft Word.app/Contents/MacOS/Microsoft Word\"";
  protected static final String POWERPOINT_CMD = "open -a \"" + OFFICE_PATH + "Microsoft PowerPoint.app/Contents/MacOS/Microsoft PowerPoint\"";

  public String findSpreadsheet() throws OfficeNotFoundException {
    if(isMsOfficePresent()) {
      return EXCEL_CMD;
    }
    return finder.findSpreadsheet();
  }

  public String findPresentation() throws OfficeNotFoundException {
    if(isMsOfficePresent()) {
      return POWERPOINT_CMD;
    }
    return finder.findPresentation();
  }

  public String findWordEditor() throws OfficeNotFoundException {
     if(isMsOfficePresent()) {
      return WORD_CMD;
    }
    return finder.findWordEditor();
  }

  public String findOther() throws OfficeNotFoundException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean isMicrosoftOffice2007() {
    return false;
  }

  protected boolean isMsOfficePresent() {
    File officeDir = new File(OFFICE_PATH);
    return officeDir.exists();
  }
}
