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

import org.silverpeas.openoffice.util.ApplicationArguments;
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
public class OfficeOnline {

  static final String LAUNCHER_VERSION = "3.0";

  static final Logger logger = Logger.getLogger(OfficeOnline.class.getName());

  /**
   * @param args the command line arguments
   * @throws OfficeNotFoundException
   */
  public static void main(final String[] args) throws OfficeNotFoundException {
    logVersion();
    try {
      ApplicationArguments arguments = ApplicationArguments.extract(args);
      log("info.url.encoded", arguments.getEncodedUrl());
      log("info.url.decoded", arguments.getUrl());
      if (arguments.getBasePath() != null) {
        log("info.default.path", arguments.getBasePath());
        MsOfficePathFinder.basePath = arguments.getBasePath();
      }
      log("info.document.type", arguments.getContentType());
      defineLookAndFeel();
      System.exit(OfficeLauncher.launch(arguments));
    } catch (IOException ex) {
      log("error.message.general", ex);
      MessageDisplayer.displayError(ex);
    } catch (InterruptedException ex) {
      log("error.message.general", ex);
      MessageDisplayer.displayError(ex);
    } catch (Throwable ex) {
      log("error.message.general", ex);
      MessageDisplayer.displayError(ex);
    } finally {
      System.exit(0);
    }
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

  private static void log(String key, Object extraInfo) {
    if (extraInfo instanceof Exception) {
      logger.log(Level.SEVERE, MessageUtil.getMessage(key), extraInfo);
    } else {
      logger.log(Level.INFO, "{0}{1}", new Object[]{MessageUtil.getMessage(key), extraInfo});
    }
  }

  private static void logVersion() {
    logger.log(Level.INFO, "{0} version {1}", new Object[]{MessageUtil.getMessage("app.title"),
        LAUNCHER_VERSION});
  }
}
