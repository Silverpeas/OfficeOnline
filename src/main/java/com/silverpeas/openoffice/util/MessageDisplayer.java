/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import javax.swing.JOptionPane;

/**
 *
 * @author ehugonnet
 */
public class MessageDisplayer {

  public static void displayMessage(String message) {
    JOptionPane.showMessageDialog(null, message,
              MessageUtil.getMessage("info.title"), JOptionPane.INFORMATION_MESSAGE);
    
  }

  public static void displayError(Throwable t) {
    JOptionPane.showMessageDialog(null, t.getMessage(),
              MessageUtil.getMessage("error.title"), JOptionPane.ERROR_MESSAGE);
  }
}
