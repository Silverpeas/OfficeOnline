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
package com.silverpeas.openoffice.windows.webdav;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import com.silverpeas.openoffice.util.MessageUtil;
import java.awt.Dimension;
import javax.swing.UIManager;

/**
 * @author ehugonnet
 */
public class UploadProgressBar extends JPanel implements PropertyChangeListener {

  private static final int FRAME_WIDTH = 350;
  private static final int FRAME_HEIGHT = 200;
  private JProgressBar progressBar;
  private JLabel taskOutput;
  private JLabel messageLabel;
  private boolean display = false;
  private JFrame frame = null;

  public UploadProgressBar() {
    super(new BorderLayout());
    progressBar = new JProgressBar(0, 100);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);

    taskOutput = new JLabel();
    messageLabel = new JLabel();
    JPanel progressPanel = new JPanel(new BorderLayout());
    progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    progressPanel.add(messageLabel, BorderLayout.PAGE_START);
    progressPanel.add(progressBar, BorderLayout.CENTER);
    progressPanel.add(taskOutput, BorderLayout.PAGE_END);
    JPanel iconPanel = new JPanel(new BorderLayout());
    iconPanel.add(new JLabel(UIManager.getIcon("OptionPane.informationIcon")), BorderLayout.CENTER);
    add(iconPanel, BorderLayout.WEST);
    add(progressPanel, BorderLayout.CENTER);
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
  }

  /**
   * Defines the maximum value for the progress bar.
   * @param max the maximum value for the progress bar.
   */
  public void setMaximum(int max) {
    progressBar.setMaximum(max);
  }

  /**
   * Defines the minimum value for the progress bar.
   * @param min the minimum value for the progress bar.
   */
  public void setMinimum(int min) {
    progressBar.setMinimum(min);
  }

  /**
   * Defines the message to be displayed before the progress bar.
   * @param message the message to be displayed before the progress bar.
   */
  public void setMessage(String message) {
    messageLabel.setText(message);
  }

  /**
   * Invoked when task's progress property changes.
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if ("progress".equals(evt.getPropertyName())) {
      if (!display) {
        display = true;
        start();
      }
      int progress = (Integer) evt.getNewValue();
      progressBar.setValue(progress);
      taskOutput.setText(String.format(MessageUtil.getMessage("upload.file.task"),
              100 * progressBar.getPercentComplete()));
    }
  }

  /**
   * Display the upload progress bar.
   */
  private void start() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGUI();
      }
    });
  }

  /**
   * Close the upload progress bar.
   */
  public void close() {
    frame.setVisible(false);
    frame.dispose();
  }

  /**
   * Create the GUI and show it. As with all GUI code, this must run on the event-dispatching
   * thread.
   */
  private void createAndShowGUI() {
    // Create and set up the window.
    frame = new JFrame(MessageUtil.getMessage("upload.file.title"));
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    this.setOpaque(true); // content panes must be opaque
    frame.setContentPane(this);
    // Center the frame
    Dimension screenDimension = getToolkit().getScreenSize();
    frame.setLocation((screenDimension.width - FRAME_WIDTH) / 2,
            (screenDimension.height - FRAME_HEIGHT) / 2);
    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }
}
