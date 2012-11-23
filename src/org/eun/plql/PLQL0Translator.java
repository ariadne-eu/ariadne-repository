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

import org.eun.plql.layer0.PlqlLayer0Analyzer;

public class PLQL0Translator 
{

//	private static String plqlQuery = "heLlo and \"reYvath\" and 001.5 and 0120 and \"///\\ 123.34\" and 1000a and 000 and .123 and \"or\" ";
	private static String plqlQuery = "heLlo";

    public static void main(String[] args) throws Exception
    {
        System.out.println(new PLQL0Translator().transformQueryToLuceneQL(plqlQuery));
    }
    
    /**
     * 
     */
    public String transformQueryToLuceneQL(String plqlQuery) {
        Reader r = new StringReader( plqlQuery ) ;
        PlqlLayer0Analyzer parser = new PlqlLayer0Analyzer( r ) ;
        parser.parse() ;
		return parser.getQuery();	        
    }
    
}
