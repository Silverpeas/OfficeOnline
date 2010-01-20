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

import java.beans.PropertyChangeListener;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ehugonnet
 */
public class MonitoredInputStream extends FilterInputStream {

  private int nbread = 0;
  private int size = 0;
  private InputStreamMonitor monitor;

  public MonitoredInputStream(InputStream stream) {
    super(stream);
    this.monitor = new InputStreamMonitor();
    try {
      size = stream.available();
    } catch (IOException ioe) {
      size = 0;
    }
  }

  @Override
  public void close() throws IOException {
    super.close();
  }

  @Override
  public int read(byte[] b) throws IOException {
    int nr = in.read(b);
    if (nr > 0) {
      monitor.setProgress(nbread += nr);
    }
    return nr;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int nr = in.read(b, off, len);
    if (nr > 0) {
      monitor.setProgress(nbread += nr);
    }
    return nr;
  }

  @Override
  public synchronized void reset() throws IOException {
    in.reset();
    nbread = size - in.available();
    monitor.setProgress(nbread);
  }

  @Override
  public long skip(long n) throws IOException {
    long nr = in.skip(n);
    if (nr > 0) {
      monitor.setProgress(nbread += nr);
    }
    return nr;
  }

  @Override
  public int read() throws IOException {
    int c = in.read();
    if (c >= 0) {
      monitor.setProgress(++nbread);
    }
    return c;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    monitor.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    monitor.removePropertyChangeListener(listener);
  }
}
