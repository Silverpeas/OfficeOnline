/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class StreamReader extends Thread {

  static Logger logger = Logger.getLogger(UrlExtractor.class.getName());
  private InputStream is;
  private StringWriter sw;

  public StreamReader(InputStream is) {
    this.is = is;
    sw = new StringWriter();
  }

  @Override
  public void run() {
    try {
      int c;
      while ((c = is.read()) != -1) {
        sw.write(c);
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, MessageUtil.getMessage("error.reading.registry"),
          e);
    }
  }

  public String getResult() {
    return sw.toString();
  }
}
