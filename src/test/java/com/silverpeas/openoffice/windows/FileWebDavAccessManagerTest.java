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

package com.silverpeas.openoffice.windows;

import junit.framework.TestCase;

/**
 *
 * @author ehugonnet
 */
public class FileWebDavAccessManagerTest extends TestCase {
    
    public FileWebDavAccessManagerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

  /**
   * Test of encodeUrl method, of class FileWebDavAccessManager.
   */
  public void testEncodeUrl() throws Exception {
    System.out.println("encodeUrl");
    String url = "http://localhost:8000/silverpeas/toto/Ceci est un &x@plé.doc";
    String expResult = "http://localhost:8000/silverpeas/toto/Ceci%20est%20un%20%26x%40pl%C3%A9.doc";
    String result = FileWebDavAccessManager.encodeUrl(url);
    assertEquals(expResult, result);
    String escapedUrl = "http://localhost:8000/silverpeas/toto/Ceci%20est%20un%20&x@plé.doc";
    assertEquals(escapedUrl.replaceAll("%20", " "), url);
    result = FileWebDavAccessManager.encodeUrl(escapedUrl);
    assertEquals(expResult, result);
  }

}
