/**
 * Copyright (C) 2000 - 2009 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of the GPL, you may
 * redistribute this Program in connection with Free/Libre Open Source Software ("FLOSS")
 * applications as described in Silverpeas's FLOSS exception. You should have received a copy of the
 * text describing the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.openoffice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.silverpeas.openoffice.util.MessageDisplayer;
import org.silverpeas.openoffice.util.MessageUtil;
import org.silverpeas.openoffice.util.MsOfficeType;
import org.silverpeas.openoffice.util.OsEnum;
import org.silverpeas.openoffice.util.PasswordManager;
import org.silverpeas.openoffice.util.UrlExtractor;
import org.silverpeas.openoffice.windows.MsOfficePathFinder;

/**
 * @author Emmanuel Hugonnet
 */
public class Launcher {

  static final String LAUNCHER_VERSION = "2.12";
  static final MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();
  static final Logger logger = Logger.getLogger(Launcher.class.getName());

  /**
   * @param args the command line arguments
   * @throws OfficeNotFoundException
   */
  public static void main(final String[] args) throws OfficeNotFoundException {
    logger.log(Level.INFO, "{0} version {1}", new Object[]{MessageUtil.getMessage("app.title"),
      LAUNCHER_VERSION});
    logger.log(Level.INFO, "{0}{1}", new Object[]{MessageUtil.getMessage("info.url.encoded"),
      args[0]});
    try {
      String url = UrlExtractor.extractUrl(args[0]);
      logger.log(Level.INFO, "{0}{1}", new Object[]{MessageUtil.getMessage("info.url.decoded"),
        url});
      if (args[1] != null && !"".equals(args[1].trim())) {
        logger.log(Level.INFO, "{0} {1}", new Object[]{
          MessageUtil.getMessage("info.default.path"), UrlExtractor.decodePath(args[1])});
        MsOfficePathFinder.basePath = UrlExtractor.decodePath(args[1]);
      }
      boolean useDeconnectedMode = false;
      if (args.length >= 3) {
        useDeconnectedMode = getBooleanValue(args[2]);
      }
      AuthenticationInfo authInfo = null;
      if (args.length >= 5) {
        authInfo = PasswordManager.extractAuthenticationInfo(args[3], args[4]);
      } else  if (args.length >= 4 ) {
        authInfo = PasswordManager.extractAuthenticationInfo(args[3], null);
      }
      MsOfficeType contentType = getContentType(UrlExtractor.decodeUrl(args[0]));
      logger.log(Level.FINE, "{0}{1}", new Object[]{MessageUtil.getMessage("info.document.type"),
        contentType});
      defineLookAndFeel();
      System.exit(OfficeLauncher.launch(contentType, url, authInfo, useDeconnectedMode));
    } catch (IOException ex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"), ex);
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

  protected static boolean getBooleanValue(final String expression) {
    return "true".equalsIgnoreCase(expression) || "yes".equalsIgnoreCase(expression)
        || "y".equalsIgnoreCase(expression) || "oui".equalsIgnoreCase(expression)
        || "1".equalsIgnoreCase(expression);
  }

  protected static void defineLookAndFeel() {
    try {
      try {
        if (OsEnum.getOS() == OsEnum.WINDOWS_VISTA || OsEnum.getOS() == OsEnum.WINDOWS_XP
            || OsEnum.getOS() == OsEnum.WINDOWS_SEVEN) {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
      } catch (ClassNotFoundException ex) {
        logger.log(Level.INFO, null, ex);
      } catch (InstantiationException ex) {
        logger.log(Level.INFO, null, ex);
      } catch (IllegalAccessException ex) {
        logger.log(Level.INFO, null, ex);
      }
    } catch (UnsupportedLookAndFeelException ex) {
      logger.log(Level.INFO, "Unable to load native look and feel");
    }
  }

  protected static MsOfficeType getContentType(String url) throws MalformedURLException {
    String fileName = new URL(url).getFile();
    String contentType = mimeTypes.getContentType(fileName.toLowerCase());
    return MsOfficeType.valueOfMimeType(contentType);
  }
}
