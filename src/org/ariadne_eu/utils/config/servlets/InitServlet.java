/**
 * 
 */
package org.ariadne_eu.utils.config.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.minor.lucene.core.searcher.MemoryReaderManagement;

import org.ariadne.config.Constants;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.metadata.insert.InsertMetadataFactory;
import org.ariadne_eu.metadata.query.QueryMetadataFactory;
import org.ariadne_eu.metadata.query.language.TranslateLanguage;
import org.ariadne_eu.service.RFMImplementation;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * @author gonzalo
 * 
 */
public class InitServlet extends HttpServlet {
	
	protected static String dataDir = "";
	
	public final String CONSTANTS_INIT_PARAMETER = "constantsClass";
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {

	}
	
	public static void initializeServices() {
        TranslateLanguage.initialize();
        InsertMetadataFactory.initialize();
        QueryMetadataFactory.initialize();
        
    }
	
	public static void initializePropertiesManager(){
		try {
			PropertiesManager.getInstance().init();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void init() throws ServletException {
		
		try {
			String fileName = getServletContext().getInitParameter(CONSTANTS_INIT_PARAMETER);
			Constants.init(fileName);
		}catch(Exception e) {
			throw new ServletException(e.getMessage());
		}
		
		try {
			PropertiesManager.getInstance().setPropertiesFile(getServletContext().getRealPath("install")+ File.separator + "ariadne.properties");
			String basePath = getServletContext().getRealPath("");
			System.setProperty("basePath", basePath);
			RFMImplementation.getIds();
			if (PropertiesManager.getInstance().getPropertiesFile().exists()){
				PropertiesManager.getInstance().init();
				boolean inMemory = new Boolean(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INMEMORY));
				if (inMemory)
					MemoryReaderManagement.initialize();
			}
			
			
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}

	}

}
