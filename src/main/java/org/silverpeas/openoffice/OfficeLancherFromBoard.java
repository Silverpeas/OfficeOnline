package org.silverpeas.openoffice;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.httpclient.HttpException;
import org.silverpeas.openoffice.util.FinderFactory;
import org.silverpeas.openoffice.util.MessageDisplayer;
import org.silverpeas.openoffice.util.MessageUtil;
import org.silverpeas.openoffice.util.MsOfficeType;
import org.silverpeas.openoffice.util.OsEnum;
import org.silverpeas.openoffice.util.UrlExtractor;
import org.silverpeas.openoffice.windows.FileWebDavAccessManager;
import org.silverpeas.openoffice.windows.MsOfficeVersion;

public class OfficeLancherFromBoard implements ClipboardOwner {

	static final Logger logger = Logger.getLogger(OfficeLauncher.class.getName());
	
	private Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();	
	private static final MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();
	 

	public void run() {		
		Transferable trans = sysClip.getContents(this);
		regainOwnership(trans);		
		logger.log(Level.INFO, "Listening to board...");		
		
		while (true) {
			try {
				Thread.sleep(250);				
			} catch (InterruptedException ex) {
				logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"), ex);
				MessageDisplayer.displayError(ex);
			}
		}		
	}

	public void lostOwnership(Clipboard c, Transferable t) {		
		try {
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"), ex);
		    MessageDisplayer.displayError(ex);
		}
		
		Transferable contents = sysClip.getContents(this);

		if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			processContents(contents);
			regainOwnership(contents);
		}
	}

	public static int processContents(Transferable t) {
		try {
			String url = (String) t.getTransferData(DataFlavor.stringFlavor);
			logger.log(Level.INFO, "Processing: " + url);						
			if (url.contains("silverpeas") && url.contains("jackrabbit") && url.contains("webdav")) {				
				MsOfficeType type = getContentType(UrlExtractor.decodeUrl(url));				
				OfficeFinder finder = FinderFactory.getFinder(type);
				if (finder.isMicrosoftOffice() && (OsEnum.getOS() == OsEnum.WINDOWS_XP || (OsEnum.isWindows() && MsOfficeVersion.isOldOffice(type)))) {
				        url = url.replace("/repository/", "/repository2000/");
				}
				AuthenticationInfo authInfo = null; //TODO : extract from url parameters
				switch (type) {
			      case EXCEL:
			        return launch(type, finder.findSpreadsheet(), url, false, authInfo);
			      case POWERPOINT:
			        return launch(type, finder.findPresentation(), url, false, authInfo);
			      case WORD:
			        return launch(type, finder.findWordEditor(), url, false, authInfo);
			      case NONE:
			      default:
			        return launch(type, finder.findOther(), url, false, authInfo);
			    }
			}
		} catch (IOException ex) {
	      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"), ex);
	      MessageDisplayer.displayError(ex);
	    } catch (InterruptedException ex) {
	      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"),
	          ex);
	      MessageDisplayer.displayError(ex);
	    } catch (Throwable ex) {
	      logger.log(Level.SEVERE, MessageUtil.getMessage("error.message.general"),
	          ex);
	      MessageDisplayer.displayError(ex);
	    } 
		
		return 0;
	}
	
	protected static MsOfficeType getContentType(String url) throws MalformedURLException {
	    String fileName = new URL(url).getFile();
	    String contentType = mimeTypes.getContentType(fileName.toLowerCase());
	    return MsOfficeType.valueOfMimeType(contentType);
	}
	
	/**
	   * Launch document edition
	   *
	   * @param path path to editor
	   * @param url document url
	   * @param modeDisconnected disconnected mode.
	   * @param auth authentication info
	   * @return status
	   * @throws IOException
	   * @throws InterruptedException
	   */
	  protected static int launch(MsOfficeType type, String path, String url, boolean modeDisconnected,
	      AuthenticationInfo auth) throws IOException, InterruptedException {
	    logger.log(Level.INFO, "The path: {0}", path);
	    logger.log(Level.INFO, "The url: {0}", url);
	    if (modeDisconnected) {
	      try {
	        String webdavUrl = url;
	        final FileWebDavAccessManager webdavAccessManager = new FileWebDavAccessManager(auth);
	        if ('"' == url.charAt(0)) {
	          webdavUrl = url.substring(1, url.length() - 1);
	        }
	        String tmpFilePath = webdavAccessManager.retrieveFile(webdavUrl);
	        logger.log(Level.INFO, "The exact exec line: {0} {1}", new Object[]{path, tmpFilePath});	        
	        Process process = Runtime.getRuntime().exec(path + ' ' + tmpFilePath);	        
	        process.waitFor();
	        webdavAccessManager.pushFile(tmpFilePath, url);
	        MessageDisplayer.displayMessage(MessageUtil.getMessage("info.ok"));
	        return 0;
	      } catch (HttpException ex) {
	        logger.log(Level.SEVERE, null, ex);
	        throw new IOException(ex);
	      } catch (IOException ex) {
	        logger.log(Level.SEVERE, null, ex);
	        throw ex;
	      }
	    } else {
	      // Standard mode : just open it
	      logger.log(Level.INFO, "The exact exec line: {0} {1}", new Object[]{path, url});
	      Process process = Runtime.getRuntime().exec(path + ' ' + url);
	      return process.waitFor();
	    }
	  }

	private void regainOwnership(Transferable t) {
		sysClip.setContents(t, this);
	}	

	public static void main(String[] args) {
		OfficeLancherFromBoard b = new OfficeLancherFromBoard();		
		b.run();		
	}
}