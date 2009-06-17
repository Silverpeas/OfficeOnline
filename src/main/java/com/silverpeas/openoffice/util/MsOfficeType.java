/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

/**
 *
 * @author Emmanuel Hugonnet
 */
public enum MsOfficeType {

  NONE(""),
  EXCEL("application/vnd.ms-excel"),
  WORD("application/vnd.ms-word"),
  POWERPOINT("application/vnd.ms-powerpoint");
  private String contentType;

  private MsOfficeType(String contentType) {
    this.contentType = contentType;
  }

  public static MsOfficeType valueOfMimeType(String mimeType) {
    if (EXCEL.contentType.equals(mimeType)) {
      return EXCEL;
    }
    if (WORD.contentType.equals(mimeType)) {
      return WORD;
    }
    if (POWERPOINT.contentType.equals(mimeType)) {
      return POWERPOINT;
    }
    return NONE;
  }

  public boolean isMsOfficeCompatible() {
    return this != NONE;
  }
}
