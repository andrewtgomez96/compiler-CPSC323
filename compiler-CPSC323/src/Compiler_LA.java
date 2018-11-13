import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class Compiler_LA {

    //public static final String[] operators = {"=", "+", "-", "/", "*", ">", "<", "|" };
   // public ArrayList<String> opsInLexeme = new ArrayList<String>();

    public static enum Type {
        IDENTIFIER, KEYWORD, INTEGER, REAL, OPERATOR, SEPARATOR, INVALID
    }

    public class Token{
        private Type token;
        private String  lexeme;
        private Integer lineNumber;
        private ArrayList<Type> expectedToken;

        Token(Type token, String lexeme, Integer lineNumber){
            this.token = token;
            this.lexeme = lexeme;
            this.lineNumber = lineNumber;
        }

        public void setToken(Type token){
            this.token = token;
        }
        public Type getToken() { return this.token; }

        public void setLexeme(String lexeme){
            this.lexeme = lexeme;
        }
        public String getLexeme() { return this.lexeme; }

        public void setLineNumber(Integer lineNumber){
            this.lineNumber = lineNumber;
        }
        public Integer getLineNumber() { return this.lineNumber; }

        public void addExpectedToken(Type token) { expectedToken.add(token); }
        public ArrayList getExpectedToken() { return expectedToken; }

        @Override
        public String toString(){
            return token.toString() + "\t\t\t" + this.lexeme;
        }
    }

    public Token lexer(String lexeme, Integer lineNum){

        //switch case for separators
        switch(lexeme){
            case "(":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case ")":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case ",":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case ";":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case "{":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case "}":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case "[":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case "]":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case ".":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
            case ":":
                return new Token(Type.SEPARATOR, lexeme, lineNum);
        }

        //switch case for keywords
        switch(lexeme){
            case "while":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "whileend":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "if":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "else":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "ifend":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "do":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "function":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "$$":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "int":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "boolean":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "true":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "false":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "real":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "return":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "get":
                return new Token(Type.KEYWORD, lexeme, lineNum);
            case "put":
                return new Token(Type.KEYWORD, lexeme, lineNum);
        }

        //switch case for operators
        switch(lexeme){
            case "=":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "+":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "-":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "/":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "*":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case ">":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "<":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "|":
                return new Token(Type.OPERATOR, lexeme, lineNum);
        }

        //DOUBLE OPERATORS
        switch(lexeme) {
            case "=<":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "=>":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "^=":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "==":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "&&":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "+=":
                return new Token(Type.OPERATOR, lexeme, lineNum);
            case "-=":
                return new Token(Type.OPERATOR, lexeme, lineNum);
        }

        FSMInteger fsmInt = new FSMInteger();
        FSMReal fsmReal = new FSMReal();
        FSMIdentifier fsmId = new FSMIdentifier();

        if(fsmInt.isInteger(lexeme)){
            return new Token(Type.INTEGER, lexeme, lineNum);
        }
        else if(fsmReal.isReal(lexeme)){
            return new Token(Type.REAL, lexeme, lineNum);
        }
        else if(fsmId.isIdentifier(lexeme)){
            return new Token(Type.IDENTIFIER, lexeme, lineNum);
        }
        else
            return new Token(Type.INVALID, lexeme, lineNum);
    }


}
