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

import org.eun.plql.layer2.PlqlLayer2Analyzer;

public class PLQL2Translator 
{

	//private String plqlQuery = "lom.general.identifier.(catalog=isbn and entry=xxxxx)" ; 
	//private String plqlQuery = "lom.general.(title = \"Design Patterns\" and language = \"en\")" ; 
	//private String plqlQuery = "lom.general.title = \"Design Patterns\" and lom.educational.(intendedEndUserRole = \"learner\" and typicalAgeRange = \"15-18\")" ; 
	//private static String plqlQuery = "lom.general.title = \"abc\" not tiger" ; 
	//private String plqlQuery = "(lom.general.title = \"abc\") and tiger" ;
	//private String plqlQuery = "lom.general.title = abc" ;
 
	//private static String plqlQuery = "lom.(general = xxxx and technical = yyyyy)" ; 
	//private static String plqlQuery = "lom.general.( title.string = \"abc\" and title.string.language = \"fr\" )" ; 

	//private static String plqlQuery = "lom.geneRal.((titLe = 12) and lanGuage=\"fr\")" ;
	//private static String plqlQuery = "lre.typicalAgeRange = 12-13";
	//private String plqlQuery = "lom.general.(title = \"java\")" ;
	
	//private String plqlQuery = "lom.general.( (identifier = 123 and catalog = xxx) and ( language = \"fr\" and title = abc ) )" ; 

	//private static String plqlQuery = "lom.classification.(purpose = discipline and taxon = 195)";
	
	//private static String plqlQuery = "lre.discipline = 195";
	
	//private static String plqlQuery = "lom.general.title exact \"abc\"";
	//private static String plqlQuery = "lom.general.identifier.entry = \"14698\" OR lom.general.identifier.entry = \"14698\"" ;
	private static String plqlQuery = "lom.lifecycle.contribute.date > 2008-03-19 OR lom.lifecycle.contribute.date = 2008-03-19";
	//private static String plqlQuery = "lom.general.title > 25";
	//private static String plqlQuery = "lom.general.title >= 25";
	//private static String plqlQuery = "lom.general.title < 25";
	//private static String plqlQuery = "lom.general.title <= 25";
	
	
	/*
	lre.structure = atomic
    lre.status = draft
    lre.typicalAgeRange = 12-13
    lre.cc = \"by-sa\"
    lre.author=\"Author\"
    lre.learningResourceType = \"exercise\"
    lre.creationDate=2007-04-04
    lre.competency = \"top_5 act_3\"
    lre.discipline = 195 
	 
	 */
    public static void main(String[] args) throws Exception
    {
        System.out.println(new PLQL2Translator().transformQueryToLuceneQL(plqlQuery));
    }
     
    /**
     * 
     */ 
    public String transformQueryToLuceneQL(String plqlQuery) {
        Reader r = new StringReader( plqlQuery ) ;
        PlqlLayer2Analyzer parser = new PlqlLayer2Analyzer( r ) ;
        parser.parse() ;
		return parser.getQuery();	        
    }
    
}
