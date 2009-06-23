/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.windows;

import com.silverpeas.openoffice.OfficeFinder;
import com.silverpeas.openoffice.OfficeNotFoundException;
import com.silverpeas.openoffice.OpenOfficeFinder;
import java.io.File;

/**
 *
 * @author ehugonnet
 */
public class MsOfficePathFinder implements OfficeFinder {

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
      return basePath + File.separator + EXCEL;
    }
    return openOffice.findSpreadsheet();
  }

  @Override
  public String findPresentation() throws OfficeNotFoundException {
    if (exists(POWERPOINT)) {
      return basePath + File.separator + POWERPOINT;
    }
    return openOffice.findPresentation();
  }

  @Override
  public String findWordEditor() throws OfficeNotFoundException {
    if (exists(WORDS)) {
      return basePath + File.separator + WORDS + " /m";
    }
    return openOffice.findWordEditor();
  }

  @Override
  public String findOther() throws OfficeNotFoundException {
    return openOffice.findOther();
  }

  @Override
  public boolean isMicrosoftOffice2007() {
    return false;
  }

  protected boolean exists(String exe) {
    File executable = new File(basePath, exe);
    return executable.exists() && executable.isFile();
  }
}
