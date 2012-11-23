package org.ariadne_eu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

import org.ariadne.config.PropertiesManager;
import org.ariadne.util.IOUtilsv2;
import org.ariadne_eu.utils.config.RepositoryConstants;
import org.ariadne_eu.utils.lucene.reindex.ReIndexFSImpl;

public class IndexFsOffline {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		final PropertiesManager propmanager = new PropertiesManager();
		if(args.length < 1) {
			propmanager.init("reindex.properties");
		} else if(args.length == 1) {
			propmanager.init(args[0]);	
		} else {
			System.out.println("Usage : ");
			System.exit(0);
		}

		RepositoryConstants.init(RepositoryConstants.class.getCanonicalName());
		PropertiesManager.getInstance().init(propmanager.getProperty("repo.propertiesfile"));

		String newIndexDir = propmanager.getProperty("index.tempfolder");
		File newdir = new File(newIndexDir);
		newdir.mkdir();
		String oldIndexDir = PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR); 
		PropertiesManager.getInstance().get$properties().put(RepositoryConstants.getInstance().SR_LUCENE_INDEXDIR, newIndexDir);

		ReIndexFSImpl reindex = new ReIndexFSImpl();
		reindex.reIndexMetadata();

		boolean doSwitch = false;
		if(propmanager.getProperty("repo.switch").trim().equalsIgnoreCase("true")) doSwitch = true;

		if(doSwitch) {
			File destDir = new File(oldIndexDir);
			File oldDir = new File(destDir.getParent() + File.separator + destDir.getName() + ".old");
			if(oldDir.exists())IOUtilsv2.deleteDirectory(oldDir);
			destDir.renameTo(oldDir);
			newdir.renameTo(destDir);

			String reloadUrl = propmanager.getProperty("repo.tomcat.reloadUrl");

			try {

				Authenticator.setDefault(new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						String username = propmanager.getProperty("repo.tomcat.username");
						String pwd = propmanager.getProperty("repo.tomcat.password");
						return new PasswordAuthentication (username, pwd.toCharArray());
					}
				});

				URL url = new URL(reloadUrl);
				URLConnection http = url.openConnection();
			}catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
