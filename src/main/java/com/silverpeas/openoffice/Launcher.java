/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

import com.silverpeas.openoffice.util.MsOfficeType;
import com.silverpeas.openoffice.util.PasswordManager;
import com.silverpeas.openoffice.util.UrlExtractor;

/**
 * 
 * @author Emmanuel Hugonnet
 */
public class Launcher {

  static final MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();
  static Logger logger = Logger.getLogger(Launcher.class.getName());

  /**
   * @param args
   *            the command line arguments
   */
  public static void main(String[] args) throws OfficeNotFoundException {
    System.out.println("OpenOffice Launcher");
    logger.log(Level.INFO, "OpenOffice Launcher");
    logger.log(Level.INFO, "URL encoded: " + args[0]);
    try {
      String url = UrlExtractor.extractUrl(args[0]);
      logger.log(Level.INFO, "URL decoded: " + url);
      /*
       * Test if password has been given
       */
      AuthenticationInfo authInfo = null;
      if (args.length >= 3) {
        authInfo = PasswordManager.extractAuthenticationInfo(args[1], args[2]);
      }

      MsOfficeType contentType = getContentType(UrlExtractor.decodeUrl(args[0]));
      logger.log(Level.FINE, "We have a document of type : " + contentType);
      System.exit(OfficeLauncher.launch(contentType, url, authInfo));
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Error launching OpenOffice", ex);
    } catch (InterruptedException ex) {
      logger.log(Level.SEVERE, "Error launching OpenOffice", ex);
    } catch (Throwable ex) {
      logger.log(Level.SEVERE, "Error launching OpenOffice", ex);
    } finally {
      System.exit(0);
    }
  }

  private static MsOfficeType getContentType(String url)
      throws MalformedURLException {
    String fileName = new URL(url).getFile();
    String contentType = mimeTypes.getContentType(fileName);
    return MsOfficeType.valueOfMimeType(contentType);
  }
}
