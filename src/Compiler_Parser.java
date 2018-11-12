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
                    outputforTrio.append("<Function Definitions'> -> <Function> <Function Definitions'>\n" + "<Function> -> function <Identifier> ( <Opt Parameter List> )  <Opt Declaration List>  <Body> \n");
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
                    outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Integer>\n");
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
                //do stuff here
                break;
            case KEYWORD:
                //do stuff here
                break;
            case SEPARATOR:
                //do stuff here
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
