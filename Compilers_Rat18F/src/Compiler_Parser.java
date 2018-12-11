import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Compiler_Parser {

    private HashMap<Compiler_LA.Type, String> expectedToken = new HashMap<>();
    private StringBuilder outputforTrio;
    public int errors;
    int location = 5000;
    boolean currentIf = false;
    boolean currentWhile = false;
    Assembly asmTemp = new Assembly();
    Symbol symTemp = new Symbol();
    String save;


    public Compiler_Parser(){
        initHashMap();
        errors = 0;
    }

    //implement an expected token to pass into the error() function
    public String parse(Compiler_LA.Token preToken, Compiler_LA.Token currToken, Compiler_LA.Token postToken) throws IOException {

        outputforTrio = new StringBuilder();


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
                    if (!symTemp.checkValue(currToken.getLexeme())){
                        symTemp.addSymbol(preToken.getLexeme(), currToken.getLexeme(), location);
                        location++;
                    }
                    else{
                        outputforTrio.append("Error! This identifier, " + currToken.getLexeme() +  ", already exits.");
                    }
                    System.out.println(preToken.getLexeme() + "     " + postToken.getToken());
                    if (preToken.getLexeme().equals("=") && postToken.getToken() == Compiler_LA.Type.SEPARATOR){
                       asmTemp.addCode("POPM", symTemp.getLocation(save));
                       save = "";
                    }
                    if (postToken.getLexeme().equals(",")){
                        save = preToken.getLexeme();
                    }
                    if(postToken.getLexeme().equals(",")) {
                        outputforTrio.append("<Decleration List> -> <Decleration>;<Declaration List'>\n" + "<Decleration> -> <int | boolean | real > <IDs>\n" + "<IDs> -> <Identifier><IDs'>\n" + "<IDs'> -> ,<IDs>\n");
                    } else {
                        outputforTrio.append("<Decleration List> -> <Decleration>;<Declaration List'>\n" + "<Decleration> -> <int | boolean | real> <IDs>\n" + "<IDs> -> <Identifier><IDs'>\n");
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

                    if (!symTemp.checkValue(currToken.getLexeme())){
                        symTemp.addSymbol(save, currToken.getLexeme(), location);
                        location++;
                    }
                    else{
                        outputforTrio.append("Error! This identifier, " + currToken.getLexeme() +  ", already exits.");
                    }
                    if (postToken.getLexeme().equals(";")){
                        save = "";
                    }
                    outputforTrio.append("<IDs'>  ->  ,<IDs>\n");
                } else if(preToken.getLexeme().equals("=")){
                    outputforTrio.append("<Assign> -> <Identifier> = <Expression>;\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>\n");



                    if (postToken.getToken() == Compiler_LA.Type.SEPARATOR) {
                        asmTemp.addCode("PUSHM", symTemp.getLocation(currToken.getLexeme()));
                        asmTemp.addCode("POPM", symTemp.getLocation(save));
                        save = "";
                    }

                } else if(preToken.getLexeme().equals(">") || preToken.getLexeme().equals("<") ){
                    outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>\n");
                } else if(preToken.getLexeme().equals("(") || preToken.getLexeme().equals(")") ){
                    if(postToken.getLexeme().equals(",")) {
                        outputforTrio.append("<Primary> -> <Identifier>(<IDs>)" + "<IDs> -> <Identifier><IDs Prime>\n" + "<IDs Prime> -> ,<IDs>\n");
                    } else if(postToken.getLexeme().equals(")")){
                        outputforTrio.append("<Primary> -> <Identifier>(<IDs>)\n" + "<IDs> -> <Identifier><IDs Prime>\n" + "<IDs Prime> -> ɛ\n");
                    } else {
                        outputforTrio.append("<Function> -> function <Identifier> (<Opt Parameter List>)<Opt Declaration List><Body>\n");
                    }
                } //else if(preToken.getLexeme().equals("") ){
                    //possibly need to add one prod rule here for put but it should be counted in keyword section
                else if(postToken.getLexeme().equals("=") ){
                    outputforTrio.append("<Statement>-> <Assign>\n" + "<Assign> -><Identifier> = <Expression>;\n");
                } else {
                    error(preToken, currToken);
                }
                break;
            case INTEGER:
                if(preToken.getLexeme().equals("-")){
                    outputforTrio.append("<Factor> -> -<Primary>\n" + "<Primary> -> <Integer>\n");
                    asmTemp.addCode("SUB", -10);
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



                    if (postToken.getToken() == Compiler_LA.Type.SEPARATOR) {
                        asmTemp.addCode("PUSHI", Integer.parseInt(currToken.getLexeme()));
                        asmTemp.addCode("POPM", symTemp.getLocation(save));
                        save = "";
                    }
                } else if(preToken.getLexeme().equals("return")){
                    outputforTrio.append("<Return> -> return <Expression>;\n" + "<Expression> -> <Term><Expression'>\n" + "<Factor> -> <Primary>\n" + "<Primary> -> <Integer>\n");
                }else {
                    error(preToken, currToken);
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
                    error(preToken, currToken);
                }
                break;
            case OPERATOR:
                if(currToken.getLexeme().equals("-") || currToken.getLexeme().equals("+")){
                    //according to rules we need to check for all primary
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false") || preToken.getLexeme().equals("(") || preToken.getLexeme().equals(")")) {
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                        //pre and post follow same rules so we passed the - or + check
                            if(currToken.getLexeme().equals("-")) {
                                outputforTrio.append("<Expression> -> <Term><Expression'>\n" + "<Expression'> -> -<Term><Expression'>\n");
                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("SUB", -10);
                                if (save != "STDOUT"){
                                    asmTemp.addCode("POPM", symTemp.getLocation(save));
                                    save = "";
                                }
                                else {
                                    asmTemp.addCode("STDOUT", -10);
                                    save = "";
                                }
                            } else {
                                outputforTrio.append("<Expression> -> <Term><Expression'>\n" + "<Expression'> -> +<Term><Expression'>\n");
                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("ADD", -10);
                                if (save != "STDOUT"){
                                    asmTemp.addCode("POPM", symTemp.getLocation(save));
                                    save = "";
                                }
                                else {
                                    asmTemp.addCode("STDOUT", -10);
                                    save = "";
                                }
                            }
                        } else {
                            error(preToken, currToken);
                        }
                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals("*") || currToken.getLexeme().equals("/")){
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false") || preToken.getLexeme().equals("(") || preToken.getLexeme().equals(")")) {
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                            //pre and post follow same rules so we passed the * or / check
                            if(currToken.getLexeme().equals("/")) {
                                outputforTrio.append("<Term> -> <Factor><Term'>\n" + "<Term'> -> /<Factor><Term'>\n");

                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("DIV", -10);
                                if (save != "STDOUT"){
                                    asmTemp.addCode("POPM", symTemp.getLocation(save));
                                    save = "";
                                }
                                else {
                                    asmTemp.addCode("STDOUT", -10);
                                    save = "";
                                }

                            } else {
                                outputforTrio.append("<Term> -> <Factor><Term'>\n" + "<Term'> -> *<Factor><Term'>\n");

                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("MUL", -10);
                                if (save != "STDOUT"){
                                    asmTemp.addCode("POPM", symTemp.getLocation(save));
                                    save = "";
                                }
                                else {
                                    asmTemp.addCode("STDOUT", -10);
                                    save = "";
                                }
                            }
                        } else {
                            error(preToken, currToken);
                        }
                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals(">") || currToken.getLexeme().equals("<") || currToken.getLexeme().equals("=<") || currToken.getLexeme().equals("=>") || currToken.getLexeme().equals("^=") || currToken.getLexeme().equals("==")){
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false") || preToken.getLexeme().equals("(")) {
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                            //pre and post follow same rules so we passed the relop check
                            if(currToken.getLexeme().equals(">")) {
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> >\n");
                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("GRT", -10);
                            } else if(currToken.getLexeme().equals("<")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> <\n");
                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("LES", -10);
                            } else if(currToken.getLexeme().equals("=<")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> =<\n");
                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("LEQ", -10);
                            } else if(currToken.getLexeme().equals("=>")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> =>\n");
                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("GEQ", -10);
                            } else if(currToken.getLexeme().equals("^=")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> ^=\n");
                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("NEQ", -10);
                            } else if(currToken.getLexeme().equals("==")){
                                outputforTrio.append("<Condition> -> <Expression> <Relop> <Expression>\n" + "<Relop> -> ==\n");
                                if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(preToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(preToken.getLexeme()));
                                }

                                if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                                    asmTemp.addCode("PUSHM", symTemp.getLocation(postToken.getLexeme()));
                                }
                                else{
                                    asmTemp.addCode("PUSHI", Integer.parseInt(postToken.getLexeme()));
                                }
                                asmTemp.addCode("EQU", -10);
                            }
                        } else {
                            error(preToken, currToken);
                        }
                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals("=")){
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER){
                        if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                            outputforTrio.append("<Assign> -> <Identifier> = <Expression>\n");
                            save = preToken.getLexeme();

                        } else {
                            error(preToken, currToken);
                        }
                    } else {
                        error(preToken, currToken);
                    }

                }
                break;
            case KEYWORD:
                if(currToken.getLexeme().equals("int") ||currToken.getLexeme().equals("real") || currToken.getLexeme().equals("boolean") ) {
                    if(preToken.getLexeme().equals(":")){
                        outputforTrio.append("<Parameter> -> <IDs>:<Qualifier>\n" + "<Qualifier> -> " + currToken.getLexeme() + "\n");
                    } else if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER) {
                        outputforTrio.append("<Declaration> -> <Qualifier> <IDs>\n" + "<Qualifier> -> " + currToken.getLexeme() + "\n");
                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals("return")) {
                    if(postToken.getLexeme().equals(";")) {
                        outputforTrio.append("<Statement> -> <Return>\n" + "   <Return> -> return;\n");
                    } else if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.REAL || postToken.getToken() == Compiler_LA.Type.INTEGER || postToken.getLexeme().equals("true") || postToken.getLexeme().equals("false") || postToken.getLexeme().equals("(")){
                        outputforTrio.append("<Statement> -> <Return>\n" + "<Return> -> return<Expression>;\n");
                    } else {
                        error(preToken, currToken);
                    }

                } else if(currToken.getLexeme().equals("function")) {
                    if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER) {
                        outputforTrio.append("<Function Definitions> -> <Function><Function Definitions Prime>\n" + "<Function> -> function  <Identifier>(<Opt Parameter List>)<Opt Declaration List><Body>\n");
                    } else {
                        error(preToken, currToken);
                    }

                } else if(currToken.getLexeme().equals("put") || currToken.getLexeme().equals("get")) {
                    if(postToken.getLexeme().equals("(")) {
                        if(currToken.getLexeme().equals("put")){
                            outputforTrio.append("<Statement> -> <Print>\n" + "<Print> -> put(<Expression>);\n");
                            save = "STDOUT";
                        } else {
                            outputforTrio.append("<Statement> -> <Scan>\n" + "<Scan> -> get(<IDs>);\n");
                            asmTemp.addCode("STDIN", -10);
                        }
                    } else {
                        error(preToken, currToken);
                    }

                } else if(currToken.getLexeme().equals("true") || currToken.getLexeme().equals("false")) {
                    if(preToken.getLexeme().equals("-")){
                        outputforTrio.append("<Factor> -> -<Primary>\n" + "<Primary> -> " + currToken.getLexeme() + "\n");
                    } else {
                        outputforTrio.append("<Factor> -> <Primary>\n" + "<Primary> -> " + currToken.getLexeme() + "\n");
                    }

                } else if(currToken.getLexeme().equals("if") || currToken.getLexeme().equals("while")) {
                    if(postToken.getLexeme().equals("(")) {
                        if(currToken.getLexeme().equals("if")) {
                            outputforTrio.append("<Statement> -> <If>\n" + "<If> -> if (<Condition>) <Statement> ifend\n");
                        } else {
                            outputforTrio.append("<Statement> -> <While>\n" + "<While> ->  while(<Condition>) <Statement> whileend\n");
                            asmTemp.addCode("LABEL", -10);
                            currentWhile = true;
                        }
                    } else {
                        error(preToken, currToken);
                    }

                } else if(currToken.getLexeme().equals("else") || currToken.getLexeme().equals("ifend") || currToken.getLexeme().equals("whileend")) {
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("(") || preToken.getLexeme().equals("}") || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false") || preToken.getLexeme().equals("=")) {
                        if(currToken.getLexeme().equals("else")) {
                            outputforTrio.append("<Statement> -> <If>\n" + "<If> -> if (<Condition>) <Statement> else <Statement> ifend\n");
                        } else if(currToken.getLexeme().equals("ifend")){
                            outputforTrio.append("<If> -> if (<Condition>) <Statement> else <Statement> ifend\n");
                            asmTemp.addCode("LABEL", -10);
                            currentIf = false;
                        } else {
                            outputforTrio.append("<While> ->  while(<Condition>) <Statement> whileend\n");
                            asmTemp.addCode("JUMP", asmTemp.getIndex("LABEL"));
                            currentWhile = false;
                        }
                    } else {
                        error(preToken, currToken);
                    }
                } else {
                    error(preToken, currToken);
                }
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
                            asmTemp.addCode("POPM", symTemp.getLocation(postToken.getLexeme()));
                        }
                    } else if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER && postToken.getToken() == Compiler_LA.Type.IDENTIFIER) {
                        outputforTrio.append("<Factor> -> <Primary>\n" + "<Primary> -> <Identifier>(<IDs>)\n");
                    } else if(preToken.getLexeme().equals("+") || preToken.getLexeme().equals("-") || preToken.getLexeme().equals("*") || preToken.getLexeme().equals("/")) {
                        outputforTrio.append("<Factor> -> <Primary>\n" + "<Primary> -> (<Expression>)\n");
                    } else if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER) {
                        if( postToken.getLexeme().equals(")")) {   //I BELIEVE THIS PART STILL NEEDS TO BE ADJUSTED FOR FUNCTIONS THAT HAVE PARAMETERS
                            outputforTrio.append("<Function Definitions> -> <Function><Function Definitions'>\n" + "<Function> -> function <Identifier> (<Opt Parameter List>)<Opt Declaration List><Body>\n");
                        }
                    } else if(preToken.getLexeme().equals("while") || preToken.getLexeme().equals("if")) {
                        outputforTrio.append("<Condition> -> <Expression><Relop><Expression>\n");
                    } else {
                        error(preToken, currToken);
                    }

                } else if(currToken.getLexeme().equals(")")) {
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false") ||  preToken.getLexeme().equals(")")){
                        outputforTrio.append("<Factor> -> <Primary>\n" + "<Primary> -> (<Expression>)\n");

                        if (postToken.getLexeme().equals("{")){
                            if(currentIf) {
                                asmTemp.addCode("JUMPZ", -3);
                            }
                            else if (currentWhile){
                                asmTemp.addCode("JUMPZ", -1);
                            }
                        }
                    } else if(preToken.getLexeme().equals("int") || preToken.getLexeme().equals("boolean")|| preToken.getLexeme().equals("real") || preToken.getLexeme().equals("(")){
                        outputforTrio.append("<Function Definitions> -> <Function><Function Definitions'>\n" + "<Function> -> function <Identifier> (<Opt Parameter List>)<Opt Declaration List><Body>\n");
                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals("{")) {
                    if(postToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.KEYWORD  ) {
                        if(preToken.getLexeme().equals(")")) {
                            outputforTrio.append("<Function> -> function<Identifier> (<Opt Parameter List>)<Opt Declaration List><Body>\n" + "<Body> -> {<Statement List>}\n");
                        } else {
                            outputforTrio.append("<Statement> -> <Compound>\n" + "<Compound> -> {<Statement List>}\n");
                        }
                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals("}")) {
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.SEPARATOR || preToken.getLexeme().equals("ifend") || preToken.getLexeme().equals("whileend")) {
                        outputforTrio.append("<Statement> -> <Compound>\n" + "<Compound> -> {<Statement List>}\n");

                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals(";")) {   //NOT SURE HOW TO DO THIS
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || preToken.getToken() == Compiler_LA.Type.REAL || preToken.getToken() == Compiler_LA.Type.INTEGER || preToken.getLexeme().equals("true") || preToken.getLexeme().equals("false")) {
                        outputforTrio.append("<Statement> -> <Assign>\n" + "<Assign> -> <Identifier>=<Expression>;\n");
                    } else if(preToken.getLexeme().equals(")")) {
                        outputforTrio.append("<Statement> -> <Print>\n" + "<Print> -> put(<Expression>);\n");
                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals(":")) {
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER && (postToken.getLexeme().equals("boolean") || postToken.getLexeme().equals("int") || postToken.getLexeme().equals("real"))) {
                            outputforTrio.append("<Parameter List> -> <Parameter><Parameter List'>\n" + "<Parameter> -> <IDs>:<Qualifier>\n");
                    } else {
                        error(preToken, currToken);
                    }
                } else if(currToken.getLexeme().equals(",")) {
                    if(preToken.getToken() == Compiler_LA.Type.IDENTIFIER || postToken.getToken() == Compiler_LA.Type.IDENTIFIER) {
                        outputforTrio.append("<IDs> -> <Identifier><IDs'>\n" + "<IDs'> -> ,<IDs>\n");
                    } else {
                        error(preToken, currToken);
                    }

                } else if(currToken.getLexeme().equals("$$")) {  //need to check for end and begin
                    outputforTrio.append("<Rat18F> -> <Opt Function Definitions> $$ <Opt Declaration List> <Statement List> $$\n");
                }
                break;
            case INVALID:
                error(preToken, currToken);
                break;
        }



        return outputforTrio.toString();
    }

    //utility function that inits a hashmap for error printing of expected lexeme using follow sets
    public void initHashMap() {
        expectedToken.put(Compiler_LA.Type.IDENTIFIER, " (, ), +, -, /, *, :, ;, >, <, ==, ^=, =>, =< or , ");
        expectedToken.put(Compiler_LA.Type.KEYWORD, " IDENTIFIER, (, $$, INTEGER, REAL, ) or ,");
        expectedToken.put(Compiler_LA.Type.INTEGER, " ), +, -, /, *, ;, >, <, ==, ^=, =>, =< or , ");
        expectedToken.put(Compiler_LA.Type.REAL, " ), +, -, /, *, ;, >, <, ==, ^=, =>, =< or , ");
        expectedToken.put(Compiler_LA.Type.OPERATOR, " IDENTIFIER, (, INTEGER, REAL, true, false ");
        expectedToken.put(Compiler_LA.Type.SEPARATOR, "IDENTIFIER, INTEGER, REAL, KEYWORD, $$ ");
    }

    //adds an error print to any lexeme that could not fit the production rules applied
    private void error(Compiler_LA.Token preToken, Compiler_LA.Token errToken) {

        // print error
        outputforTrio.append("ERROR: at line " + errToken.getLineNumber() +" Expected " + expectedToken.get(preToken.getToken()) + "but got token: " + errToken.getToken() + " " + errToken.getLexeme() + "\n");

       errors++; // increment error counter
    }

    public String printTables() {
        String result = "";

        asmTemp.checkJumps();
        result += asmTemp.printAssemblyTable();

        result += "\n\n" + symTemp.printSymbolTable();

        return result;
    }
}
