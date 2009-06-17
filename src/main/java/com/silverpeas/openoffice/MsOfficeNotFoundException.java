/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class MsOfficeNotFoundException extends Exception {

  private static final long serialVersionUID = 10l;

  public MsOfficeNotFoundException() {
    super();
  }

  public MsOfficeNotFoundException(String message) {
    super(message);
  }

  public MsOfficeNotFoundException(Throwable cause) {
    super(cause);
  }

  public MsOfficeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
