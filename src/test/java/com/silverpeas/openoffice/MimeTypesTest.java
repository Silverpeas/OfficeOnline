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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.openoffice;

import com.silverpeas.openoffice.util.MsOfficeType;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 *
 * @author ehugonnet
 */
public class MimeTypesTest {

  @Test
  public void testMsDoc() throws Exception {
    assertEquals(MsOfficeType.WORD, Launcher.getContentType("file:///HelloWorld.doc"));
    assertEquals(MsOfficeType.WORD, Launcher.getContentType("file:///HelloWorld.DOC"));
    assertEquals(MsOfficeType.WORD, Launcher.getContentType("file:///hello world.doc"));
    assertEquals(MsOfficeType.WORD, Launcher.getContentType("file:///HelloWorld.dOcx"));
    assertEquals(MsOfficeType.WORD, Launcher.getContentType("file:///HelloWorld.docm"));
  }

  @Test
  public void testMsExcel() throws Exception {
    assertEquals(MsOfficeType.EXCEL, Launcher.getContentType("file:///HelloWorld.xls"));
    assertEquals(MsOfficeType.EXCEL, Launcher.getContentType("file:///HelloWorld.XLS"));
    assertEquals(MsOfficeType.EXCEL, Launcher.getContentType("file:///hello world.xls"));
     assertEquals(MsOfficeType.EXCEL, Launcher.getContentType("file:///HelloWorld.xLsx"));
  }

  @Test
  public void testMsPowerpoint() throws Exception {
    assertEquals(MsOfficeType.POWERPOINT, Launcher.getContentType("file:///HelloWorld.ppt"));
    assertEquals(MsOfficeType.POWERPOINT, Launcher.getContentType("file:///HelloWorld.PPT"));
    assertEquals(MsOfficeType.POWERPOINT, Launcher.getContentType("file:///hello world.PPtx"));
  }
}
