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

        switch (currToken.getToken()) {
            case IDENTIFIER:
                if (preToken.getLexeme().equals("function")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("return")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                } else if (preToken.getLexeme().equals("int") || preToken.getLexeme().equals("boolean") || preToken.getLexeme().equals("real")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                } else if (preToken.getLexeme().equals("-")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("+")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("*")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("/")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals(",")) {
                    currToken.addExpectedToken(Compiler_LA.Type.IDENTIFIER);
                } else if (preToken.getLexeme().equals("=")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals(">") || preToken.getLexeme().equals("<")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("(") || preToken.getLexeme().equals(")")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                }
                break;
            case REAL:
                if (preToken.getLexeme().equals("return")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                } else if (preToken.getLexeme().equals("int") || preToken.getLexeme().equals("boolean") || preToken.getLexeme().equals("real")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                } else if (preToken.getLexeme().equals("-")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("+")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("*")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("/")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals(",")) {
                    currToken.addExpectedToken(Compiler_LA.Type.IDENTIFIER);
                } else if (preToken.getLexeme().equals("=")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals(">") || preToken.getLexeme().equals("<")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("(") || preToken.getLexeme().equals(")")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                }
                break;
            case INTEGER:
                if (preToken.getLexeme().equals("return")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                } else if (preToken.getLexeme().equals("int") || preToken.getLexeme().equals("boolean") || preToken.getLexeme().equals("real")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                } else if (preToken.getLexeme().equals("-")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("+")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("*")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("/")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals(",")) {
                    currToken.addExpectedToken(Compiler_LA.Type.IDENTIFIER);
                } else if (preToken.getLexeme().equals("=")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals(">") || preToken.getLexeme().equals("<")) {
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                } else if (preToken.getLexeme().equals("(") || preToken.getLexeme().equals(")")) {
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                }
                break;
            case KEYWORD:
                currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                break;
            case SEPARATOR:
                if(preToken.getToken().equals("REAL") || preToken.getToken().equals("INTEGER")){
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                }
                else if (preToken.getToken().equals("IDENTIFIER")){
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.KEYWORD);
                    currToken.addExpectedToken(Compiler_LA.Type.IDENTIFIER);
                }
                else if (preToken.getToken().equals("OPERATOR")){
                    currToken.addExpectedToken(Compiler_LA.Type.REAL);
                    currToken.addExpectedToken(Compiler_LA.Type.INTEGER);
                    currToken.addExpectedToken(Compiler_LA.Type.IDENTIFIER);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                }
                else if (preToken.getToken().equals("KEYWORD")){
                    currToken.addExpectedToken(Compiler_LA.Type.REAL);
                    currToken.addExpectedToken(Compiler_LA.Type.INTEGER);
                    currToken.addExpectedToken(Compiler_LA.Type.IDENTIFIER);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);

                }
                else if (preToken.getToken().equals("SEPERATOR")){
                    currToken.addExpectedToken(Compiler_LA.Type.REAL);
                    currToken.addExpectedToken(Compiler_LA.Type.INTEGER);
                    currToken.addExpectedToken(Compiler_LA.Type.IDENTIFIER);
                    currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);
                    currToken.addExpectedToken(Compiler_LA.Type.OPERATOR);
                }
                break;
            case OPERATOR:
                currToken.addExpectedToken(Compiler_LA.Type.REAL);
                currToken.addExpectedToken(Compiler_LA.Type.INTEGER);
                currToken.addExpectedToken(Compiler_LA.Type.IDENTIFIER);
                currToken.addExpectedToken(Compiler_LA.Type.SEPARATOR);

                break;
        }

        ArrayList<Compiler_LA.Type> expected = currToken.getExpectedToken();
        if (!expected.contains(postToken.getToken())){
            error(currToken);
            return null;
        }

        // IDENTIFIER, KEYWORD, INTEGER, REAL, OPERATOR, SEPARATOR, INVALID
        //utilize past, current and future prediction to associate trio of lexemes to production rules
        //If any lexeme does not fulfill a production rule for its current position then it is deemed invalid and sent to the error function
        switch(currToken.getToken()) {
            case IDENTIFIER:
                if (preToken.getLexeme().equals("function")){
                    outputforTrio.append("<Function Definitions> -> <Function> <Function Definitions'>\n" + "<Function> -> function <Identifier> ( <Opt Parameter List> )  <Opt Declaration List>  <Body> \n");
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
                    error(currToken);
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
                    error(currToken);
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
                    error(currToken);
                }
                break;
            case OPERATOR:
                if(preToken.getToken().equals("Identifier")) {
                    outputforTrio.append("<Term> -> <Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                }
                else if(preToken.getToken().equals("Identifier")) {
                    outputforTrio.append("<Term> -> <Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                }
                else if(preToken.getToken().equals("Identifier")) {
                    outputforTrio.append("<Term> -> <Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                }
                else {
                    error(currToken);
                }
            case KEYWORD:
                if(preToken.getToken().equals("")) {
                    outputforTrio.append("");
                } else {
                    error(currToken);
                }
                break;
            case SEPARATOR:
                if(preToken.getLexeme().equals("Identifier")) {
                    outputforTrio.append("<Term> -> <Factor><Term'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Real>\n");
                } else {
                    error(currToken);
                }
                break;
            case INVALID:
                error(currToken);
                break;
        }
        return outputforTrio.toString();
    }

    private void error(Compiler_LA.Token errToken) {

        ArrayList<Compiler_LA.Type> expected = errToken.getExpectedToken();
        String error = "ERROR\n" + "Token: " + errToken.getToken() + "\nLexeme: " + errToken.getLexeme() + "\nExpected Tokens: ";

        for ( Compiler_LA.Type token : expected ){
            error += token;
        }
        System.out.println(error);
    }
}
