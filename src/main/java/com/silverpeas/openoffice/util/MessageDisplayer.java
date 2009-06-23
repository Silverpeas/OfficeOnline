/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author ehugonnet
 */
public class MessageDisplayer {

  static Logger logger = Logger.getLogger(
      MessageDisplayer.class.getName());

  static {
    try {
      try {
        if (OsEnum.getOS() == OsEnum.WINDOWS_VISTA || OsEnum.getOS() == OsEnum.WINDOWS_XP) {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
      } catch (ClassNotFoundException ex) {
        logger.log(Level.INFO, null, ex);
      } catch (InstantiationException ex) {
        logger.log(Level.INFO, null, ex);
      } catch (IllegalAccessException ex) {
        logger.log(Level.INFO, null, ex);
      }
    } catch (UnsupportedLookAndFeelException ex) {
      logger.log(Level.INFO, "Unable to load native look and feel");
    }
  }

  public static void displayMessage(String message) {
    JOptionPane.showMessageDialog(null, message,
        MessageUtil.getMessage("info.title"), JOptionPane.INFORMATION_MESSAGE);
  }

  public static void displayError(Throwable t) {
    JOptionPane.showMessageDialog(null, t.getMessage(),
        MessageUtil.getMessage("error.title"), JOptionPane.ERROR_MESSAGE);
  }
}
