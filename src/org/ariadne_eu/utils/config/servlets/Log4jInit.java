package org.ariadne_eu.utils.config.servlets;

import java.io.File;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

public class Log4jInit extends HttpServlet {

	protected static String logDir = "";
	protected static String logFile = "";
	protected static String prefix = "";
	protected static String file = "";

	public void init() {
		reloadLogging();
	}

	public void reloadLogging(){
		logDir = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_LOG4J_DIR);
		logFile = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_LOG4J_FILENAME);
		if(!logDir.equals("")){
			System.setProperty("logdir", logDir);
		}
		if(!logFile.equals("")){
			System.setProperty("logfile", logFile);
		}
		else {
			System.setProperty("logfile", "default_log");
		}
		prefix =  getServletContext().getRealPath("install");
		file = getInitParameter("log4j-init-file");
		//  if the log4j-init-file is not set, then no point in trying
		if(file != null) {
			PropertyConfigurator.configure(prefix + File.separator + file);
		}
	}

	public static void reloadLive() {
		logDir = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_LOG4J_DIR);
		logFile = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_LOG4J_FILENAME);
		if (!logDir.equals("")) {
			System.setProperty("logdir", logDir);
		}
		if (!logFile.equals("")) {
			System.setProperty("logfile", logFile);
		}else {
			System.setProperty("logfile", "default_log");
		}
		// if the log4j-init-file is not set, then no point in trying
		if (file != null) {
			PropertyConfigurator.configure(prefix + File.separator + file);
		}
	}

	public
	void doGet(HttpServletRequest req, HttpServletResponse res) {
	}
}