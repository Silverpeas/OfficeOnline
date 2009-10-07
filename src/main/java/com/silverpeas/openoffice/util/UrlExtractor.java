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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ehsavoie
 */
public class UrlExtractor {

  static Logger logger = Logger.getLogger(UrlExtractor.class.getName());

  /**
   * Decode the encoded URL
   * @param encodedUrl the url encoded.
   * @return the url decoded.
   */
  public static String decodeUrl(String encodedUrl) {
    String url = encodedUrl;
    try {
      url = URLDecoder.decode(encodedUrl, "UTF-8");
      logger.log(Level.INFO,
          MessageUtil.getMessage("info.url.decoded") + url);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.decoding.url"), ex);
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

  /**
   * Decode the encoded path
   * @param encodedUrl the path encoded.
   * @return the path decoded.
   */
  public static String decodePath(String encodedPath) {
    String path = decodeUrl(encodedPath);
    return path.replace('/', File.separatorChar);
  }
}
