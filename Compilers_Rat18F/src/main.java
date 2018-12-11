import java.util.Scanner;
import java.io.*;
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
       // writer.write("Token" + "\t\t\t" + "Lexeme");
       // writer.newLine();

        //HERE ARE THE THREE LINES I ADDED TO TAKE IN FILE FROM USER
        System.out.print("Please input the filename that you want to test in the directory \nExample: test.txt\n:");
        Scanner readInFile = new Scanner(System.in);
        String fileName = readInFile.next();


        File file = new File(fileName);
        Scanner input = new Scanner(file);


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

        //while we have another lexeme to verify loop
        while(input.hasNext()){
            lexeme = input.next();
            if (input.hasNext(System.lineSeparator()) ) {lineNum++; System.out.println("A NEW LINE"); continue;}
            //ignore comments comments
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
        }



        //We call the parser on an iteration over the list of all lexemes in the program
        for(int i = 0; i < allLexemes.size() - 1; i++) {
            //CALL PARSER HERE FOR PRE, CURR AND POST LEXEME
            if(i == 0){
                parserHolder = Parser.parse(allLexemes.get(i), allLexemes.get(i), allLexemes.get(i + 1));
            } else {
                parserHolder = Parser.parse(allLexemes.get(i - 1), allLexemes.get(i), allLexemes.get(i + 1));
            }
            /*
            //This is where the output format printing occurs
            System.out.println(allLexemes.get(i));
            writer.write(allLexemes.get(i).toString());
            writer.newLine();
            System.out.println(parserHolder);
            writer.write(parserHolder);
            writer.newLine();
            */
        }

        //parse the very last input
        parserHolder = Parser.parse(allLexemes.get(allLexemes.size()-2), allLexemes.get(allLexemes.size() - 1), allLexemes.get(allLexemes.size() - 1));
        System.out.println(allLexemes.get(allLexemes.size()-1));
       // writer.write(allLexemes.get(allLexemes.size()-1).toString());
        //writer.newLine();
       // System.out.println(parserHolder);
       // writer.write(parserHolder);
       // writer.newLine();

        //print the number of errors in program
       // System.out.println("Your program parsed with a total of: " + Parser.errors + " errors\n");
       // writer.write("Your program parsed with a total of: ");
      //  String errorNum = Integer.toString(Parser.errors);
      //  writer.write(errorNum + " errors");
      //  writer.newLine();

        writer.write(Parser.printTables());



        //close file for proper program shutdown
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
