package org.ariadne_eu.metadata.query.language;

import java.util.HashMap;

import org.ariadne_eu.metadata.query.language.plql_xquery.Plql_XQueryHandler;

/**
 * Created by ben
 * Date: 5-mei-2007
 * Time: 17:16:30
 * To change this template use File | Settings | File Templates.
 */
public class TranslateLanguage {

    public static final int UNDEFINED = -1;
    public static final int VSQL = 0;
    public static final int PLQL0 = 1;
    public static final int PLQL1 = 2;
    public static final int PLQL2 = 3;
    public static final int XQUERY = 4;
    public static final int LUCENE = 5;
    public static final int JSON = 6;

    private static HashMap cachedImplementations = new HashMap();

    public static void initialize() {
        cachedImplementations = new HashMap();
    }

    public static String translateToQuery(String query, int startQueryLanguage, int endQueryLanguage, int startResult, int nbResults, int resultsFormat) throws QueryTranslationException {
        return getTranslateImplementation(startQueryLanguage, endQueryLanguage).translateToQuery(query, startResult, nbResults, resultsFormat);
    }

    public static String translateToCount(String query, int startQueryLanguage, int endQueryLanguage) throws QueryTranslationException {
        return getTranslateImplementation(startQueryLanguage, endQueryLanguage).translateToCount(query);
    }

    public static int getQueryLanguage(String queryLanguage) {
        if (queryLanguage == null)
            return UNDEFINED;
        if (queryLanguage.equalsIgnoreCase("vsql")) {
            return VSQL;
        } else if (queryLanguage.equalsIgnoreCase("plql0")) {
            return PLQL0;
        } else if (queryLanguage.equalsIgnoreCase("http://www.prolearn-project.org/PLQL/l0")) {
            return PLQL0;
        } else if (queryLanguage.equalsIgnoreCase("plql1")) {
            return PLQL1;
        } else if (queryLanguage.equalsIgnoreCase("http://www.prolearn-project.org/PLQL/l1")) {
            return PLQL1;
        } else if (queryLanguage.equalsIgnoreCase("plql2")) {
            return PLQL2;
        } else if (queryLanguage.equalsIgnoreCase("http://www.prolearn-project.org/PLQL/l2")) {
            return PLQL2;
        } else if (queryLanguage.equalsIgnoreCase("lucene")) {
            return LUCENE;
        } else if (queryLanguage.equalsIgnoreCase("json")) {
    		return JSON;
    	} else {
            return UNDEFINED;
        }
    }

    private static Translate getTranslateImplementation(int startQueryLanguage, int endQueryLanguage) throws QueryTranslationException {
        if (cachedImplementations.get(new HashMapKey(startQueryLanguage, endQueryLanguage)) == null) {
            switch(endQueryLanguage) {
                case XQUERY:
                    switch(startQueryLanguage) {
                        case VSQL:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Vsql_XQueryHandler(startQueryLanguage, endQueryLanguage));
                            break;
                        case PLQL0:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Plql_XQueryHandler(startQueryLanguage, endQueryLanguage));
                            break;
                        case PLQL1:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Plql_XQueryHandler(startQueryLanguage, endQueryLanguage));
                            break;
                        case PLQL2:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Plql_XQueryHandler(startQueryLanguage, endQueryLanguage));
                            break;
                    }
                    break;
                case LUCENE:
                    switch(startQueryLanguage) {
                    	case VSQL:
                    		cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Vsql_LuceneHandler(startQueryLanguage, endQueryLanguage));
                    		break;
                        case PLQL0:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Plql_LuceneHandler(startQueryLanguage, endQueryLanguage));
                            break;
                        case PLQL1:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Plql_LuceneHandler(startQueryLanguage, endQueryLanguage));
                            break;
                        case PLQL2:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Plql_LuceneHandler(startQueryLanguage, endQueryLanguage));
                            break;
                        case LUCENE:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new LuceneHandler(startQueryLanguage, endQueryLanguage));
                            break;
                        case JSON:
                            cachedImplementations.put(new HashMapKey(startQueryLanguage, endQueryLanguage), new Json_LuceneHandler(startQueryLanguage, endQueryLanguage));
                            break;
                    }
                    break;
            }
        }
        if (cachedImplementations.get(new HashMapKey(startQueryLanguage, endQueryLanguage)) == null)
            throw new QueryTranslationException();
        return (Translate) cachedImplementations.get(new HashMapKey(startQueryLanguage, endQueryLanguage));
    }

    private static class HashMapKey {
        private int startQueryLanguage;
        private int endQueryLanguage;

        public HashMapKey(int startQueryLanguage, int endQueryLanguage) {
            this.startQueryLanguage = startQueryLanguage;
            this.endQueryLanguage = endQueryLanguage;
        }

        public boolean equals(Object object) {
            if (object instanceof HashMapKey) {
                HashMapKey hashMapKey = (HashMapKey) object;
                return startQueryLanguage == hashMapKey.startQueryLanguage && endQueryLanguage == hashMapKey.endQueryLanguage;
            }
            return false;
        }

        public int hashCode() {
            return startQueryLanguage*1000 + endQueryLanguage;
        }
    }
}
