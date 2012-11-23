package org.ariadne_eu.metadata.query.lucene.query;

public class LuceneQLMaker {
	
	public static final String S2QL = "http://fire.eun.org/xsd/s2ql-2.0";
	public static final String LuceneQL = "LuceneQL";
	public static final String PLQL_L0 = "http://www.prolearn-project.org/PLQL/l0";
	public static final String PLQL_L1 = "http://www.prolearn-project.org/PLQL/l1";
	public static final String PLQL_L2 = "http://www.prolearn-project.org/PLQL/l2";
	public static final String JsonQL = "JsonQL";
	
	public static String createQuery(String queryLanguage, String query) throws QueryMakerException{
		try {
			if (queryLanguage.equals(S2QL)){
				return new org.eun.translator.SimpleTranslator().transformQueryToLuceneQL(query);
			} else if (queryLanguage.equals(LuceneQL)){
				return query;
			} else if (queryLanguage.equals(PLQL_L0)){
				return new org.eun.plql.PLQL0Translator().transformQueryToLuceneQL(query);
			} else if (queryLanguage.equals(PLQL_L1)){
				return new org.eun.plql.PLQL1Translator().transformQueryToLuceneQL(query);
			} else if (queryLanguage.equals(PLQL_L2)){
				return new org.eun.plql.PLQL2Translator().transformQueryToLuceneQL(query);
		    } else if (queryLanguage.equals(JsonQL)){
				return query;
		    } else {
		    	throw new QueryMakerException(queryLanguage+" isn't supported.");
		    }
	    } catch (Exception e) {
			throw new QueryMakerException("Translation error :\n"+e);
		}
	}
	
}
