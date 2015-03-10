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

import org.silverpeas.openoffice.util.ApplicationArguments;
import org.silverpeas.openoffice.util.FinderFactory;
import org.silverpeas.openoffice.util.MessageDisplayer;
import org.silverpeas.openoffice.util.MessageUtil;
import org.silverpeas.openoffice.util.MsOfficeType;
import org.silverpeas.openoffice.util.OsEnum;
import org.silverpeas.openoffice.windows.FileWebDavAccessManager;
import org.silverpeas.openoffice.windows.MsOfficeVersion;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Emmanuel Hugonnet
 */
public class OfficeLauncher {

  static final Logger logger = Logger.getLogger(OfficeLauncher.class.getName());

  /**
   * Launches the document editor corresponding to the type of the document to open.
   *
   * @see org.silverpeas.openoffice.util.ApplicationArguments
   *
   * @param arguments the arguments required to launch the document editor.
   * @return the process execution status.
   * @throws IOException
   * @throws InterruptedException
   * @throws OfficeNotFoundException
   */
  public static int launch(ApplicationArguments arguments)
      throws IOException, InterruptedException, OfficeNotFoundException {
    OfficeFinder finder = FinderFactory.getFinder(arguments.getContentType());
    logger.log(Level.INFO, "We are on {0} OS", OsEnum.getOS());
    String webdavUrl = arguments.getUrl();
    boolean disconnectedMode = ((OsEnum.isWindows() && arguments.isDisconnectedMode()) ||
        OsEnum.getOS() == OsEnum.MAC_OSX) && finder.isMicrosoftOffice();
    if (!disconnectedMode) {
      if (finder.isMicrosoftOffice() && (OsEnum.getOS() == OsEnum.WINDOWS_XP || (OsEnum
          .isWindows() && MsOfficeVersion.isOldOffice(arguments.getContentType())))) {
        webdavUrl = webdavUrl.replace("/repository/", "/repository2000/");
      }
    }
    switch (arguments.getContentType()) {
      case EXCEL:
        return launch(arguments.getContentType(), finder.findSpreadsheet(), webdavUrl,
            disconnectedMode, arguments.getLogin());
      case POWERPOINT:
        return launch(arguments.getContentType(), finder.findPresentation(), webdavUrl,
            disconnectedMode, arguments.getLogin());
      case WORD:
        return launch(arguments.getContentType(), finder.findWordEditor(), webdavUrl,
            disconnectedMode, arguments.getLogin());
      case NONE:
      default:
        return launch(arguments.getContentType(), finder.findOther(), webdavUrl, disconnectedMode,
            arguments.getLogin());
    }
  }

  /**
   * Launch document edition
   *
   * @param path the path of the editor to launch.
   * @param url the URL at which the document is located.
   * @param disconnectedMode is the document should be accessed in disconnected mode.
   * @return status the process execution status.
   * @throws IOException
   * @throws InterruptedException
   */
  protected static int launch(MsOfficeType type, String path, String url, boolean disconnectedMode,
      String login) throws IOException, InterruptedException {
    logger.log(Level.INFO, "The path: {0}", path);
    logger.log(Level.INFO, "The url: {0}", url);
    if (disconnectedMode) {
      try {
        String webDavUrl = unquoteUrl(url);
        final FileWebDavAccessManager webdavAccessManager = new FileWebDavAccessManager(login);
        String tmpFilePath = webdavAccessManager.retrieveFile(webDavUrl);
        logger.log(Level.INFO, "The exact exec line: {0} {1}", new Object[]{path, tmpFilePath});
        Process process = Runtime.getRuntime().exec(path + ' ' + tmpFilePath);
        process.waitFor();
        webdavAccessManager.pushFile(tmpFilePath, url);
        MessageDisplayer.displayMessage(MessageUtil.getMessage("info.ok"));
        return 0;
      } catch (IOException ex) {
        logger.log(Level.SEVERE, null, ex);
        throw ex;
      }
    } else {
      // Standard mode: just open it
      logger.log(Level.INFO, "The exact exec line: {0} {1}", new Object[]{path, url});
      Process process = Runtime.getRuntime().exec(path + ' ' + url);
      return process.waitFor();
    }
  }

  private OfficeLauncher() {
  }

  private static String unquoteUrl(String url) {
    String unquotedUrl = url;
    if ('"' == url.charAt(0)) {
      unquotedUrl = url.substring(1, url.length() - 1);
    }
    return unquotedUrl;
  }

}
