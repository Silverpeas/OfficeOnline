/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.macosx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.silverpeas.openoffice.OpenOfficeFinder;
import com.silverpeas.openoffice.OpenOfficeNotFoundException;
import com.silverpeas.openoffice.util.StreamReader;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class WhereisMacHelper extends OpenOfficeFinder {

  private static final String OPENOFFICE_EXE = "soffice";
  private static final String WHEREIS_CMD = "whereis";
  private static final String DEFAULT_PATH = "/Applications/OpenOffice.org.app/Contents/MacOS/soffice";

  public String whereis() {
    try {
      Process process = Runtime.getRuntime().exec(WHEREIS_CMD + ' ' + OPENOFFICE_EXE);
      StreamReader reader = new StreamReader(process.getInputStream());
      reader.start();
      process.waitFor();
      reader.join();
      return reader.getResult().trim();
    } catch (InterruptedException ex) {
      Logger.getLogger(WhereisMacHelper.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(WhereisMacHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  @Override
  public String findOpenOffice()
      throws OpenOfficeNotFoundException {
    String result = whereis();
    if (result == null || result.isEmpty()) {
      return DEFAULT_PATH;
    }
    return result.substring(result.indexOf(' ') + 1);
  }
}
