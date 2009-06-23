/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

/**
 *
 * @author Emmanuel Hugonnet
 */
public abstract class OpenOfficeFinder implements OfficeFinder {

  @Override
  public String findSpreadsheet() throws OfficeNotFoundException {
    return findOpenOffice();
  }

  @Override
  public String findPresentation() throws OfficeNotFoundException {
    return findOpenOffice();
  }

  @Override
  public String findWordEditor() throws OfficeNotFoundException {
    return findOpenOffice();
  }

  @Override
  public String findOther() throws OfficeNotFoundException {
    return findOpenOffice();
  }

  @Override
  public boolean isMicrosoftOffice2007() {
    return false;
  }

  public abstract String findOpenOffice() throws OfficeNotFoundException;
}
