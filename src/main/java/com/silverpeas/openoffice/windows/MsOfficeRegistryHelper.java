/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.windows;

import com.silverpeas.openoffice.OfficeFinder;
import com.silverpeas.openoffice.OfficeNotFoundException;
import com.silverpeas.openoffice.OpenOfficeFinder;
import com.silverpeas.openoffice.util.RegistryKeyReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class MsOfficeRegistryHelper implements OfficeFinder {

  static final OpenOfficeFinder openOffice = new RegistryHelper();
  static final String ACCESS = "Access.Application";
  static final String EXCEL = "Excel.Application";
  static final String OUTLOOK = "Outlook.Application";
  static final String POWERPOINT = "Powerpoint.Application";
  static final String WORD = "Word.Application";
  static final String FRONTPAGE = "FrontPage.Application";
  static final String BASE_APPLICATION_KEY = "\"HKEY_LOCAL_MACHINE\\Software\\Classes\\";
  static final String BASE_KEY_CLSID = "\"HKEY_LOCAL_MACHINE\\Software\\Classes\\CLSID\\";
  static final Pattern AUTOMATION = Pattern.compile("\\s*/[aA][uU][tT][oO][mM][aA][tT][iI][oO][nN]\\s*");

  static final String BASE_MSOFFICE_2007_KEY = "\"HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Office\\12.0\" /ve";

  protected String getClsid(String type) {
    return RegistryKeyReader.readKey(BASE_APPLICATION_KEY + type + "\\CLSID\"");
  }

  protected String getPath(String type) {
    String clsid = getClsid(type);
    if (clsid != null) {
      String path = RegistryKeyReader.readKey(BASE_KEY_CLSID + clsid + "\\LocalServer32\"");
      if (path != null) {
        return '"' + extractPath(path) + '"';
      }
    }
    return null;
  }

  protected String extractPath(String path) {
    String result = path;
    Matcher matcher = AUTOMATION.matcher(path);
    if (matcher.find()) {
      result = path.substring(0, matcher.start());
    }
    return result;
  }

  @Override
  public String findSpreadsheet() throws OfficeNotFoundException {
    String msPath = getPath(EXCEL);
    if (msPath == null) {
      msPath = openOffice.findOpenOffice();
    }
    return msPath;
  }

  @Override
  public String findPresentation() throws OfficeNotFoundException {
    String msPath = getPath(POWERPOINT);
    if (msPath == null) {
      msPath = openOffice.findOpenOffice();
    }
    return msPath;
  }

  @Override
  public String findWordEditor() throws OfficeNotFoundException {
    String msPath = getPath(WORD);
    if (msPath == null) {
      msPath = openOffice.findOpenOffice();
    } else {
    	msPath = msPath + " /m";
    }
    return msPath;
  }

  @Override
  public String findOther() throws OfficeNotFoundException {
    return openOffice.findOpenOffice();
  }
  
  @Override
  public boolean isMicrosoftOffice2007() {
	  return ( RegistryKeyReader.readKey(BASE_MSOFFICE_2007_KEY) != null);
  }
}
