/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class OfficeNotFoundException extends Exception {

  private static final long serialVersionUID = 10l;

  public OfficeNotFoundException() {
    super();
  }

  public OfficeNotFoundException(String message) {
    super(message);
  }

  public OfficeNotFoundException(Throwable cause) {
    super(cause);
  }

  public OfficeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
