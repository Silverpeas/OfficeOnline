/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

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
import javax.swing.JOptionPane;

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
    System.out.println(MessageUtil.getMessage("app.title"));
    logger.log(Level.INFO, MessageUtil.getMessage("app.title"));
    logger.log(Level.INFO, MessageUtil.getMessage("info.url.encoded") + args[0]);
    try {
      String url = UrlExtractor.extractUrl(args[0]);
      logger.log(Level.INFO, MessageUtil.getMessage("info.url.decoded") + url);
      AuthenticationInfo authInfo = null;
      if (args.length >= 3) {
        authInfo = PasswordManager.extractAuthenticationInfo(args[1], args[2]);
      }

      MsOfficeType contentType = getContentType(UrlExtractor.decodeUrl(args[0]));
      logger.log(Level.FINE, MessageUtil.getMessage("info.document.type") +
          contentType);
      System.exit(OfficeLauncher.launch(contentType, url, authInfo));
    } catch (IOException ex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"),
          ex);
      JOptionPane.showMessageDialog(null, ex.getMessage(),
          MessageUtil.getMessage("error.title"), JOptionPane.ERROR_MESSAGE);
    } catch (InterruptedException ex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"),
          ex);
      JOptionPane.showMessageDialog(null, ex.getMessage(),
          MessageUtil.getMessage("error.title"), JOptionPane.ERROR_MESSAGE);
    } catch (Throwable ex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"),
          ex);
      JOptionPane.showMessageDialog(null, ex.getMessage(),
          MessageUtil.getMessage("error.title"), JOptionPane.ERROR_MESSAGE);
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
