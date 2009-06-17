/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.silverpeas.openoffice.util;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class DirectoryFilter implements FileFilter {

  public boolean accept(File file) {
    return file.isDirectory();
  }

}
