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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpException;

import org.silverpeas.openoffice.util.FinderFactory;
import org.silverpeas.openoffice.util.MessageDisplayer;
import org.silverpeas.openoffice.util.MessageUtil;
import org.silverpeas.openoffice.util.MsOfficeType;
import org.silverpeas.openoffice.util.OsEnum;
import org.silverpeas.openoffice.windows.FileWebDavAccessManager;
import org.silverpeas.openoffice.windows.MsOfficeVersion;

/**
 * @author Emmanuel Hugonnet
 */
public class OfficeLauncher {

  static final Logger logger = Logger.getLogger(OfficeLauncher.class.getName());

  /**
   * Launch the document editor.
   *
   * @param type type of editor for the document (word editor, presentation editor, etc.)
   * @param url the url to the document.
   * @param authInfo authentication parameters
   * @param useDeconnectedMode : set to true if you want to activate the Disconnected mode : 1)
   * download file using webdav to local temp directory 2) open it 3) after close, send it back to
   * silverpeas, still using webdav 4) delete temp file locally
   * @return the result of the process.
   * @throws IOException
   * @throws InterruptedException
   * @throws OfficeNotFoundException
   */
  public static int launch(MsOfficeType type, String url, AuthenticationInfo authInfo,
      boolean useDeconnectedMode) throws IOException, InterruptedException, OfficeNotFoundException {
    OfficeFinder finder = FinderFactory.getFinder(type);
    logger.log(Level.INFO, "Are we using Office : {0}", finder.isMicrosoftOffice());
    logger.log(Level.INFO, "We are on {0} OS", OsEnum.getOS());
    boolean modeDisconnected = ((OsEnum.isWindows() && useDeconnectedMode) || OsEnum.getOS()
        == OsEnum.MAC_OSX) && finder.isMicrosoftOffice();
    switch (type) {
      case EXCEL:
        return launch(type, finder.findSpreadsheet(), url, modeDisconnected, authInfo);
      case POWERPOINT:
        return launch(type, finder.findPresentation(), url, modeDisconnected, authInfo);
      case WORD:
        return launch(type, finder.findWordEditor(), url, modeDisconnected, authInfo);
      case NONE:
      default:
        return launch(type, finder.findOther(), url, modeDisconnected, authInfo);
    }
  }

  /**
   * Launch document edition
   *
   * @param path path to editor
   * @param url document url
   * @param modeDisconnected disconnected mode.
   * @param auth authentication info
   * @return status
   * @throws IOException
   * @throws InterruptedException
   */
  protected static int launch(MsOfficeType type, String path, String url, boolean modeDisconnected,
      AuthenticationInfo auth) throws IOException, InterruptedException {
    logger.log(Level.INFO, "The path: {0}", path);
    logger.log(Level.INFO, "The url: {0}", url);
    logger.log(Level.INFO, "The command line: {0} {1}", new Object[]{path, url});
    if (modeDisconnected) {
      try {
        String webdavUrl = url;
        final FileWebDavAccessManager webdavAccessManager = new FileWebDavAccessManager(auth);
        if ('"' == url.charAt(0)) {
          webdavUrl = url.substring(1, url.length() - 1);
        }
        String tmpFilePath = webdavAccessManager.retrieveFile(webdavUrl);
        logger.log(Level.INFO, "The exact exec line: {0} {1}", new Object[]{path, tmpFilePath});
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
      String webdavUrl = url;
      if (OsEnum.getOS() == OsEnum.WINDOWS_XP || (OsEnum.isWindows() && MsOfficeVersion.isOldOffice(
          type))) {
        webdavUrl = webdavUrl.replace("/repository/", "/repository2000/");
      }
      logger.log(Level.INFO, "The exact exec line: {0} {2}", new Object[]{path, webdavUrl});
      Process process = Runtime.getRuntime().exec(path + ' ' + webdavUrl);
      return process.waitFor();
    }
  }

  private OfficeLauncher() {
  }
}
