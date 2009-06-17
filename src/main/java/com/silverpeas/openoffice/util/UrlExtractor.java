/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import com.silverpeas.openoffice.Launcher;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ehsavoie
 */
public class UrlExtractor {

  /**
   * Decode the encoded URL
   * @param encodedUrl the url encoded.
   * @return the url decoded.
   */
  public static String decodeUrl(String encodedUrl) {
    String url = encodedUrl;
    try {
      url = URLDecoder.decode(encodedUrl, "UTF-8");
      Logger.getLogger(UrlExtractor.class.getName()).log(Level.INFO,
          "URL decoded: " + url);
    } catch (IOException ex) {
      Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE,
          "Error decoding the URL", ex);
    }
    return url;
  }

  /**
   * Escape the rl to manage whitespaces.
   * If we are on Windows the url is surrounded with quotes.
   * If we are on Unix the whitespaces are escaped with %20.
   * @param url the url with whitespaces.
   * @return the url with whitespaces escaped.
   */
  public static String escapeUrl(String url) {
    OsEnum os = OsEnum.getOS(System.getProperty("os.name"));
    switch (os) {
      case WINDOWS_XP:
      case WINDOWS_VISTA:
        return '"' + url + '"';
      case LINUX:
      case MAC_OSX:
        return url.replaceAll(" ", "%20");
      default:
        return url.replaceAll(" ", "%20");
    }
  }

  /**
   * Decode and espace whitespaces from the url.
   * @param encodedUrl the genuine encoded url.
   * @return the url for the command line.
   */
  public static String extractUrl(String encodedUrl) {
    String decodedUrl = decodeUrl(encodedUrl);
    return escapeUrl(decodedUrl);
  }
}
