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
package com.silverpeas.openoffice.windows.webdav;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.client.methods.LockMethod;
import org.apache.jackrabbit.webdav.lock.Scope;
import org.apache.jackrabbit.webdav.lock.Type;
import com.silverpeas.openoffice.util.MessageUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InterruptedIOException;
import java.net.URLDecoder;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.UIManager;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.jackrabbit.webdav.client.methods.UnLockMethod;

/**
 * Simple class to help manipulate Webdav ressources.
 * @author ehugonnet
 */
public class WebdavManager {

  private HttpClient client;
  static Logger logger = Logger.getLogger(
      WebdavManager.class.getName());

  /**
   * Prepare HTTP connections to the WebDav server
   * @param host the webdav server host name.
   * @param login the login for the user on the webdav server.
   * @param password the login for the user on the webdav server.
   */
  public WebdavManager(String host, String login, String password) {
    HostConfiguration hostConfig = new HostConfiguration();
    hostConfig.setHost(host);
    HttpConnectionManager connectionManager =
        new MultiThreadedHttpConnectionManager();
    HttpConnectionManagerParams connectionParams = new HttpConnectionManagerParams();
    int maxHostConnections = 20;
    connectionParams.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
    connectionManager.setParams(connectionParams);
    HttpClientParams clientParams = new HttpClientParams();
    clientParams.setParameter(HttpClientParams.CREDENTIAL_CHARSET, "ISO-8859-1");
    clientParams.setParameter(HttpClientParams.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    client = new HttpClient(clientParams, connectionManager);
    Credentials creds = new UsernamePasswordCredentials(login, password);
    client.getState().setCredentials(AuthScope.ANY, creds);
    client.setHostConfiguration(hostConfig);
  }

  /**
   * Lock a ressource on a webdav server.
   * @param uri the URI to the ressource to be locked.
   * @param login the user locking the ressource.
   * @return the lock token.
   * @throws IOException
   */
  public String lockFile(URI uri, String login) throws IOException {
    logger.log(Level.INFO, MessageUtil.getMessage("info.webdav.locking")
        + ' ' + uri.getEscapedURI());
    // Let's lock the file
    LockMethod lockMethod = new LockMethod(uri.getEscapedURI(),
        Scope.EXCLUSIVE, Type.WRITE, login, 600000l, false);
    client.executeMethod(lockMethod);
    if (lockMethod.succeeded()) {
      return lockMethod.getLockToken();
    } else {
      if (lockMethod.getStatusCode() == 423) {
        throw new IOException(MessageUtil.getMessage("error.webdav.already.locked"));
      }
      throw new IOException(MessageUtil.getMessage("error.webdav.locking")
          + ' ' + lockMethod.getStatusCode() + " - "
          + lockMethod.getStatusText());
    }
  }

  /**
   * Unlock a ressource on a webdav server.
   * @param uri the URI to the ressource to be unlocked.
   * @param lockToken the current lock token.
   * @throws IOException
   */
  public void unlockFile(URI uri, String lockToken) throws IOException {
    if (lockToken == null || lockToken.isEmpty()) {
      return;
    }
    UnLockMethod unlockMethod = new UnLockMethod(uri.getEscapedURI(), lockToken);
    client.executeMethod(unlockMethod);
    if (unlockMethod.getStatusCode() != 200 && unlockMethod.getStatusCode() != 204) {
      logger.log(Level.INFO, MessageUtil.getMessage("error.webdav.unlocking") + ' '
          + unlockMethod.getStatusCode());
    }
    try {
      unlockMethod.checkSuccess();
      logger.log(Level.INFO, MessageUtil.getMessage("info.webdav.unlocked"));
    } catch (DavException ex) {
      logger.log(Level.SEVERE,
          MessageUtil.getMessage("error.webdav.unlocking"), ex);
      throw new IOException(MessageUtil.getMessage("error.webdav.unlocking"),
          ex);
    }
  }

  /**
   * Get the ressource from the webdav server.
   * @param uri the uri to the ressource.
   * @param lockToken the current lock token.
   * @return the path to the saved file on the filesystem.
   * @throws IOException
   */
  public String getFile(URI uri, String lockToken) throws IOException {
    GetMethod method = executeGetFile(uri);
    String fileName = uri.getPath();
    fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
    fileName = URLDecoder.decode(fileName, "UTF-8");
    UIManager.put("ProgressMonitor.progressText", MessageUtil.getMessage("download.file.title"));
    ProgressMonitorInputStream is = new ProgressMonitorInputStream(null,
        MessageUtil.getMessage("downloading.remote.file") + ' ' + fileName,
        new BufferedInputStream(method.getResponseBodyAsStream()));
    fileName = fileName.replace(' ', '_');
    ProgressMonitor monitor = is.getProgressMonitor();
    monitor.setMaximum(new Long(method.getResponseContentLength()).intValue());
    monitor.setMillisToDecideToPopup(0);
    monitor.setMillisToPopup(0);
    File tempDir = new File(System.getProperty("java.io.tmpdir"), "silver-"
        + System.currentTimeMillis());
    tempDir.mkdirs();
    File tmpFile = new File(tempDir, fileName);
    FileOutputStream fos = new FileOutputStream(tmpFile);
    byte[] data = new byte[64];
    int c = 0;
    try {
      while ((c = is.read(data)) > -1) {
        fos.write(data, 0, c);
      }
    } catch (InterruptedIOException ioinex) {
      logger.log(Level.INFO, MessageUtil.getMessage("info.user.cancel") + ' '
          + ioinex.getMessage());
      unlockFile(uri, lockToken);
      System.exit(0);
    } finally {
      fos.close();
    }
    return tmpFile.getAbsolutePath();
  }

  /**
   * Update a ressource on the webdav file server.
   * @param uri the uri to the ressource.
   * @param localFilePath the path to the file to be uploaded on the filesystem.
   * @param lockToken the current lock token.
   * @throws IOException
   */
  public void putFile(URI uri, String localFilePath, String lockToken) throws IOException {
    // Checks if file still exists
    try {
      executeGetFile(uri);
    } catch (IOException ioex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.remote.file"));
      throw new IOException(MessageUtil.getMessage("error.remote.file"));
    }
    PutMethod putMethod = new PutMethod(uri.getEscapedURI());
    logger.log(Level.INFO, MessageUtil.getMessage("info.webdav.put") + ' ' + localFilePath);
    File file = new File(localFilePath);
    UploadProgressBar progress = new UploadProgressBar();
    progress.setMaximum(new Long(file.length()).intValue());
    progress.setMessage(MessageUtil.getMessage("uploading.remote.file") + ' ' +
        uri.getPath().substring(
        uri.getPath().lastIndexOf('/') + 1));
    MonitoredInputStream is =
        new MonitoredInputStream(new BufferedInputStream(new FileInputStream(file)));
    is.addPropertyChangeListener(progress);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] data = new byte[64];
    int c = 0;
    while ((c = is.read(data)) > -1) {
      baos.write(data, 0, c);
    }
    RequestEntity requestEntity = new ByteArrayRequestEntity(baos.toByteArray());
    putMethod.setRequestEntity(requestEntity);
    putMethod.setRequestHeader(PutMethod.HEADER_LOCK_TOKEN, lockToken);
    client.executeMethod(putMethod);
    progress.close();
    if (putMethod.succeeded()) {
      logger.log(Level.INFO, MessageUtil.getMessage("info.file.updated"));
    } else {
      throw new IOException(MessageUtil.getMessage("error.put.remote.file")
          + " - " + putMethod.getStatusCode() + " - "
          + putMethod.getStatusText());
    }
  }

  private GetMethod executeGetFile(URI uri) throws IOException {
    GetMethod method = new GetMethod();
    method.setURI(uri);
    client.executeMethod(method);
    if (method.getStatusCode() != 200) {
      throw new IOException(MessageUtil.getMessage("error.get.remote.file")
          + ' ' + method.getStatusCode() + " - " + method.getStatusText());
    }
    return method;
  }
}
