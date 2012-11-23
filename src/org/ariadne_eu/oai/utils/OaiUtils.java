/*******************************************************************************
 * Copyright (c) 2008 Ariadne Foundation.
 * 
 * This file is part of Ariadne Harvester.
 * 
 * Ariadne Harvester is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Ariadne Harvester is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Ariadne Harvester.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package org.ariadne_eu.oai.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

public class OaiUtils {

	public static final String DEFAULT_ENCODING = "UTF-8";

//	private static Unmarshaller unmarshaller = null;

	private static Calendar starttime = null;

	public static Namespace OAIOAINS = Namespace.getNamespace("oai",
			"http://www.openarchives.org/OAI/2.0/");

	public static Namespace LOMLOMNS = Namespace.getNamespace("lom",
			"http://ltsc.ieee.org/xsd/LOM");

	public static Namespace OAINS = Namespace
			.getNamespace("http://www.openarchives.org/OAI/2.0/");

	public static Namespace LOMNS = Namespace
			.getNamespace("http://ltsc.ieee.org/xsd/LOM");

	private static final String LOMV1_0 = "LOMv1.0";

	public static Element newElement(String name, Element parent) {
		Element element = new Element(name,LOMNS);
		parent.addContent(element);
		return element;
	}

	public static Element addLangString(String language, String string, Element parent) {
		Element element = new Element("string",LOMNS);
		element.setAttribute("language", language);
		element.setText(string);
		parent.addContent(element);
		return element;
	}

	public static void addIdentifier(String catalogString, String entryString, Element parent) {
		if(catalogString != null && !catalogString.trim().equals("")) {
			Element catalog = new Element("catalog",LOMNS);
			catalog.setText(catalogString);
			parent.addContent(catalog);
		}

		Element entry = new Element("entry",LOMNS);
		entry.setText(entryString);
		parent.addContent(entry);
	}
	
	public static CDATA createVcard(String firstName, String lastName) {
		String fn = "FN:"+firstName +" "+ lastName;
		String n = "N:"+ lastName +";"+ firstName;
		return new CDATA("BEGIN:VCARD\n" + fn + "\n" + n + "\nVERSION:3.0\nEND:VCARD");
	}

	public static void addLomVocabularyItem(String valueString, Element parent) {
		addVocabularyItem(LOMV1_0, valueString, parent);
	}

	public static void addVocabularyItem(String sourceString, String valueString, Element parent) {
		if(sourceString != null && !sourceString.trim().equals("")) {
			Element source = new Element("source",LOMNS);
			source.setText(sourceString);
			parent.addContent(source);
		}

		Element value = new Element("value",LOMNS);
		value.setText(valueString);
		parent.addContent(value);
	}

	public static String parseLom2XmlstringNoXmlHeader(Element lom){
		XMLOutputter outputter = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setOmitDeclaration(true);
		outputter.setFormat(format);
		String output = outputter.outputString(lom);
		return output;
	}
	
	public static void begin() {
		starttime = new GregorianCalendar();
	}

	public static void end() {
		GregorianCalendar endtime = new GregorianCalendar();
		long difference = endtime.getTimeInMillis()
				- starttime.getTimeInMillis();
		int mins = (int) Math.floor(difference / 60000.0);
		double secs = (difference / 1000.0 - mins * 60.0);
		System.out.println(mins + " m " + secs + " s");
	}

	public static Document parseXmlString2Lom(String lomString)
			throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		return builder.build(new StringReader(lomString));
	}

//	private static void initXmlString2Lomparser() {
//		try {
//			JAXBContext jaxbContext = JAXBContext
//					.newInstance("org.ieee.ltsc.lom.jaxb.lomxml");
//			unmarshaller = jaxbContext.createUnmarshaller();
//			XmlString2LomparserInited = true;
//		} catch (JAXBException e) {
//			System.out.println(e.getMessage());
//		}
//	}

	public static String[] readN(int n, File file) {

		String[] buffer = new String[n];
		String[] result = null;
		int i = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line = reader.readLine();
			while (line != null) {
				if (!line.equals(""))
					buffer[i++ % n] = line;
				// if(i >= n) i = 0;
				line = reader.readLine();
			}

		} catch (IOException e) {
			// TODO: handle exception
		}
		// StringBuffer result = new StringBuffer();
		// if(i >= n){
		// for(int j = 0; j < n; j++){
		// result.append(buffer[ (i + j) % n ]).append("\n");
		// }
		// }
		// else{
		// for(int j = 0; j < i; j++){
		// result.append(buffer[j]).append("\n");
		// }
		// }

		if (i >= n) {
			result = new String[n];
			for (int j = 0; j < n; j++) {
				result[j] = buffer[(i + j) % n];
			}
		} else {
			result = new String[i];
			for (int j = 0; j < i; j++) {
				result[j] = buffer[j];
			}
		}

		return result;
	}

	// /**
	// * Write a String to a file
	// *
	// * @param inputString The String to write
	// * @param outputFileName The output file name/location
	// */
	// public void writeStringToFile(String inputString, String outputFileName)
	// {
	// try {
	// File result = new File(outputFileName);
	// FileWriter fw = new FileWriter(result);
	// BufferedWriter bw = new BufferedWriter(fw);
	// bw.write(inputString);
	// bw.close();
	// fw.close();
	// } catch (IOException e) {
	// String messageString = e.getMessage();
	// // Logger.getLogger(SamgiConstantsG.LOGGER).error(messageString, e);
	// }
	// 
	// }
	// 

	/**
	 * Write a String to a file, in a given encoding
	 * 
	 * @param inputText
	 *            The String to write
	 * @param outputFileName
	 *            The output file name/location
	 * @param encoding
	 *            The encoding to use for the file
	 * @throws IOException
	 */
	public static void writeStringToFileInEncoding(String inputText,
			String outputFileName, String encoding) throws IOException {
		// OPTION 1:
		try {
			File result = new File(outputFileName);
			FileOutputStream fos = new FileOutputStream(result);
			Writer out = new OutputStreamWriter(fos, encoding);
			out.write(inputText);
			out.close();
			fos.close();
		} catch (IOException e) {
			System.out.flush();
			throw e;
		}

		// OPTION 2:
		// try {
		// byte currentXMLBytes[] = inputText.getBytes(encoding);
		// ByteArrayInputStream byteArrayInputStream = new
		// ByteArrayInputStream(currentXMLBytes);
		// BufferedReader in = new BufferedReader(new
		// InputStreamReader(byteArrayInputStream, encoding));
		//
		// FileOutputStream fos = new FileOutputStream(outputFileName);
		// BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos,
		// encoding));
		//
		// String str;
		// while ((str = in.readLine()) != null) {
		// out.write(str, 0, str.length());
		// out.newLine();
		// out.flush();
		// }
		//
		// byteArrayInputStream.close();
		// fos.close();
		// in.close();
		// out.close();
		// } catch (UnsupportedEncodingException ex) {
		// System.err.println(ex);
		// System.err.println("ERROR: The encoding specified is wrong or not supported by this system");
		// } catch (IOException ex) {
		// System.out.flush();
		// System.err.println(ex);
		// }

	}

	// public void validateXMLString(String xml) throws SAXException{
	// SchemaFactory factory =
	// SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
	// //URL schemaLocation = new URL("http://fire.eun.org/xsd/lre/lre.xsd");
	// URL schemaLocation = new
	// URL("http://ltsc.ieee.org/xsd/lomv1.0/20040413/lom.xsd");
	// Schema schema = factory.newSchema(schemaLocation);
	// Validator validator = schema.newValidator();
	// Source source = new StreamSource(new
	// ByteArrayInputStream(xml.getBytes()));
	// // try {
	// validator.validate(source);
	// System.out.println( "lomxml is valid.");
	// // }
	// // catch (SAXException ex) {
	// // System.out.println("lomxml is not valid because ");
	// // System.out.println(ex.getMessage());
	// // }
	// }

	/**
	 * Write a String to a file, in UTF-8 encoding
	 * 
	 * @param inputText
	 *            The String to write
	 * @param outputFileName
	 *            The output file name/location
	 * @throws IOException
	 */
	public static void writeStringToFileInEncodingUTF8(String inputText,
			String outputFileName) throws IOException {
		writeStringToFileInEncoding(inputText, outputFileName, "UTF-8");
	}

	public static java.util.Date parseStringToDate(String dateString) {
		java.text.SimpleDateFormat dfparser;
		dateString = dateString.replaceAll("th", "");
		dateString = dateString.replaceAll("nd", "");
		dateString = dateString.replaceAll("\\n", " ");
		dateString = dateString.replaceAll("\\r", " ");
		dateString = dateString.replaceAll(
				System.getProperty("line.separator"), " ");
		// Replace 2 or more spaces by 1 space, because otherwise the parsing
		// will fail.
		dateString = dateString.replaceAll("\\s{2,}", " ");
		try {
			// The one used by the LOM Java API
			dfparser = new java.text.SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.US);
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			// The one used by java.util.Date toString method
			dfparser = new java.text.SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US);
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			dfparser = new java.text.SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			dfparser = new java.text.SimpleDateFormat("yyyy-MM-dd");
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			dfparser = new java.text.SimpleDateFormat("MMM dd, yyyy");
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			dfparser = new java.text.SimpleDateFormat("MM-dd.yy");
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			dfparser = new java.text.SimpleDateFormat("dd MMM yyyy");
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			dfparser = new java.text.SimpleDateFormat("dd.MM.yy");
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			dfparser = new java.text.SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		try {
			dfparser = new java.text.SimpleDateFormat("E M dd hh:mm:ss z yyyy");
			return dfparser.parse(dateString);
		} catch (ParseException e) {
			//
		}
		// If all attempts fail, just return null.
		return null;
	}

	private static Validator validator = null;

	private static void initLomValidator() {
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		URL schemaLocation;
		try {
			schemaLocation = new URL("http://ltsc.ieee.org/xsd/lomv1.0/lom.xsd");
			Schema schema = factory.newSchema(schemaLocation);
			validator = schema.newValidator();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String validateLomXml(String xml) {
		if (validator == null)
			initLomValidator();
		Source source = new StreamSource(new StringReader(xml));
		try {
			validator.validate(source);
			return "";
		} catch (SAXException ex) {
			return "Document is not valid because " + ex.getMessage();
		} catch (IOException e) {
			return "Document is not valid because " + e.getMessage();
		}
	}

	public static String calcUntil(Calendar untilDate, String granularity) {
		String pattern = "";
		if (granularity.equals("YYYY-MM-DDThh:mm:ssZ")) {
			untilDate.add(Calendar.SECOND, -1);
			pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		} else {
			untilDate.add(Calendar.DATE, -1);
			pattern = "yyyy-MM-dd";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		return sdf.format(untilDate.getTime());

	}

	public static String calcFrom(String fromDate, String granularity)
			throws ParseException {
		String pattern = "";
		if (granularity.equals("YYYY-MM-DDThh:mm:ssZ")) {
			pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		} else {
			pattern = "yyyy-MM-dd";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		return sdf.format(sdf.parse(fromDate));

	}

	public static Vector<String> getSets(String setString) {
		StringTokenizer tokenizer = new StringTokenizer(setString, ";");
		Vector<String> sets = new Vector<String>();
		while (tokenizer.hasMoreTokens()) {
			sets.add(tokenizer.nextToken());
		}
		if (sets.size() == 0)
			sets.add("");
		return sets;
	}

	public static Calendar getCurrentTime() {
		Calendar utc = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("UTC"));
		return utc;
	}

}
