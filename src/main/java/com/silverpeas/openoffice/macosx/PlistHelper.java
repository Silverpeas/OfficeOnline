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

/**
 * @author Emmanuel Hugonnet
 */
public class PlistHelper {
  Logger logger = Logger.getLogger(PlistHelper.class.getName());

  public static final String BINARY_TO_XML = "plutil -convert xml1";

  public static final String XML_TO_BINARY = "plutil -convert xml1";

  public void convertToBinary(String xmlPlist) {
    try {
      Process process = Runtime.getRuntime().exec(XML_TO_BINARY + ' ' + xmlPlist);
      process.waitFor();
    } catch (InterruptedException ex) {
      Logger.getLogger(PlistHelper.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(PlistHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void addEntry(String xmlFile) {

  }

  public void convertToXml(String binaryPlist) {
    try {
      Process process = Runtime.getRuntime().exec(BINARY_TO_XML + ' ' + binaryPlist);
      process.waitFor();
    } catch (InterruptedException ex) {
      Logger.getLogger(PlistHelper.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(PlistHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
