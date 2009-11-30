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
