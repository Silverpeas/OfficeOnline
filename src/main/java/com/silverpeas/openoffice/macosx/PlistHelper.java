/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.macosx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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
