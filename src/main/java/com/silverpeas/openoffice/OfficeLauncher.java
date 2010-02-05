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
package com.silverpeas.openoffice;

import com.silverpeas.openoffice.windows.FileWebDavAccessManager;
import com.silverpeas.openoffice.util.MessageUtil;
import com.silverpeas.openoffice.util.MessageDisplayer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.silverpeas.openoffice.util.FinderFactory;
import com.silverpeas.openoffice.util.MsOfficeType;
import com.silverpeas.openoffice.util.OsEnum;
import org.apache.commons.httpclient.HttpException;

/**
 * @author Emmanuel Hugonnet
 */
public class OfficeLauncher {

  static Logger logger = Logger.getLogger(OfficeLauncher.class.getName());

  /*
   * If user is under Windows vista and use MS Office 2007. Disconnected mode must be activated : 1)
   * download file using webdav to local temp directory 2) open it 3) after close, send it back to
   * silverpeas, still using webdav 4) delete temp file locally
   */
  public static int launch(MsOfficeType type, String url, AuthenticationInfo authInfo)
      throws IOException, InterruptedException, OfficeNotFoundException {
    OfficeFinder finder = FinderFactory.getFinder(type);
    boolean modeDisconnected = (OsEnum.getOS() == OsEnum.WINDOWS_VISTA ||
        OsEnum.getOS() == OsEnum.MAC_OSX) && (finder.isMicrosoftOffice2007());
    switch (type) {
      case EXCEL:
        return launch(finder.findSpreadsheet(), url, modeDisconnected, authInfo);
      case POWERPOINT:
        return launch(finder.findPresentation(), url, modeDisconnected, authInfo);
      case WORD:
        return launch(finder.findWordEditor(), url, modeDisconnected, authInfo);
      case NONE:
      default:
        return launch(finder.findOther(), url, modeDisconnected, authInfo);
    }
  }

  /**
   * Launch document edition
   * @param path path to editor
   * @param url document url
   * @param modeDisconnected disconnected mode (used under vista + MS Office 2007)
   * @param auth authentication info
   * @return status
   * @throws IOException
   * @throws InterruptedException
   */
  public static int launch(String path, String url, boolean modeDisconnected,
      AuthenticationInfo auth) throws IOException, InterruptedException {
    logger.log(Level.INFO, "The path: " + path);
    logger.log(Level.INFO, "The url: " + url);
    logger.log(Level.INFO, "The command line: " + path + ' ' + url);
    if (modeDisconnected) {
      try {
        String webdavUrl = url;
        final FileWebDavAccessManager webdavAccessManager = new FileWebDavAccessManager(auth);
        if ('"' == url.charAt(0)) {
          webdavUrl = url.substring(1, url.length() - 1);
        }
        String tmpFilePath = webdavAccessManager.retrieveFile(webdavUrl);
        logger.log(Level.INFO, "The exact exec line: " + path + ' ' + tmpFilePath);
        Process process = Runtime.getRuntime().exec(path + ' ' + tmpFilePath);
        process.waitFor();
        webdavAccessManager.pushFile(tmpFilePath, url);
        MessageDisplayer.displayMessage(MessageUtil.getMessage("info.ok"));
        return 0;
      } catch (HttpException ex) {
        logger.log(Level.SEVERE, null, ex);
        throw new IOException(ex);
      } catch (IOException ex) {
        logger.log(Level.SEVERE, null, ex);
        throw ex;
      }
    } else {
      // Standard mode : just open it
      logger.log(Level.INFO, "The exact exec line: " + path + ' ' + url);
      Process process = Runtime.getRuntime().exec(path + ' ' + url);
      return process.waitFor();
    }
  }
}
