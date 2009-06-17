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

  public String findSpreadsheet() throws OfficeNotFoundException {
    return findOpenOffice();
  }

  public String findPresentation() throws OfficeNotFoundException {
    return findOpenOffice();
  }

  public String findWordEditor() throws OfficeNotFoundException {
    return findOpenOffice();
  }

   public String findOther() throws OfficeNotFoundException {
    return findOpenOffice();
  }

   public boolean isMicrosoftOffice2007() {
	return false;   
   }
   
  public abstract String findOpenOffice() throws OfficeNotFoundException;
}
