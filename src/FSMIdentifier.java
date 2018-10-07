import java.util.ArrayList;

public class FSMIdentifier {
    private ArrayList<Integer> possStates;
    private Integer firstState;
    private ArrayList<Integer> acceptStates;

    Integer[][] states = new Integer[][]{
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, null, null, null, null, null, null, null, null, null, null, null},
            { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, null },
            { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, null },
            { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, null },
            { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, null },
    };

    FSMIdentifier(){
        possStates = new ArrayList<>();
        acceptStates = new ArrayList<>();

        possStates.add(0);
        possStates.add(1);
        possStates.add(2);
        possStates.add(3);
        possStates.add(4);
        possStates.add(5);
        possStates.add(6);
        possStates.add(7);
        possStates.add(8);
        possStates.add(9);

        acceptStates.add(4);
        acceptStates.add(3);
        firstState = 0;

    }

    public boolean isIdentifier(String lexeme){
        Integer currState = firstState;
        char currCharacter;
        Integer nextState;

        for(int i = 0; i < lexeme.length(); i++){
            currCharacter = lexeme.charAt(i);
            nextState = nextState(currState, currCharacter);

            if(nextState == null){
                return false;
            }

            currState = nextState;
        }

        return acceptStates.contains(currState);
    }

    public Integer nextState(int currState, char input){
        Integer col;
        Integer nextState = null;

        col = char_to_col(input);

        nextState = states[currState][col];

        return nextState;
    }

    public Integer char_to_col(char input) {
        Integer colNum = null;

        switch (Character.toUpperCase(input)) {
            case 'A':
                colNum = 0;
                break;
            case 'B':
                colNum = 1;
                break;
            case 'C':
                colNum = 2;
                break;
            case 'D':
                colNum = 3;
                break;
            case 'E':
                colNum = 4;
                break;
            case 'F':
                colNum = 5;
                break;
            case 'G':
                colNum = 6;
                break;
            case 'H':
                colNum = 7;
                break;
            case 'I':
                colNum = 8;
                break;
            case 'J':
                colNum = 9;
                break;
            case 'K':
                colNum = 10;
                break;
            case 'L':
                colNum = 11;
                break;
            case 'M':
                colNum = 12;
                break;
            case 'N':
                colNum = 13;
                break;
            case 'O':
                colNum = 14;
                break;
            case 'P':
                colNum = 15;
                break;
            case 'Q':
                colNum = 16;
                break;
            case 'R':
                colNum = 17;
                break;
            case 'S':
                colNum = 18;
                break;
            case 'T':
                colNum = 19;
                break;
            case 'U':
                colNum = 20;
                break;
            case 'V':
                colNum = 21;
                break;
            case 'W':
                colNum = 22;
                break;
            case 'X':
                colNum = 23;
                break;
            case 'Y':
                colNum = 24;
                break;
            case 'Z':
                colNum = 25;
                break;
            case '0':
                colNum = 26;
                break;
            case '1':
                colNum = 27;
                break;
            case '2':
                colNum = 28;
                break;
            case '3':
                colNum = 29;
                break;
            case '4':
                colNum = 30;
                break;
            case '5':
                colNum = 31;
                break;
            case '6':
                colNum = 32;
                break;
            case '7':
                colNum = 33;
                break;
            case '8':
                colNum = 34;
                break;
            case '9':
                colNum = 35;
                break;
            default:
                colNum = 36;
        }
        return colNum;
    }
}

