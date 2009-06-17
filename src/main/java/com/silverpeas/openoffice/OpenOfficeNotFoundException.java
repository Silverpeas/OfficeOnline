/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

/**
 *
 * @author Emmanuel Hugonnet
 */
public class OpenOfficeNotFoundException extends OfficeNotFoundException {

  private static final long serialVersionUID = 10l;

  public OpenOfficeNotFoundException() {
    super();
  }

  public OpenOfficeNotFoundException(String message) {
    super(message);
  }

  public OpenOfficeNotFoundException(Throwable cause) {
    super(cause);
  }

  public OpenOfficeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
