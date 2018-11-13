import java.util.Scanner;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.HashSet;
public class main {

    public static final char[] separatorsAndOps = { '(', ')', '{', '}', '[', ']', ',', ';', ':', '=', '+', '-', '/', '*', '>', '<', '|', '^' };
    public static final HashSet<String> dubOps = new HashSet<>();
    public static ArrayList<String> separatedLexemes = new ArrayList<>();
    public static ArrayList<Compiler_LA.Token> allLexemes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        initDubOps();
        BufferedWriter writer =  new BufferedWriter( new FileWriter("output.txt"));
        writer.write("Token" + "\t\t\t" + "Lexeme");
        writer.newLine();

        //HERE ARE THE THREE LINES I ADDED TO TAKE IN FILE FROM USER
        //System.out.print("Please input the filename that you want to test in the directory \nExample: test.txt\n:");
        Scanner readInFile = new Scanner(System.in);
        String fileName = readInFile.next();

        File file = new File("test.txt");
        Scanner input = new Scanner(file);

        Scanner getLine = new Scanner(file);

        //init local vars
        String lexeme = "", preLexeme = "";
        boolean isComment = false;
        int commStart, commEnd, lineNum = 1;

        //init local parser vars
        String parserHolder = "";

        //instantiate lexer class object in main
        Compiler_LA Compiler = new Compiler_LA();
        Compiler_LA.Token thisToken;

        //instantiate parser class object in main
        Compiler_Parser Parser = new Compiler_Parser();

        while(input.hasNext()){
            lexeme = input.next();
            String line = getLine.nextLine();
            String endOfLine = line.substring(line.lastIndexOf(" ")+1);

            if(lexeme.contains("[*")) {
                commStart = lexeme.indexOf("[*");
                preLexeme = lexeme.substring(0, commStart);

                isComment = true;
                while(isComment) {
                    if (lexeme.contains("*]")) {
                        commEnd = lexeme.indexOf("*]"); //index might be off
                        lexeme = lexeme.substring(commEnd + 1, lexeme.length() - 1);
                        isComment = false;
                    }
                    else{
                        if(input.hasNext()){
                            lexeme = input.next();
                        }
                    }
                }

                if(!preLexeme.isEmpty()) {
                    parseSeparatorOperator(preLexeme);
                    for(String lexemeOpSep: separatedLexemes) {
                        //CALL TO LEXER ON PRELEXEME HERE
                        thisToken = Compiler.lexer(lexemeOpSep, lineNum);
                        //add token to universal program list of ordered lexemes
                        allLexemes.add(thisToken);

                        preLexeme = "";
                        //THIS IS WHERE OLD PRINT HAPPENED FOR PROJ 1
                        //System.out.println(thisToken);
                       // writer.write(thisToken.toString());
                       // writer.newLine();
                    }
                }
            }

                parseSeparatorOperator(lexeme);

            for(String lexemeOpSep: separatedLexemes) {
                //CALL TO LEXER HERE
                thisToken = Compiler.lexer(lexemeOpSep, lineNum);
                //add token to universal program list of ordered lexemes
                allLexemes.add(thisToken);
                //THIS IS WHERE OLD PRINT HAPPENED FOR PROJ 1
                //System.out.println(thisToken);
                //writer.write(thisToken.toString());
                //writer.newLine();
            }

            if (endOfLine.equals(lexeme)){
                lineNum++;
            }
        }

        for(int i = 0; i < allLexemes.size() - 1; i++) {
            //CALL PARSER HERE
            if(i == 0){
                parserHolder = Parser.parse(allLexemes.get(i), allLexemes.get(i), allLexemes.get(i + 1));
                if (parserHolder.equals(null)){
                    return;
                }
            } else {
                parserHolder = Parser.parse(allLexemes.get(i - 1), allLexemes.get(i), allLexemes.get(i + 1));
                if (parserHolder.equals(null)){
                    return;
                }
            }
            //I think this print works have not tested and not sure
            System.out.println(allLexemes.get(i));
            writer.write(allLexemes.get(i).toString());
            writer.newLine();
            System.out.println(parserHolder);
            writer.write(parserHolder);
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }

    //function that allows for special case of operator or separator
    public static void parseSeparatorOperator(String lexeme){
        separatedLexemes.clear();
        StringBuilder lexemeHolder = new StringBuilder();
        boolean charSepOp = false;

        for(int i = 0; i < lexeme.length(); i++) {
            charSepOp = false;

            for (char separatorOp : separatorsAndOps) {
                if (lexeme.charAt(i) == separatorOp) {
                    charSepOp = true;
                    if(lexemeHolder.length() > 0) {
                        separatedLexemes.add(lexemeHolder.toString());
                        lexemeHolder.setLength(0);
                    }
                    //if is for double operator being one lexeme and else runs for any single operator
                    if( i + 1 < lexeme.length() && isValidDubOp(lexeme.charAt(i), lexeme.charAt(i+1))) {
                        lexemeHolder.append(String.valueOf(lexeme.charAt(i)));
                        lexemeHolder.append(String.valueOf(lexeme.charAt(i+1)));
                        separatedLexemes.add(lexemeHolder.toString());
                        lexemeHolder.setLength(0);
                        i++;
                        break;
                    } else {
                        separatedLexemes.add(String.valueOf(separatorOp));
                        break;
                    }
                }
            }

            if(!charSepOp) {
                lexemeHolder.append(String.valueOf(lexeme.charAt(i)));
            }
        }

        if(lexemeHolder.length() > 0) {
            separatedLexemes.add(lexemeHolder.toString());
        }
    }

    public static boolean isValidDubOp(Character firstChar, Character secondChar) {
        StringBuilder combineOps = new StringBuilder();
        boolean isValid = false;

        combineOps.append(firstChar);
        combineOps.append(secondChar);

        if(dubOps.contains(combineOps.toString())) {
            isValid = true;
        }

        return isValid;
    }

    public static void initDubOps() {
        dubOps.add("=<");
        dubOps.add("=>");
        dubOps.add("^=");
        dubOps.add("==");
        dubOps.add("+=");
        dubOps.add("-=");
        dubOps.add("*=");
        dubOps.add("/=");
    }

}
