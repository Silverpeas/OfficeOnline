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
 * FLOSS exception.  You should have received a copy of the text describing
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

package com.silverpeas.openoffice.util;

import javax.swing.JOptionPane;

/**
 * Simple utility class to display messages graphically.
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
  
  public static String displayPromptPassword() {
    return JOptionPane.showInputDialog(null, MessageUtil.getMessage("info.missing.password.label"),
        MessageUtil.getMessage("info.missing.password.title"), JOptionPane.PLAIN_MESSAGE);
  }
}
