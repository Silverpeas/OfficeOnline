/**
 * Copyright (C) 2000 - 2009 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
