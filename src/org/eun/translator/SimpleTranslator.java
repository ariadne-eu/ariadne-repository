package org.eun.translator;
/*
Copyright (C) 2005  David Massart and Quentin Tremerie, European Schoolnet

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;



public class SimpleTranslator 
{		
	public static String THE_FORMAT =
        "http://fire.eun.org/xsd/strictLomResults-1.0" ; 
	private static Logger log =  Logger.getLogger(SimpleTranslator.class);
	
	public String transformQueryToLuceneQL(String query) throws Exception{
		log.debug("SimpleTranslator.convertQuery("+query+")");
        
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("S2QL2LuceneQL.xsl");
		
		TransformerFactory tfactory = TransformerFactory.newInstance();
		
		Object o = tfactory.newTransformer(new StreamSource(new BufferedInputStream(is)));
		
		Transformer transformer = (Transformer) o;
		
        // Create a Source from input string
        javax.xml.transform.Source xmlSource =
            new javax.xml.transform.stream.StreamSource(new StringReader( query )) ;
        log.debug("SimpleTranslator.convertQuery() : StreamSource CREATED") ;  
        
        // Create a result Stream
        // We use a ByteArrayOutputStream to enable utf-8 encoding        
        // instead of using a string writer
        String 						resultString	=	null;
        ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream() ;
        //StringWriter 				stringWriter	=	new StringWriter() ;
        javax.xml.transform.Result 	xmlResult 		=
            new javax.xml.transform.stream.StreamResult(baos);
        
        log.debug("SimpleTranslator.convertQuery() : StreamResult CREATED") ;
                   
        // Apply Transformation
        try 
        {
            transformer.transform(xmlSource, xmlResult);
            log.debug("SimpleTranslator.convertQuery() : TRANSFORMATION SUCCESSFULL") ;
        }
        catch (javax.xml.transform.TransformerException te) 
        {
        	log.debug("SimpleTranslator.convertQuery() : ERROR WHILE TRANSFORMATION !") ;
            te.printStackTrace() ;
            throw new Exception(te) ;
        }      
        
        // The ByteArrayOutputStream enables us to use the constructor
        // string(baos.toByteArray(), "utf-8") ; , 
        // which is not possible with stringWriter.toString() ;
        try 
        {
            //        resultString = stringWriter.toString() ;
            resultString = new String(baos.toByteArray(), "utf-8") ;
        } 
        catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace() ;
        }       
        
        // Replacing conflicting characters
        resultString = resultString.replaceAll("&lt;","<") ;
        resultString = resultString.replaceAll("&gt;",">") ;
                
        // Handle Results : add suffix to choose the right resultFormat        
        //String suffix = " and sto.result_type = '"+resultFormat+"' ";
        //log.debug("SimpleTranslator.convertQuery() : suffix : "+suffix) ;        
        //resultString+= suffix;   
        
        // Job Done
        return resultString;        
    }

	
	public static void main(String[] args) 
    {
		String sQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<s2ql xmlns=\"http://fire.eun.org/xsd/s2ql-2.0\" " +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
						"xsi:schemaLocation=\"http://fire.eun.org/xsd/s2ql-2.0.xsd\">" +
						// "<language>fr</language>" +
						"<keyword>test</keyword>" +
						"<ageRange>" +
						"<maxAge>25</maxAge>" +
						"<minAge>2</minAge>" +
						"</ageRange>" +
						"<language>fr</language>" +
						//"<language>en</language>" +
						//"<language>he</language>" +
						"<keyword>flag</keyword>" +
						"</s2ql>";
		
		SimpleTranslator translator = new SimpleTranslator() ;
		try 
        {
			System.out.println(translator.transformQueryToLuceneQL(sQuery)) ;
		}
        catch (Exception e) 
        {
			e.printStackTrace() ;
		}
	}
	
}
