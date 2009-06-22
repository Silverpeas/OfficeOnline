package com.silverpeas.openoffice.windows;

import com.silverpeas.openoffice.*;
import com.silverpeas.openoffice.util.MessageDisplayer;
import com.silverpeas.openoffice.util.MessageUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.net.URLEncoder;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.LockMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.jackrabbit.webdav.client.methods.UnLockMethod;
import org.apache.jackrabbit.webdav.lock.Scope;
import org.apache.jackrabbit.webdav.lock.Type;

/**
 * This class manage download and upload of documents using webdav protocol.
 * 
 * @author Ludovic Bertin
 *
 */
public class FileWebDavAccessManager {

  private String userName;
  private String password;
  private String lockToken = null;
  static Logger logger = Logger.getLogger(
      FileWebDavAccessManager.class.getName());

  /**
   * The AccessManager is inited with authentication info to avoid login prompt
   *
   * @param auth	authentication info
   */
  public FileWebDavAccessManager(AuthenticationInfo auth) {
    this.userName = auth.getLogin();
    this.password = auth.getPassword();
  }

  public HttpClient initConnection(URL url) {
    HostConfiguration hostConfig = new HostConfiguration();
    hostConfig.setHost(url.getHost());
    HttpConnectionManager connectionManager =
        new MultiThreadedHttpConnectionManager();
    HttpConnectionManagerParams params = new HttpConnectionManagerParams();
    int maxHostConnections = 20;
    params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
    connectionManager.setParams(params);
    HttpClient client = new HttpClient(connectionManager);
    Credentials creds = new UsernamePasswordCredentials(userName, password);
    client.getState().setCredentials(AuthScope.ANY, creds);
    client.setHostConfiguration(hostConfig);
    return client;
  }

  /**
   * Retrieve the file from distant URL to local temp file.
   *
   * @param url	document url
   *
   * @return	full path of local temp file
   *
   * @throws HttpException
   * @throws IOException
   */
  public String retrieveFile(String url) throws HttpException, IOException,
      URISyntaxException {
    URL formattedUrl = new URL(url);
    HttpClient client = initConnection(formattedUrl);

    logger.log(Level.INFO, MessageUtil.getMessage("info.webdav.locking") +
        ' ' + encodeUrl(url));
    //Let's lock the file
    LockMethod lockMethod = new LockMethod(encodeUrl(url),
        Scope.EXCLUSIVE, Type.WRITE,
        userName, 600000l, false);
    client.executeMethod(lockMethod);
    if (lockMethod.succeeded()) {
      lockToken = lockMethod.getLockToken();
    } else {
      throw new IOException(MessageUtil.getMessage("error.webdav.locking") 
          + ' ' + lockMethod.getStatusCode() + " - "
          + lockMethod.getStatusText());
    }
    logger.log(Level.INFO, MessageUtil.getMessage("info.webdav.locked") + ' ' 
        + lockToken);
    GetMethod method = new GetMethod();
    method.setURI(new URI(url, false));
    client.executeMethod(method);
    if (method.getStatusCode() != 200) {
      throw new IOException(MessageUtil.getMessage("error.get.remote.file") +
          +' ' + method.getStatusCode() + " - " + method.getStatusText());
    }

    String fileName = formattedUrl.getFile();
    fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
    fileName = fileName.replace(' ', '_');
    InputStream is = method.getResponseBodyAsStream();
    File tempDir = new File(System.getProperty("java.io.tmpdir"), "silver-" +
        System.currentTimeMillis());
    tempDir.mkdirs();
    File tmpFile = new File(tempDir, fileName);
    FileOutputStream fos = new FileOutputStream(tmpFile);
    int data = is.read();
    while (data != -1) {
      fos.write(data);
      data = is.read();
    }
    fos.close();
    logger.log(Level.INFO, MessageUtil.getMessage(
        "info.webdav.file.locally.saved") + ' ' + tmpFile.getAbsolutePath());
    return tmpFile.getAbsolutePath();
  }

  /**
   * Push back file into remote location using webdav.
   *
   * @param tmpFilePath			full path of local temp file
   * @param url					remote url
   *
   * @throws HttpException
   * @throws IOException
   */
  public void pushFile(String tmpFilePath, String url) throws HttpException,
      IOException,
      MalformedURLException,
      UnsupportedEncodingException,
      URISyntaxException {
    /*
     * Build URL object to extract host
     */
    URL formattedUrl = new URL(url);
    HttpClient client = initConnection(formattedUrl);
    /*
     * Checks if file still exists
     */
    GetMethod method = new GetMethod();
    method.setURI(new URI(url, false));
    client.executeMethod(method);

    if (method.getStatusCode() == 200) {
      logger.log(Level.INFO, MessageUtil.getMessage("info.webdav.unlocking") +
          ' ' + encodeUrl(url));
      //Let's lock the file
      UnLockMethod unlockMethod = new UnLockMethod(encodeUrl(url), lockToken);
      client.executeMethod(unlockMethod);
      if (unlockMethod.getStatusCode() != 200 && unlockMethod.getStatusCode() !=
          204) {
        logger.log(Level.INFO,
            MessageUtil.getMessage("error.webdav.unlocking") + ' ' + unlockMethod.
            getStatusCode());
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
      URI uri = new URI(url, false);
      PutMethod putMethod = new PutMethod(uri.getEscapedURI());
      logger.log(Level.INFO, MessageUtil.getMessage("info.webdav.put") +
          ' ' + tmpFilePath);
      File file = new File(tmpFilePath);
      InputStream is = new FileInputStream(file);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int data = is.read();
      while (data != -1) {
        baos.write(data);
        data = is.read();
      }
      byte[] bytes = baos.toByteArray();
      RequestEntity requestEntity = new ByteArrayRequestEntity(bytes);
      putMethod.setRequestEntity(requestEntity);
      client.executeMethod(putMethod);
      if (putMethod.succeeded()) {
        logger.log(Level.INFO, MessageUtil.getMessage("info.file.updated"));
        // delete temp file
        file.delete();
        file.getParentFile().delete();
        logger.log(Level.INFO, MessageUtil.getMessage("info.file.deleted"));
        MessageDisplayer.displayMessage(MessageUtil.getMessage("info.ok"));
      } else {
        throw new IOException(MessageUtil.getMessage("error.put.remote.file") +
            " - " + putMethod.getStatusCode() + " - " +
            putMethod.getStatusText());
      }
    } else {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.remote.file"));
      throw new IOException(MessageUtil.getMessage("error.remote.file"));
    }
  }

  public static String encodeUrl(String url) throws MalformedURLException,
      UnsupportedEncodingException,
      URISyntaxException {
    int count = 0;
    StringTokenizer tokenizer = new StringTokenizer(url, "/", true);
    StringBuilder buffer = new StringBuilder();
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      if (count < 4 || "/".equals(token)) {
        buffer.append(token);
      } else {
        buffer.append(URLEncoder.encode(token, "UTF-8"));
      }
      count++;
    }
    String resultingUrl = buffer.toString();
    return resultingUrl.replace('+', ' ').replaceAll(" ", "%20");
  }
}
