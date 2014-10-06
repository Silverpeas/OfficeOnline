package org.silverpeas.openoffice.util;

import java.net.MalformedURLException;

/**
 * Extracts the arguments of the application and exposes them.
 * @author mmoquillon
 */
public class ApplicationArguments {

  private String encodedUrl;
  private String url;
  private String basePath;
  private boolean disconnectedMode;
  private MsOfficeType contentType;
  private String token;
  private String login;

  protected ApplicationArguments() {

  }

  public static ApplicationArguments extract(String[] args) throws MalformedURLException {
    ApplicationArguments arguments = new ApplicationArguments();
    // the URL of the document to open
    arguments.encodedUrl = args[0];
    arguments.url = UrlExtractor.extractUrl(arguments.encodedUrl);
    // the base path of the Office Suite installation.
    if (args[1] != null && !"".equals(args[1].trim())) {
      arguments.basePath = UrlExtractor.decodePath(args[1]);
    }
    // the user login
    arguments.login = args[2];
    // the authentication token
    arguments.token = args[3];
    // is the file has to be opened in the disconnected mode?
    arguments.disconnectedMode = getBooleanValue(args[4]);
    // the content type of the document
    arguments.contentType =
        ContentTypeUtil.getContentType(UrlExtractor.decodeUrl(arguments.encodedUrl));

    return arguments;
  }

  /**
   * The encoded form of the document URL as passed as argument to the program.
   * @return the encoded form of the document URL.
   */
  public String getEncodedUrl() {
    return encodedUrl;
  }

  /**
   * The webDAV URL at which the document to open is located.
   * @return the URL of the document to access in webDAV.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Gets the base path from which the Office Suite will be seek.
   * @return the base path from which the Office Suite can be found.
   */
  public String getBasePath() {
    return basePath;
  }

  /**
   * Is the file to be opened in disconnected mode?
   * <p>
   * In disconnected mode, the file is first downloaded from its webDAV location to the local
   * temporary directory, then it is opened by the Office Suite. Once closed, the document is
   * sent back to Silverpeas by webDAV and then it is deleted from its temporary location.
   * </p>
   * @return true if the document has to be opened in disconnected mode.
   */
  public boolean isDisconnectedMode() {
    return disconnectedMode;
  }

  /**
   * Gets the content type of the document (word editor, presentation editor, etc.)
   * @return the content type of the document.
   */
  public MsOfficeType getContentType() {
    return contentType;
  }

  /**
   * Gets the authentication token to use to access the document by webDAV. Without this token or
   * with an invalid token, the access will be forbidden by Silverpeas.
   * @return an authentication token.
   */
  public String getToken() {
    return token;
  }

  /**
   * Gets the login of the user asking the access to the document.
   * @return the login of the user.
   */
  public String getLogin() {
    return login;
  }

  private static boolean getBooleanValue(final String expression) {
    return "true".equalsIgnoreCase(expression) || "yes".equalsIgnoreCase(expression) ||
        "y".equalsIgnoreCase(expression) || "oui".equalsIgnoreCase(expression) ||
        "1".equalsIgnoreCase(expression);
  }
}
