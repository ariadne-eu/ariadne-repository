//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package org.eun.plql.layer1;



//#line 1 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"


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

  import java.io.IOException;
import java.io.Reader;




public class PlqlLayer1Analyzer
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class PlqlLayer1AnalyzerVal is defined in PlqlLayer1AnalyzerVal.java


String   yytext;//user variable to return contextual strings
PlqlLayer1AnalyzerVal yyval; //used to return semantic vals from action routines
PlqlLayer1AnalyzerVal yylval;//the 'lval' (result) I got from yylex()
PlqlLayer1AnalyzerVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new PlqlLayer1AnalyzerVal[YYSTACKSIZE];
  yyval=new PlqlLayer1AnalyzerVal();
  yylval=new PlqlLayer1AnalyzerVal();
  valptr=-1;
}
void val_push(PlqlLayer1AnalyzerVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
PlqlLayer1AnalyzerVal val_pop()
{
  if (valptr<0)
    return new PlqlLayer1AnalyzerVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
PlqlLayer1AnalyzerVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new PlqlLayer1AnalyzerVal();
  return valstk[ptr];
}
//#### end semantic value section ####
public final static short NL=257;
public final static short AND=258;
public final static short CHARSTRING1=259;
public final static short CHARSTRING2=260;
public final static short LEFT_PARENTHESIS=261;
public final static short RIGHT_PARENTHESIS=262;
public final static short INTEGER=263;
public final static short REAL=264;
public final static short DOT=265;
public final static short OPERATORS=266;
public final static short STANDARD=267;
public final static short SEMICOLON=268;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    1,    1,    1,    2,    3,    3,    3,
    3,    4,    5,    6,    7,    8,    9,   10,   10,   10,
   10,   10,   11,   11,   12,   12,   13,   14,
};
final static short yylen[] = {                            2,
    1,    1,    3,    1,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    3,    3,    5,    9,    1,    3,    1,    1,
};
final static short yydefred[] = {                         0,
   14,   15,    0,   16,   17,   28,    0,    0,    4,    7,
    8,    9,   12,   13,   10,   11,    0,   18,    0,    0,
    0,    0,    0,    0,    0,    5,   19,    0,    0,    0,
    0,    0,    0,   25,    0,    0,    0,    0,   27,    0,
    0,    0,    0,   23,    0,    0,    0,    0,   24,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   13,   14,   15,   16,   17,
   18,   42,   40,   19,
};
final static short yysindex[] = {                      -224,
    0,    0, -224,    0,    0,    0,    0, -249,    0,    0,
    0,    0,    0,    0,    0,    0, -251,    0, -253, -247,
 -234, -224, -224, -215, -241,    0,    0, -249, -217, -249,
 -217, -215, -211,    0, -240, -220, -215, -245,    0, -209,
 -211, -208, -207,    0, -241, -241, -213, -209,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   56,    0,    0,
    0,    0,    0,    0,    0,    0,   59,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    2,    3,
    4,    0,   60,    0,    0,    0,    0,    0,    0,    0,
    5, -205,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   -3,    0,  -32,  -15,    0,    0,    0,    0,    0,   10,
    0,  -19,   15,   25,
};
final static int YYTABLESIZE=272;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         20,
    6,   21,   22,   20,    6,   35,   23,   44,   22,   34,
   22,   25,   21,    1,   26,   49,   24,    1,   28,   30,
   33,    6,   34,   23,   38,   39,   47,   27,   36,   34,
   34,   29,   31,   41,    1,    2,    3,   37,    4,    5,
   23,   26,    6,    1,    2,   32,   37,    4,    5,    1,
    2,   45,   39,    4,    5,    1,   45,   46,    2,    3,
   26,   48,   43,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    6,   21,   22,   20,    6,    0,    6,   21,
   22,   20,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          3,
    0,    0,    0,    0,    0,   25,  258,   40,  258,   25,
  258,  265,    3,  259,  262,   48,  268,  259,   22,   23,
   24,  267,   38,  258,  265,  266,   46,  262,   32,   45,
   46,   22,   23,   37,  259,  260,  261,  258,  263,  264,
  258,  262,  267,  259,  260,  261,  258,  263,  264,  259,
  260,  265,  266,  263,  264,    0,  265,  265,    0,    0,
  266,   47,   38,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  262,  262,  262,  262,  262,   -1,  268,  268,
  268,  268,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=268;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"NL","AND","CHARSTRING1","CHARSTRING2","LEFT_PARENTHESIS",
"RIGHT_PARENTHESIS","INTEGER","REAL","DOT","OPERATORS","STANDARD","SEMICOLON",
};
final static String yyrule[] = {
"$accept : plql",
"plql : clause",
"plql : exactClause",
"plql : exactClause SEMICOLON clause",
"clause : keywordClause",
"clause : LEFT_PARENTHESIS clause RIGHT_PARENTHESIS",
"clause : clause AND clause",
"keywordClause : operand",
"operand : term1",
"operand : term2",
"operand : integer",
"operand : real",
"term1 : charString1",
"term2 : charString2",
"charString1 : CHARSTRING1",
"charString2 : CHARSTRING2",
"integer : INTEGER",
"real : REAL",
"exactClause : pathexpr",
"exactClause : LEFT_PARENTHESIS exactClause RIGHT_PARENTHESIS",
"exactClause : exactClause AND exactClause",
"exactClause : clause AND exactClause",
"exactClause : exactClause AND clause",
"pathexpr : standard DOT path operator operand",
"pathexpr : standard DOT path DOT standard DOT path operator operand",
"path : term1",
"path : path DOT path",
"operator : OPERATORS",
"standard : STANDARD",
};

//#line 201 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"


	private PlqlLayer1Parser lexer;
	private String query ;
	private static boolean DISPLAY_OUTPUT = false;
	
	private int yylex () {
		int yyl_return = -1;
		try {
			yylval = new PlqlLayer1AnalyzerVal(0);
			yyl_return = lexer.yylex();
		}
		catch (IOException e) {
			System.err.println("IO error :"+e);
		}
		return yyl_return;
	} 

	public void yyerror (String error) {
		System.err.println ("Syntax Error\n" + error);
	}

	public PlqlLayer1Analyzer(Reader r) {
		lexer = new PlqlLayer1Parser(r, this);
	}
	

	public void parse(){
	    yyparse() ;
	}
	
    public String getQuery() {
        return query ;
    }	
//#line 320 "PlqlLayer1Analyzer.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 62 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{  	
					yyval.sval = val_peek(0).sval;
					query = yyval.sval;
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-1.1 : " + query);
					}
break;
case 2:
//#line 67 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
					yyval.sval = val_peek(0).sval;
					query = yyval.sval;
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-1.2 : " + val_peek(0).sval);
					}
break;
case 3:
//#line 72 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
					yyval.sval = val_peek(2).sval + ";" + val_peek(0).sval;
					query = yyval.sval;
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-1.3 : " + val_peek(2).sval);
					}
break;
case 4:
//#line 79 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-2.1 : " + val_peek(0).sval);
                    }
break;
case 5:
//#line 82 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{	
					val_peek(2).sval = " ( " + val_peek(1).sval + " ) "; 
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-2.3 : " + val_peek(2).sval);
					}
break;
case 6:
//#line 86 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{ 	
					val_peek(2).sval = val_peek(2).sval + " AND " + val_peek(0).sval; 
    				if (DISPLAY_OUTPUT) System.out.println("rule number = 1-2.4 : " + val_peek(2).sval);
					}
break;
case 7:
//#line 92 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-2 : " + val_peek(0).sval);
                    }
break;
case 8:
//#line 98 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-3.1 : " + val_peek(0).sval);
                    }
break;
case 9:
//#line 101 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-3.2 : " + val_peek(0).sval);
                    }
break;
case 10:
//#line 104 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-3.3 : " + val_peek(0).sval);
                    }
break;
case 11:
//#line 107 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-3.4 : " + val_peek(0).sval);
                    }
break;
case 12:
//#line 113 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-4 : " + val_peek(0).sval);
                    }
break;
case 13:
//#line 118 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-5 : " + val_peek(0).sval);
                    }
break;
case 14:
//#line 123 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{   
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-6 : " + val_peek(0).sval);
					}
break;
case 15:
//#line 128 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{	
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-7 : " + val_peek(0).sval);
					}
break;
case 16:
//#line 133 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{	
					int number = val_peek(0).ival;
					yyval.sval = ""+number;
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-8 : " + yyval.sval);
					}
break;
case 17:
//#line 140 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{	
					double number = val_peek(0).dval;
					yyval.sval = ""+number;
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-9 : " + yyval.sval);
                	}
break;
case 18:
//#line 148 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-10.1 : " + val_peek(0).sval);
					}
break;
case 19:
//#line 151 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{	
					val_peek(2).sval = " ( " + val_peek(1).sval + " ) "; 
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-10.2 : " + val_peek(2).sval);
					}
break;
case 20:
//#line 155 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{ 	
					val_peek(2).sval = val_peek(2).sval + " AND " + val_peek(0).sval; 
    				if (DISPLAY_OUTPUT) System.out.println("rule number = 1-10.3 : " + val_peek(2).sval);
					}
break;
case 21:
//#line 159 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{ 	
					val_peek(2).sval = val_peek(2).sval + " AND " + val_peek(0).sval; 
    				if (DISPLAY_OUTPUT) System.out.println("rule number = 1-10.4 : " + val_peek(2).sval);
					}
break;
case 22:
//#line 163 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{ 	
					val_peek(2).sval = val_peek(2).sval + " AND " + val_peek(0).sval; 
    				if (DISPLAY_OUTPUT) System.out.println("rule number = 1-10.5 : " + val_peek(2).sval);
					}
break;
case 23:
//#line 170 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{	
					val_peek(4).sval = (val_peek(4).sval + "." + val_peek(2).sval + ":" + val_peek(0).sval).toLowerCase();	
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-11 : " + val_peek(4).sval);
					}
break;
case 24:
//#line 174 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{	
					val_peek(8).sval = (val_peek(8).sval + "." + val_peek(6).sval + "." + val_peek(4).sval + "." + val_peek(2).sval + ":" + val_peek(0).sval).toLowerCase();	
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-11.2 : " + val_peek(8).sval);
					}
break;
case 25:
//#line 180 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-12.1 : " + val_peek(0).sval);
					}
break;
case 26:
//#line 183 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{	
					val_peek(2).sval = (val_peek(2).sval + val_peek(1).sval + val_peek(0).sval).toLowerCase(); 
					if (DISPLAY_OUTPUT) System.out.println("rule number = 1-12.2 : " + val_peek(2).sval);
					}
break;
case 27:
//#line 190 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-13 : " + val_peek(0).sval);
            		}
break;
case 28:
//#line 197 "/Sandbox/eclipse/hmdb/plql2lucene/plql_layer1/src/conf/Layer1.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 1-14 : " + val_peek(0).sval);
               		}
break;
//#line 654 "PlqlLayer1Analyzer.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public PlqlLayer1Analyzer()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public PlqlLayer1Analyzer(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
