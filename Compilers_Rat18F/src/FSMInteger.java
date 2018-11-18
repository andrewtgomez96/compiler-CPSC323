import java.util.ArrayList;

public class FSMInteger {
    private ArrayList<Integer> possStates;
    private Integer firstState;
    private ArrayList<Integer> acceptStates;

    FSMInteger(){
       possStates = new ArrayList<>();
       acceptStates = new ArrayList<>();

       possStates.add(1);
       acceptStates.add(1);
       firstState = 1;

    }

    public boolean isInteger(String lexeme){
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

    //For the FSM of an integer the currState always loops back to 1 so we do not need to consider it in the decision of next state
    //The FSM only has one state which is 1
    public Integer nextState(int currState, char input){
        Integer newState = null;
        switch(input) {
            case '0':
                newState = 1;
                break;
            case '1':
                newState = 1;
                break;
            case '2':
                newState = 1;
                break;
            case '3':
                newState = 1;
                break;
            case '4':
                newState = 1;
                break;
            case '5':
                newState = 1;
                break;
            case '6':
                newState = 1;
                break;
            case '7':
                newState = 1;
                break;
            case '8':
                newState = 1;
                break;
            case '9':
                newState = 1;
                break;
        }
        return newState;
    }

}
