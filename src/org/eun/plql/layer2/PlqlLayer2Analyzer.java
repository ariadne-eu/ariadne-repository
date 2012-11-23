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



package org.eun.plql.layer2;



//#line 1 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"


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




public class PlqlLayer2Analyzer
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
//public class PlqlLayer2AnalyzerVal is defined in PlqlLayer2AnalyzerVal.java


String   yytext;//user variable to return contextual strings
PlqlLayer2AnalyzerVal yyval; //used to return semantic vals from action routines
PlqlLayer2AnalyzerVal yylval;//the 'lval' (result) I got from yylex()
PlqlLayer2AnalyzerVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new PlqlLayer2AnalyzerVal[YYSTACKSIZE];
  yyval=new PlqlLayer2AnalyzerVal();
  yylval=new PlqlLayer2AnalyzerVal();
  valptr=-1;
}
void val_push(PlqlLayer2AnalyzerVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
PlqlLayer2AnalyzerVal val_pop()
{
  if (valptr<0)
    return new PlqlLayer2AnalyzerVal();
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
PlqlLayer2AnalyzerVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new PlqlLayer2AnalyzerVal();
  return valstk[ptr];
}
//#### end semantic value section ####
public final static short NL=257;
public final static short AND=258;
public final static short OR=259;
public final static short LEFT_PATENTHESIS=260;
public final static short RIGHT_PATENTHESIS=261;
public final static short CHARSTRING1=262;
public final static short CHARSTRING2=263;
public final static short DOT=264;
public final static short OPERATOR=265;
public final static short STANDARD=266;
public final static short INTEGER=267;
public final static short REAL=268;
public final static short standard=269;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    2,    2,    2,    3,
    4,    5,    6,    7,    8,    9,    9,   10,   10,   11,
   12,   13,   13,   13,   14,   14,
};
final static short yylen[] = {                            2,
    1,    1,    1,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    3,    3,    3,    3,    1,
    5,    3,    3,    3,    1,    1,
};
final static short yydefred[] = {                         0,
    0,   12,   13,    0,   14,   15,    0,    0,    2,    6,
    7,   10,   11,    8,    9,    3,    0,    0,    0,   25,
   26,    0,    0,   20,    0,    4,   18,    0,    0,   19,
   17,   16,    0,    0,    0,    0,    0,    0,   24,    0,
   22,    0,    0,   21,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   13,   14,   15,   16,   17,
   25,   31,   35,   22,
};
final static short yysindex[] = {                      -240,
 -240,    0,    0, -262,    0,    0,    0, -210,    0,    0,
    0,    0,    0,    0,    0,    0, -214, -225, -249,    0,
    0, -240, -231,    0, -256,    0,    0, -210, -215,    0,
    0,    0, -215, -247, -210, -221, -256, -215,    0, -215,
    0, -217, -210,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   35,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -222,    0,
};
final static short yygindex[] = {                         0,
    2,  -20,  -19,    0,    0,    0,    0,    0,    0,    0,
    9,    0,   -8,  -27,
};
final static int YYTABLESIZE=262;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         27,
    5,   19,   18,   30,   32,    2,    3,   38,   40,   34,
    5,    6,    2,   34,   40,   40,   41,   24,   34,    1,
   34,    2,    3,   28,   36,    4,    5,    6,   29,   42,
    2,   43,   20,   21,    1,   26,   20,   21,   23,   39,
   20,   21,   37,   44,   33,    0,    2,   20,   21,   23,
   24,    0,    0,    0,    0,    0,    0,    0,    0,    0,
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
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    5,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         19,
    0,  264,    1,   23,   25,  262,  263,   35,   36,   29,
  267,  268,  262,   33,   42,   43,   37,  265,   38,  260,
   40,  262,  263,   22,   33,  266,  267,  268,  260,   38,
  262,   40,  258,  259,    0,  261,  258,  259,  261,  261,
  258,  259,   34,  261,  260,   -1,  262,  258,  259,  264,
  265,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  261,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=269;
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
null,null,null,"NL","AND","OR","LEFT_PATENTHESIS","RIGHT_PATENTHESIS",
"CHARSTRING1","CHARSTRING2","DOT","OPERATOR","STANDARD","INTEGER","REAL",
"standard",
};
final static String yyrule[] = {
"$accept : plql",
"plql : clause",
"clause : operand",
"clause : pathexpr",
"clause : LEFT_PATENTHESIS clause RIGHT_PATENTHESIS",
"clause : clause boolean clause",
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
"pathexpr : path operator operand",
"pathexpr : path DOT pathExp",
"path : STANDARD DOT term1",
"path : path DOT term1",
"operator : OPERATOR",
"pathExp : LEFT_PATENTHESIS selector boolean selector RIGHT_PATENTHESIS",
"selector : term1 operator operand",
"selector : selector boolean selector",
"selector : LEFT_PATENTHESIS selector RIGHT_PATENTHESIS",
"boolean : AND",
"boolean : OR",
};

//#line 238 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"


	private PlqlLayer2Parser lexer;/***/
	private String query ;
	public static boolean DISPLAY_OUTPUT = false;
	
	private int yylex () {
		int yyl_return = -1;
		try {
			yylval = new PlqlLayer2AnalyzerVal(0);/***/
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

	public PlqlLayer2Analyzer(Reader r) {/***/
		lexer = new PlqlLayer2Parser(r, this);/***/
	}
	
	public void parse()
    {
	    yyparse() ;
	}
	
    public String getQuery()
    {
        return query ;
    }		
//#line 319 "PlqlLayer2Analyzer.java"
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
	
	//GAP start
	boolean flag = false;
	//GAP end
	
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
        
     // GAP start
		if (yylval.sval.equalsIgnoreCase("="))
			flag = true;
		else if (flag == true) {
			String temp = yylval.sval;
			temp = temp.replaceAll("[:]", "\\\\:");
			yylval = new PlqlLayer2AnalyzerVal(temp);
		}
		// GAP end
		
		
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
//#line 64 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{  	yyval.sval = val_peek(0).sval;
							query = yyval.sval;
						}
break;
case 2:
//#line 69 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
										val_peek(0).sval="contents:	"+val_peek(0).sval;
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 2.1 : " + val_peek(0).sval);
                    }
break;
case 3:
//#line 73 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{		
					if (DISPLAY_OUTPUT) System.out.println("rule number = 2.2 : " + val_peek(0).sval);
				}
break;
case 4:
//#line 76 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{	
					val_peek(2).sval = " ( " + val_peek(1).sval + " ) "; 
					if (DISPLAY_OUTPUT) System.out.println("rule number = 2.3 : " + val_peek(2).sval);
					}
break;
case 5:
//#line 80 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{ 	
					val_peek(2).sval = val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval; 
					if (DISPLAY_OUTPUT) System.out.println("rule number = 2.4 : " + val_peek(2).sval);
					}
break;
case 6:
//#line 86 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 3.1 : " + val_peek(0).sval);
                    }
break;
case 7:
//#line 89 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 3.2 : " + val_peek(0).sval);
                    }
break;
case 8:
//#line 92 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
					val_peek(0).sval=""+val_peek(0).sval;
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 3.3 : " + val_peek(0).sval);
                    }
break;
case 9:
//#line 96 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 3.4 : " + val_peek(0).sval);
                    }
break;
case 10:
//#line 102 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
										val_peek(0).sval=val_peek(0).sval.toLowerCase();
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 4 : " + val_peek(0).sval);
                    }
break;
case 11:
//#line 108 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 5 : " + val_peek(0).sval);
                    }
break;
case 12:
//#line 113 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{   
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 6 : " + val_peek(0).sval);
					}
break;
case 13:
//#line 118 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{	
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 7 : " + val_peek(0).sval);
					}
break;
case 14:
//#line 123 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 8 : " + val_peek(0).sval);
					}
break;
case 15:
//#line 128 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{	
				if (DISPLAY_OUTPUT) System.out.println("rule number = 9 : " + val_peek(0).sval);
                }
break;
case 16:
//#line 134 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
					if ("=".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + val_peek(0).sval;
						}
					else if ("exact".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase() +".exact : " + val_peek(0).sval;
						}
					else if (">".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + "{" + val_peek(0).sval + " TO " + "99999999}";
						}
					else if (">=".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + "[" + val_peek(0).sval + " TO " + "99999999]";
						}
					else if ("<".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + "{00000000" + " TO " + val_peek(0).sval + "}";
						}
					else if ("<=".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + "[00000000" + " TO " + val_peek(0).sval + "]";;
						}
					if (DISPLAY_OUTPUT) System.out.println("rule number = 10.1 : " + val_peek(2).sval );
					}
break;
case 17:
//#line 155 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{ 
					val_peek(2).sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval; 
					if (DISPLAY_OUTPUT) System.out.println("rule number = 10.2 : " + val_peek(2).sval );
					}
break;
case 18:
//#line 162 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                    val_peek(2).sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;
					if (DISPLAY_OUTPUT) System.out.println("rule number = 11.1 : " + val_peek(2).sval);
					}
break;
case 19:
//#line 166 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                    val_peek(2).sval = val_peek(2).sval + val_peek(1).sval + val_peek(0).sval;
					if (DISPLAY_OUTPUT) System.out.println("rule number = 11.2 : " + val_peek(2).sval);
					}
break;
case 20:
//#line 175 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 13 : " + val_peek(0).sval);
            }
break;
case 21:
//#line 182 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
					/*GAP: donde los parentesis son tomados en cuenta*/
					System.out.println(val_peek(4).sval + " - " + val_peek(3).sval + " - " + val_peek(2).sval + " - " + val_peek(1).sval + " - " + val_peek(0).sval);
					val_peek(4).sval = val_peek(4).sval + " " + val_peek(3).sval + " " + val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval ; 
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 2-15.1 : " + val_peek(4).sval);
				}
break;
case 22:
//#line 191 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
					/*GAP: manejo de operadores aparte de la regla 10*/
					if ("=".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+" : " + val_peek(0).sval;
					}
					else if ("exact".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase() +".exact : " + val_peek(0).sval;
					}
					else if (">".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + "{" + val_peek(0).sval + " TO " + "99999999}";
					}
					else if (">=".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + "[" + val_peek(0).sval + " TO " + "99999999]";
					}
					else if ("<".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + "{00000000" + " TO " + val_peek(0).sval + "}";
					}
					else if ("<=".equals(val_peek(1).sval))	{
						val_peek(2).sval = val_peek(2).sval.toLowerCase()+":" + "[00000000" + " TO " + val_peek(0).sval + "]";;
					}
					
					/*Lo unico q habia anteriormente!*/
					/*$1 = $1 + " " + $2 + " " + $3;*/
					
					if (DISPLAY_OUTPUT) System.out.println("rule number = 15.1 : "+ val_peek(2).sval);
					}
break;
case 23:
//#line 217 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{ 
					val_peek(2).sval = val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval ;
					if (DISPLAY_OUTPUT) System.out.println("rule number = 15.2 : "+ val_peek(2).sval);
					}
break;
case 24:
//#line 221 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
                                        val_peek(2).sval = val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval ;
                                        if (DISPLAY_OUTPUT) System.out.println("rule number = 15.3 : " + val_peek(2).sval);
                                        }
break;
case 25:
//#line 228 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
					val_peek(0).sval = "AND";
                    if (DISPLAY_OUTPUT) System.out.println("rule number = 16.1 : " + val_peek(0).sval);
                }
break;
case 26:
//#line 232 "/Sandbox/eclipse/hmdb/plql/plql_layer2/src/conf/Layer2.y"
{
					val_peek(0).sval = " ";
					if (DISPLAY_OUTPUT) System.out.println("rule number = 16.2 : " + val_peek(0).sval);
			    }
break;
//#line 678 "PlqlLayer2Analyzer.java"
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
public PlqlLayer2Analyzer()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public PlqlLayer2Analyzer(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
