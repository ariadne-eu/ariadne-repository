package org.ariadne_eu.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ariadne_eu.content.retrieve.RetrieveContentFactory;
import org.ariadne_eu.utils.Utilities;

public class DownloadResourceServlet extends HttpServlet {

	private Logger log = Logger.getLogger(DownloadResourceServlet.class);

	public DownloadResourceServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String identifier = Utilities.escape(request.getParameter("objectid"));
		try {

			String mimetype = "application/octet-stream";

			log.info("Downloading object: " + identifier);
			
			DataHandler dataHandler = RetrieveContentFactory.retrieveContent(identifier);

			
			String fileName = RetrieveContentFactory.retrieveFileName(identifier);
			

			log.info("Datahandler is: " + dataHandler );

			response.setContentType(mimetype);
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName.replaceAll(" ", "_"));

			final BufferedInputStream input = new BufferedInputStream(dataHandler.getInputStream());
			final BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
			final int BUFFER_SIZE = 1024 * 4;
			final byte[] buffer = new byte[BUFFER_SIZE];

			while (true) {
				final int count = input.read(buffer, 0, BUFFER_SIZE);
				if (-1 == count) {
					break;
				}
				output.write(buffer, 0, count);
			}
			output.flush();

			dataHandler.getInputStream().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

	}

}
