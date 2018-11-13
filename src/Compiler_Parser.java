import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
public class Compiler_Parser {

    public enum ParserState {
        VALID, ERROR
    }

    //implement an expected token to pass into the error() function
    public String parse(Compiler_LA.Token preToken, Compiler_LA.Token currToken, Compiler_LA.Token postToken) throws IOException {
        ParserState state = ParserState.ERROR;
        StringBuilder outputforTrio = new StringBuilder();

        // IDENTIFIER, KEYWORD, INTEGER, REAL, OPERATOR, SEPARATOR, INVALID
        //utilize past, current and future prediction to associate trio of lexemes to production rules
        //If any lexeme does not fulfill a production rule for its current position then it is deemed invalid and sent to the error function
        switch(currToken.getToken()) {
            case IDENTIFIER:
                if (preToken.getLexeme().equals("function")){
                    outputforTrio.append("<Function Definitions> -> <Function><Function Definitions'>\n" + "<Function> -> function <Identifier> ( <Opt Parameter List> )  <Opt Declaration List>  <Body> \n");
                } else if (preToken.getLexeme().equals("return")) {
                    outputforTrio.append( "<Statement> -> <Return>\n" + "<Return> -> return <Expression>\n");
                } else if(preToken.getLexeme().equals("int") || preToken.getLexeme().equals("boolean") || preToken.getLexeme().equals("real")){
                    if(postToken.getLexeme().equals(",")) {
                        outputforTrio.append("<Decleration List'> -> <Decleration>\n" + "<Decleration> -> <int | boolean | real > <IDs>\n" + "<IDs> -> <Identifier>, <IDs>\n");
                    } else {
                        outputforTrio.append("<Decleration List'> -> <Decleration>\n" + "<Decleration> -> <int | boolean | real > <IDs>\n" + "<IDs> -> <Identifier>\n");
                    }
                } else if(preToken.getLexeme().equals("-")) {
                    outputforTrio.append("<Factor> -> -<Primary>\n" + " <Primary> -> <Identifier>\n");
                } else if(preToken.getLexeme().equals("+")){
                   outputforTrio.append("<Expression'> -> +<Term><Expression'>\n" + "<Term> -> <Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>\n");
                } else if(preToken.getLexeme().equals("*")){
                    outputforTrio.append("<Term'> -> *<Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>\n");
                } else if(preToken.getLexeme().equals("/")){
                    outputforTrio.append("<Term'> -> /<Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>\n");
                } else if(preToken.getLexeme().equals(",")){
                    outputforTrio.append("<IDs'>  ->  ,<Identifier><IDs'>\n" + "<IDs'> -> ɛ");
                } else if(preToken.getLexeme().equals("=")){
                    outputforTrio.append("<Assign> -> <Identifier> = <Expression>;\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>\n");
                } else if(preToken.getLexeme().equals(">") || preToken.getLexeme().equals("<") ){
                    outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>\n");
                } else if(preToken.getLexeme().equals("(") || preToken.getLexeme().equals(")") ){
                    outputforTrio.append("<Function> -> function <Identifier> (<Opt Parameter List>)<Opt Declaration List><Body>\n");
                } //else if(preToken.getLexeme().equals("") ){
                    //possibly need to add one prod rule here for put but it should be counted in keyword section
                else if(postToken.getLexeme().equals("=") ){
                    outputforTrio.append("<Statement>-> <Assign>\n" + "<Assign> -><Identifier> = <Expression>;\n");
                } else {
                    error(currToken, expectedToken);
                }
                break;
            case INTEGER:
                if(preToken.getLexeme().equals("-")){
                    outputforTrio.append("<Factor> -> -<Primary>\n" + "<Primary> -> <Integer>\n");
                } else if(preToken.getLexeme().equals("+")){
                    outputforTrio.append("<Expression'> -> +<Term><Expression'>\n" + "<Term> -> <Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Integer>\n");
                } else if(preToken.getLexeme().equals("*")){
                    outputforTrio.append("<Term'> -> *<Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Integer>\n");
                } else if(preToken.getLexeme().equals("/")){
                    outputforTrio.append("<Term'> -> /<Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Integer>\n");
                } else if(preToken.getLexeme().equals(">") || preToken.getLexeme().equals("<")){
                    outputforTrio.append("<Condition> -> <Expression><Relop><Expression>\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Integer>\n");
                } else if(preToken.getLexeme().equals("=")){
                    outputforTrio.append("<Assign> -> <Identifier> = <Expression>;\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Integer>\n");
                } else {
                    error(currToken, expectedToken);
                }
                break;
            case REAL:
                if(preToken.getLexeme().equals("-")){
                    outputforTrio.append("<Factor> -> -<Primary>\n" + "<Primary> -> <Real>\n");
                } else if(preToken.getLexeme().equals("+")){
                    outputforTrio.append("<Expression'> -> +<Term><Expression'>\n" + "<Term> -> <Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                } else if(preToken.getLexeme().equals("*")){
                    outputforTrio.append("<Term'> -> *<Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                } else if(preToken.getLexeme().equals("/")){
                    outputforTrio.append("<Term'> -> /<Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                } else if(preToken.getLexeme().equals(">") || preToken.getLexeme().equals("<")){
                    outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                } else if(preToken.getLexeme().equals("=")){
                    outputforTrio.append("<Assign> -> <Identifier> = <Expression>;\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                } else {
                    error(currToken, expectedToken);
                }
                break;
            case OPERATOR:
                if(currToken.getLexeme().equals("-") || currToken.getLexeme().equals("+")){
                    //according to rules we need to check for all primary
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false") || preToken.getLexeme().equals("(")) {
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                        //pre and post follow same rules so we passed the - or + check
                            if(currToken.getLexeme().equals("-")) {
                                outputforTrio.append("<Expression> -> <Term><Expression'>\n" + "<Expression'> -> -<Term><Expression'>\n");
                            } else {
                                outputforTrio.append("<Expression> -> <Term><Expression'>\n" + "<Expression'> -> +<Term><Expression'>\n");
                            }
                        } else {
                            error(currToken, expectedToken);
                        }
                    } else {
                        error(currToken, expectedToken);
                    }
                } else if(currToken.getLexeme().equals("*") || currToken.getLexeme().equals("/")){
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false") || preToken.getLexeme().equals("(")) {
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                            //pre and post follow same rules so we passed the * or / check
                            if(currToken.getLexeme().equals("/")) {
                                outputforTrio.append("<Term> -> <Factor><Term'>\n" + "<Term'> -> /<Factor><Term'>\n");
                            } else {
                                outputforTrio.append("<Term> -> <Factor><Term'>\n" + "<Term'> -> *<Factor><Term'>\n");
                            }
                        } else {
                            error(currToken, expectedToken);
                        }
                    } else {
                        error(currToken, expectedToken);
                    }
                } else if(currToken.getLexeme().equals(">") || currToken.getLexeme().equals("<") || currToken.getLexeme().equals("=<") || currToken.getLexeme().equals("=>") || currToken.getLexeme().equals("^=") || currToken.getLexeme().equals("==")){
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false") || preToken.getLexeme().equals("(")) {
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                            //pre and post follow same rules so we passed the relop check
                            if(currToken.getLexeme().equals(">")) {
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> >\n");
                            } else if(currToken.getLexeme().equals("<")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> <\n");
                            } else if(currToken.getLexeme().equals("=<")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> =<\n");
                            } else if(currToken.getLexeme().equals("=>")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> =>\n");
                            } else if(currToken.getLexeme().equals("^=")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> ^=\n");
                            } else if(currToken.getLexeme().equals("==")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> ==\n");
                            }
                        } else {
                            error(currToken, expectedToken);
                        }
                    } else {
                        error(currToken, expectedToken);
                    }
                } else if(currToken.getLexeme().equals("=")){
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                            outputforTrio.append("<Assign> -> <Identifier> = <Expression>");
                        } else {
                            error(currToken, expectedToken);
                        }
                    } else {
                        error(currToken, expectedToken);
                    }

                }
                break;
            case KEYWORD:
                //do stuff here
                break;
            case SEPARATOR:
                if (currToken.getLexeme().equals("(")) {
                    if(preToken.getLexeme().equals("put")) {
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false")){
                            outputforTrio.append("<Statement> -> <Print>\n" + "<Print> -> put(<Expression>);\n");
                        }
                    } else if(preToken.getLexeme().equals("get")) {
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                            outputforTrio.append("<Statement> -> <Scan>\n" + "   <Scan> -> get(<IDs>);");
                        }
                    } else if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER && postToken.getToken() == Compiler_LA.Type.IDENTIFIER) {
                        outputforTrio.append("<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>(<IDs>)\n");
                    } else if(preToken.getLexeme().equals("+") || preToken.getLexeme().equals("-") || preToken.getLexeme().equals("*") || preToken.getLexeme().equals("/")) {
                        outputforTrio.append("<Factor> -> <Primary>\n" + "<Primary> -> (<Expression>)\n");
                    } else if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER) {
                        if( postToken.getLexeme().equals(")")) {   //I BELIEVE THIS PART STILL NEEDS TO BE ADJUSTED FOR FUNCTIONS THAT HAVE PARAMETERS
                            outputforTrio.append("<Function Definitions> -> <Function><Function Definitions'>\n" + "<Function> -> function <Identifier> (<Opt Parameter List>)<Opt Declaration List><Body>\n");
                        }
                    } else {
                        error(currToken, expectedToken);
                    }

                } else if(currToken.getLexeme().equals(")")) {
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false")){
                        outputforTrio.append("<Factor> -> <Primary>\n" + "<Primary> -> (<Expression>)\n");
                    } else if(preToken.getLexeme().equals("int") || preToken.getLexeme().equals("boolean")|| preToken.getLexeme().equals("real") || preToken.getLexeme().equals("(")){
                        outputforTrio.append("<Function Definitions> -> <Function><Function Definitions'>\n" + "<Function> -> function <Identifier> (<Opt Parameter List>)<Opt Declaration List><Body>\n");
                    } else {
                        error(currToken, expectedToken);
                    }
                } else if(currToken.getLexeme().equals("{")) {
                    if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.KEYWORD  ) {
                        if(preToken.getLexeme().equals(")")) {
                            outputforTrio.append("<Function> -> function<Identifier> (<Opt Parameter List>)<Opt Declaration List><Body>\n" + "<Body> -> {<Statement List>}\n");
                        } else {
                            outputforTrio.append("<Statement> -> <Compound>\n" + "<Compound> -> {<Statement List>}\n");
                        }
                    } else {
                        error(currToken, expectedToken);
                    }
                } else if(currToken.getLexeme().equals("}")) {   //NOT SURE

                } else if(currToken.getLexeme().equals(";")) {   //NOT SURE HOW TO DO THIS
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER) {

                    } else if(preToken.getLexeme().equals(")")) {

                    } else if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false")){

                    }
                } else if(currToken.getLexeme().equals(":")) {
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER && (postToken.getLexeme().equals("boolean") || postToken.getLexeme().equals("int") || postToken.getLexeme().equals("real"))) {
                            outputforTrio.append("<Parameter List> -> <Parameter>\n" + "<Parameter> -> <IDs>:<Qualifier>\n");
                    } else {
                        error(currToken, expectedToken);
                    }
                } else if(currToken.getLexeme().equals(",")) {
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.IDENTIFIER) {
                        outputforTrio.append("<IDs> -> <Identifier><IDs'>\n" + "<IDs'> -> ,<Identifier><IDs'>");
                    } else {
                        error(currToken, expectedToken);
                    }

                } else if(currToken.getLexeme().equals("$$")) {  //need to check for end and begin
                    outputforTrio.append("<Rat18F> -> <Opt Function Definitions> $$ <Opt Declaration List> <Statement List> $$\n");
                }
                break;
            case INVALID:
                error(currToken, expectedToken);
                break;
        }



        return outputforTrio.toString();
    }

    private void error(Compiler_LA.Token errToken, Compiler_LA.Token expectedToken) {

        // print error
        //System.err.print("ERROR: " + error.getType());
        System.err.print(" at line " + errToken.getLineNumber() );
        System.err.println("; Expected " + expectedToken.getToken());

        //errorToken = token; // set error token to prevent cascading
       // errors++; // increment error counter
    }
}
