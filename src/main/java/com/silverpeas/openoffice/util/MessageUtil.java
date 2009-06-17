/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import java.util.ResourceBundle;

/**
 *
 * @author ehugonnet
 */
public class MessageUtil {

  public static final ResourceBundle MESSAGES = ResourceBundle.getBundle(
      "com/silverpeas/openoffice/messages");

  public static String getMessage(String key) {
    return MESSAGES.getString(key);
  }
}
