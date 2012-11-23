/*
Copyright (C) 2006  David Massart and Chea Sereyvath, European Schoolnet

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

package org.eun.plql ;

import java.io.Reader;
import java.io.StringReader;

import org.eun.plql.layer1.PlqlLayer1Analyzer;

public class PLQL1Translator {
	
	private static String queries[] = new String[]{
//		"heLlo and \"reYvath\" and 001.5 and 0120 and \"///\\ 123.34\" and 1000a and 000 and .123 and \"or\" "
//		,
//		"2.0 and web"
//		,
//		"dog"
//		,
//		"\"learning object\" and dog"
//		,
//		"dog and cat and jaguar"
//		,
//		"(dog and cat) and jaguar"
//		,
//		"\"lom.general.title\" and \"my dog\""
//		,
//		"1.2 and dog"
//		,
//		"test and 1024"
//		,
//		"\"12.25 dog\""
//		,
//		"\"\\\"hello\\\" he said\""
//		,
//		"dc.title = \"SQL\" and lom.general.lom.title = \"SQL\""
//		,
//		"lom.general.title = \"Design Patterns\" and lom.technical.format = \"video/mpeg\" and lom.technical.duration = \"PT1H\" and lom.rights.cost = \"no\""
//		,
//		"lom.general.title = \"Design Patterns\" and lom.educational.intendedUserRole = \"learner\" and lom.educational.typicalAgeRange = \"15-18\""
//		,
//		"((lom.general.title = abc) and (lom.general.language = \"fr\")) ; test"
//		,
//		"tiger"
//		,
//		"keyword1 and keyword2 and (lom.general.language = \"fr\") and (lom.educational.ageRange = \"10-12\")"
//		,
		"keyword1 and keyword2 and (lom.general.language = en) and (lom.educational.ageRange=10-12)"
//		,
//		"protocol.identifier.catalog = \"ariadne-protocols-targets\""
//		"lom.general.identifier.entry = \"Perro\""
		
};

//	private static String plqlQuery = "hello and \" my name is /// ) \\\"sereyvath\" and dc.title = \"SQL\" and lom.generate.title = \"SQL\" and lom.title = 1299 " ;
//	private String plqlQuery = "lom.generate.title = \"SQL dgddd )(fhfh\" and lom.general = 123 and lom.test = .0121 and test " ;
//	private String plqlQuery = " E and ((A and B) and (C and D) )";
//  private String plqlQuery = "test and ((lom.title = abc) and (lom.language=\"fr\")) " ;
	// s2ql equivalent  
//	private static String plqlQuery = "keyword1 and keyword2 and (lom.geNeral.lanGuage = \"fr test\" ) and (lom.educaTional.ageRange=10-12) " ;
//	private static String plqlQuery = "lom.general.identifier.entry = \"14698\" AND lom.general.identifier.entry = \"14698\"" ;
//	private static String plqlQuery = "lom.technical.format = \"text/html\"" ;
//	private static String plqlQuery = "Lom.description.metadata.general.language=\"nl\"";
//	private static String plqlQuery = "lom.general.lom.title = \"Design:Patterns\"  and lom.technical.format = \"video/mpeg\" and lom.technical.duration = \"PT1H\" and lom.rights.cost = \"lom\"";

    public static void main(String[] args) throws Exception
    {
    	for (int i = 0; i < queries.length; i++) {
    		System.out.println(new PLQL1Translator().transformQueryToLuceneQL(queries[i]));
		}
        
    }
    
    /**
     * 
     */ 
    public String transformQueryToLuceneQL(String plqlQuery) {
        Reader r = new StringReader( plqlQuery ) ;
        PlqlLayer1Analyzer parser = new PlqlLayer1Analyzer( r ) ;
        parser.parse() ;
		return parser.getQuery();	        
    }
    
}
