import java.util.ArrayList;

public class FSMIdentifier {
    private ArrayList<Integer> possStates;
    private Integer firstState;
    private ArrayList<Integer> acceptStates;

    Integer[][] states = new Integer[][]{
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, null, null },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, null },
            { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, null, null },
            { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, null, null }
    };

    FSMReal(){
        possStates = new ArrayList<>();
        acceptStates = new ArrayList<>();

        possStates.add(1);
        possStates.add(2);
        possStates.add(3);
        possStates.add(4);

        acceptStates.add(3);
        firstState = 1;

    }

    public boolean isReal(String lexeme){
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

        switch (input) {
            case '0':
                colNum = 0;
                break;
            case '1':
                colNum = 1;
                break;
            case '2':
                colNum = 2;
                break;
            case '3':
                colNum = 3;
                break;
            case '4':
                colNum = 4;
                break;
            case '5':
                colNum = 5;
                break;
            case '6':
                colNum = 6;
                break;
            case '7':
                colNum = 7;
                break;
            case '8':
                colNum = 8;
                break;
            case '9':
                colNum = 9;
                break;
            case '.':
                colNum = 10;
                break;
            default:
                colNum = 11;
        }
        return colNum;
    }
}

