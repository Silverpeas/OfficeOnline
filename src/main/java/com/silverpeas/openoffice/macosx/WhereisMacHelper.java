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
 * FLOSS exception.  You should have recieved a copy of the text describing
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
