/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

import com.silverpeas.openoffice.util.MessageDisplayer;
import com.silverpeas.openoffice.util.MessageUtil;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

import com.silverpeas.openoffice.util.MsOfficeType;
import com.silverpeas.openoffice.util.PasswordManager;
import com.silverpeas.openoffice.util.UrlExtractor;
import com.silverpeas.openoffice.windows.MsOfficePathFinder;
import java.io.File;

/**
 * 
 * @author Emmanuel Hugonnet
 */
public class Launcher {

  static final String LAUNCHER_VERSION = "2.11";
  static final MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();
  static Logger logger = Logger.getLogger(Launcher.class.getName());

  /**
   * @param args
   *            the command line arguments
   */
  public static void main(String[] args) throws OfficeNotFoundException {
    logger.log(Level.INFO,
        MessageUtil.getMessage("app.title") + " version " + LAUNCHER_VERSION);
    logger.log(Level.INFO, MessageUtil.getMessage("info.url.encoded") + args[0]);
    try {
      String url = UrlExtractor.extractUrl(args[0]);
      logger.log(Level.INFO, MessageUtil.getMessage("info.url.decoded") + url);
      if (args[1] != null && !"".equals(args[1].trim())) {
        logger.log(Level.INFO,
            MessageUtil.getMessage("info.default.path") + ' ' + args[1]);
        MsOfficePathFinder.basePath = args[1].replace('/', File.separatorChar);
      }
      AuthenticationInfo authInfo = null;
      if (args.length >= 4) {
        authInfo = PasswordManager.extractAuthenticationInfo(args[2], args[3]);
      }
      MsOfficeType contentType = getContentType(UrlExtractor.decodeUrl(args[0]));
      logger.log(Level.FINE, MessageUtil.getMessage("info.document.type") +
          contentType);
      System.exit(OfficeLauncher.launch(contentType, url, authInfo));
    } catch (IOException ex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"),
          ex);
      MessageDisplayer.displayError(ex);
    } catch (InterruptedException ex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"),
          ex);
      MessageDisplayer.displayError(ex);
    } catch (Throwable ex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"),
          ex);
      MessageDisplayer.displayError(ex);
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
