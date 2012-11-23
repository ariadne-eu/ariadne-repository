/*
* last update: 2006-08-24
*/

editAreaLoader.load_syntax["xml"] = {
	'COMMENT_SINGLE' : {}
	,'COMMENT_MULTI' : {'<![CDATA[' : ']]>'}
	,'QUOTEMARKS' : {}
	,'KEYWORD_CASE_SENSITIVE' : false
	,'KEYWORDS' : {
	}
	,'OPERATORS' :[
	]
	,'DELIMITERS' :[
	]
	,'REGEXPS' : {
		'xml' : {
			'search' : '()(<\\?[^>]*?\\?>)()'
			,'class' : 'xml'
			,'modifiers' : 'g'
			,'execute' : 'before' // before or after
		}
		,'comments' : {
			'search' : '()(<!--.*?-->)()'
			,'class' : 'comment'
			,'modifiers' : 'g'
			,'execute' : 'before' // before or after
		}
		,'tags' : {
			'search' : '()(</?[a-z][^ \r\n\t>]*)([^>]*>)'
			,'class' : 'tags'
			,'modifiers' : 'gi'
			,'execute' : 'before' // before or after
		}
        ,'endoftags' : {
			'search' : '(</?[a-z][^ \r\n\t>]*[^>]*)(>)()'
			,'class' : 'tags'
			,'modifiers' : 'gi'
			,'execute' : 'before' // before or after
		}
		,'attrname' : {
			'search' : '( |\n|\r|\t)([^ \r\n\t=]+)(="|\'[^<]*>)'
			,'class' : 'attrname'
			,'modifiers' : 'g'
			,'execute' : 'before' // before or after
		}
		,'attrvaluedbl' : {
			'search' : '(=)("[^"]*")()'
			,'class' : 'attrvalue'
			,'modifiers' : 'g'
			,'execute' : 'before' // before or after
		}
		,'attrvaluesgl' : {
			'search' : "(=)('[^']*')()"
			,'class' : 'attrvalue'
			,'modifiers' : 'g'
			,'execute' : 'before' // before or after
		}

	}
	,'STYLES' : {
		'COMMENTS': 'color: #008C00;'
		,'QUOTESMARKS': 'color: #6381F8;'
		,'KEYWORDS' : {
			}
		,'OPERATORS' : 'color: #E775F0;'
		,'DELIMITERS' : ''
		,'REGEXPS' : {
			'attrname': 'color: #F5844C;'
			,'tags': 'color: #0000E0;'
			,'xml': 'color: #8DCFB5;'
			,'comment': 'color: #AAAAAA;'
			,'attrvalue': 'color: #993300;'
		}
	}
};