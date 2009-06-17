/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class OpenOfficeLauncher {

  static Logger logger = Logger.getLogger(OpenOfficeLauncher.class.getName());

  public static int launch(String path, String url)
      throws IOException, InterruptedException {
    Process process = Runtime.getRuntime().exec('"' + path + "\" " + url);
    return process.waitFor();
  }
}
