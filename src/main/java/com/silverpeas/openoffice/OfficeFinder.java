/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

/**
 *
 * @author Emmanuel Hugonnet
 */
public interface OfficeFinder {

  public String findSpreadsheet() throws OfficeNotFoundException;

  public String findPresentation() throws OfficeNotFoundException;

  public String findWordEditor() throws OfficeNotFoundException;

  public String findOther() throws OfficeNotFoundException;
  
  public boolean isMicrosoftOffice2007();
}
