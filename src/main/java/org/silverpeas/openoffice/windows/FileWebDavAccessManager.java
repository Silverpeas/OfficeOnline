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
package org.silverpeas.openoffice.windows;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.silverpeas.openoffice.AuthenticationInfo;
import org.silverpeas.openoffice.util.MessageUtil;
import org.silverpeas.openoffice.windows.webdav.WebdavManager;

/**
 * This class manage download and upload of documents using webdav protocol.
 *
 * @author Ludovic Bertin
 */
public class FileWebDavAccessManager {

  private final String userName;
  private final char[] password;
  private String lockToken = null;
  static final Logger logger = Logger.getLogger(FileWebDavAccessManager.class.getName());

  /**
   * The AccessManager is inited with authentication info to avoid login prompt
   *
   * @param auth authentication info
   */
  public FileWebDavAccessManager(AuthenticationInfo auth) {
    if (auth != null) {
      this.userName = auth.getLogin();
      this.password = auth.getPassword();
    } else {
      this.userName = "";
      this.password = new char[0];
    }
  }

  /**
   * Retrieve the file from distant URL to local temp file.
   *
   * @param url document url
   * @return full path of local temp file
   * @throws HttpException
   * @throws IOException
   */
  public String retrieveFile(String url) throws HttpException, IOException {
    URI uri = getURI(url);
    WebdavManager webdav = new WebdavManager(uri.getHost(), userName, new String(password));
    // Let's lock the file
    lockToken = webdav.lockFile(uri, userName);
    logger.log(Level.INFO, "{0}{1}{2}", new Object[]{MessageUtil.getMessage("info.webdav.locked"),
      ' ', lockToken});
    try {
      String tmpFile = webdav.getFile(uri, lockToken);
      logger.log(Level.INFO, "{0}{1}{2}", new Object[]{MessageUtil.getMessage(
        "info.webdav.file.locally.saved"), ' ', tmpFile});
      return tmpFile;
    } catch (IOException ex) {
      webdav.unlockFile(uri, lockToken);
      throw ex;
    }
  }

  /**
   * Push back file into remote location using webdav.
   *
   * @param tmpFilePath full path of local temp file
   * @param url remote url
   * @throws HttpException
   * @throws IOException
   */
  public void pushFile(String tmpFilePath, String url) throws HttpException, IOException {
    URI uri = getURI(url);
    WebdavManager webdav = new WebdavManager(uri.getHost(), userName, new String(password));
    logger.log(Level.INFO, "{0}{1}{2}", new Object[]{MessageUtil.getMessage("info.webdav.put"), ' ',
      tmpFilePath});
    webdav.putFile(uri, tmpFilePath, lockToken);
    logger.log(Level.INFO, "{0}{1}{2}",
        new Object[]{MessageUtil.getMessage("info.webdav.unlocking"), ' ', uri.getEscapedURI()});
    // Let's unlock the file
    webdav.unlockFile(uri, lockToken);
    // delete temp file
    File file = new File(tmpFilePath);
    file.delete();
    file.getParentFile().delete();
    logger.log(Level.INFO, MessageUtil.getMessage("info.file.deleted"));
    logger.log(Level.INFO, MessageUtil.getMessage("info.ok"));
  }

  private static URI getURI(String url) throws URIException {
    return new URI(url, false, "UTF-8");
  }
}
