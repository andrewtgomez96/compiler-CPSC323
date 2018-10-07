import java.util.Scanner;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;

public class main {

    public static final char[] separatorsAndOps = { '(', ')', '{', '}', '[', ']', ',', ';', ':', '=', '+', '-', '/', '*', '>', '<', '|' };
    public static final String[] dubOps = { "=<", "=>", "^=", "==" };
    public static ArrayList<String> separatedLexemes = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        BufferedWriter writer =  new BufferedWriter( new FileWriter("output.txt"));
        writer.write("Token" + "\t\t\t" + "Lexeme");
        writer.newLine();

        File file = new File("test.txt");
        Scanner input = new Scanner(file);

        String lexeme = "", preLexeme = "";
        boolean isComment = false;
        int commStart, commEnd;

        Compiler_LA compiler = new Compiler_LA();
        Compiler_LA.Token thisToken;

        //compiler.removeComments(input);
        while(input.hasNext()){
            lexeme = input.next();

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
                        thisToken = compiler.lexer(lexemeOpSep);
                        preLexeme = "";
                        System.out.println(thisToken);
                        writer.write(thisToken.toString());
                        writer.newLine();
                    }
                }
            }


                parseSeparatorOperator(lexeme);

            //System.out.println(separatedLexemes.size());
            for(String lexemeOpSep: separatedLexemes) {
                thisToken = compiler.lexer(lexemeOpSep);
                System.out.println(thisToken);
                writer.write(thisToken.toString());
                writer.newLine();
            }
        }
        writer.flush();
        writer.close();
    }

    public static void parseSeparatorOperator(String lexeme){
        separatedLexemes.clear();
        int lexemeCounter = 0;
        StringBuilder lexemeHolder = new StringBuilder();
        boolean charSepOp = false;

        for(int i = 0; i < lexeme.length(); i++) {
            charSepOp = false;

            //System.out.println(lexeme.charAt(i));

            for (char separatorOp : separatorsAndOps) {
                if (lexeme.charAt(i) == separatorOp) {
                    charSepOp = true;
                    if(lexemeHolder.length() > 0) {
                        separatedLexemes.add(lexemeHolder.toString());
                        lexemeHolder.setLength(0);
                    }
                    separatedLexemes.add(String.valueOf(separatorOp));
                    break;
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

}
