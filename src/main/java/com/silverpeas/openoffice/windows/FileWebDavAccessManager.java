package com.silverpeas.openoffice.windows;

import com.silverpeas.openoffice.*;
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
    logger.log(Level.INFO, "Locking file : " + encodeUrl(url));
    //Let's lock the file
    LockMethod lockMethod = new LockMethod(encodeUrl(url),
        Scope.EXCLUSIVE, Type.WRITE,
        userName, 600000l, false);
    client.executeMethod(lockMethod);
    if (lockMethod.succeeded()) {
      lockToken = lockMethod.getLockToken();
    }
    logger.log(Level.INFO, "File locked " + lockToken);

    GetMethod method = new GetMethod();
    method.setURI(new URI(url, false));
    client.executeMethod(method);

    String fileName = formattedUrl.getFile();
    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
    String prefix =
        fileName.substring(0, fileName.lastIndexOf(".")).replace(' ', '_');
    String suffix = fileName.substring(fileName.lastIndexOf("."));

    InputStream is = method.getResponseBodyAsStream();
    File tmpFile = File.createTempFile(prefix, suffix);
    FileOutputStream fos = new FileOutputStream(tmpFile);
    int data = is.read();
    while (data != -1) {
      fos.write(data);
      data = is.read();
    }
    fos.close();
    logger.log(Level.INFO, "File saved locally: " + tmpFile.getAbsolutePath());
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
      logger.log(Level.INFO, "Unlocking file : " + encodeUrl(url));
      //Let's lock the file
      UnLockMethod unlockMethod = new UnLockMethod(encodeUrl(url), lockToken);
      client.executeMethod(unlockMethod);
      if(unlockMethod.getStatusCode() != 200 && unlockMethod.getStatusCode() != 204) {
        logger.log(Level.INFO, "Error unlocking file " + unlockMethod.getStatusCode());
      }
      try {
        unlockMethod.checkSuccess();
        logger.log(Level.INFO, "File unlocked");
      } catch (DavException ex) {
        logger.log(Level.SEVERE, null, ex);
      }
      URI uri = new URI(url, false);
      PutMethod putMethod = new PutMethod(uri.getEscapedURI());
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
      logger.log(Level.INFO, "File updated");
      // delete temp file
      file.delete();
      logger.log(Level.INFO, "Local file deleted");
    } else {
      logger.log(Level.SEVERE, "File doesn't exists anymore on server");
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
        buffer.append(URLEncoder.encode(token, "ISO-8859-1"));
      }
      count++;
    }
    String resultingUrl =  buffer.toString();
    return resultingUrl.replace('+', ' ').replaceAll(" ", "%20");
  }
}
