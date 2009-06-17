/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

import com.silverpeas.openoffice.windows.FileWebDavAccessManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.silverpeas.openoffice.util.FinderFactory;
import com.silverpeas.openoffice.util.MsOfficeType;
import com.silverpeas.openoffice.util.OsEnum;
import org.apache.commons.httpclient.HttpException;

/**
 * 
 * @author Emmanuel Hugonnet
 */
public class OfficeLauncher {

  static Logger logger = Logger.getLogger(OfficeLauncher.class.getName());

  /*
   * If user is under Windows vista and use MS Office 2007.
   * Disconnected mode must be activated :
   *
   * 1) download file using webdav to local temp directory
   * 2) open it
   * 3) after close, send it back to silverpeas, still using webdav
   * 4) delete temp file locally
   *
   */
  public static int launch(MsOfficeType type, String url,
      AuthenticationInfo authInfo) throws IOException,
      InterruptedException, OfficeNotFoundException {
    OfficeFinder finder = FinderFactory.getFinder(type);
    boolean modeDisconnected = (OsEnum.getOS(System.getProperty("os.name")) ==
        OsEnum.WINDOWS_VISTA) && (finder.isMicrosoftOffice2007());
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
   *
   * @param path path to editor
   * @param url document url
   * @param modeDisconnected disconnected mode (used under vista + MS Office 2007)
   * @param auth authentication info
   *
   * @return status
   *
   * @throws IOException
   * @throws InterruptedException
   */
  public static int launch(String path, String url, boolean modeDisconnected,
      AuthenticationInfo auth)
      throws IOException, InterruptedException {
    logger.log(Level.INFO, "The path: " + path);
    logger.log(Level.INFO, "The url: " + url);
    logger.log(Level.INFO, "The command line: " + path + ' ' + url);

    /*
     * 1) download file using webdav to local temp directory
     * 2) open it
     * 3) after close, send it back to silverpeas, still using webdav
     * 4) delete temp file locally
     */
    if (modeDisconnected) {
      try {
        FileWebDavAccessManager webdavAccessManager =
            new FileWebDavAccessManager(auth);
        url = url.substring(1, url.length() - 1);
        String tmpFilePath = webdavAccessManager.retrieveFile(url);
        Process process =
            Runtime.getRuntime().exec(path + ' ' + tmpFilePath);
        process.waitFor();
        webdavAccessManager.pushFile(tmpFilePath, url);
        return 0;
      } catch (HttpException ex) {
        logger.log(Level.SEVERE, null, ex);
        throw new IOException(ex);
      } catch (URISyntaxException ex) {
        logger.log(Level.SEVERE, null, ex);
        throw new IOException(ex);
      }
    } else {
      //Standard mode : just open it
      Process process = Runtime.getRuntime().exec(path + ' ' + url);
      return process.waitFor();
    }
  }
}
