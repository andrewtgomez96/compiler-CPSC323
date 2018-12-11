import java.util.ArrayList;

public class Symbol {
    private int location;
    private String type;
    private String identifier;
    public ArrayList<Symbol> symbolTable = new ArrayList<>();

    public Symbol(){ }

    public void addSymbol(String type, String identifier, int location){
        Symbol temp = new Symbol();
        temp.type = type;
        temp.identifier = identifier;
        temp.location = location;
        symbolTable.add(temp);

    }

    public int getLocation(String id){
        for (Symbol x : symbolTable){
            if (x.getIdentifier().equals(id)){
                return x.location;
            }
        }
        return 0;
    }

    public String getType(){
        return type;
    }

    public String getIdentifier(){
        return identifier;
    }

    public boolean checkValue(String id){
        for (Symbol x : symbolTable){
            if (x.identifier.equals(id)){
                return true;
            }
        }
        return false;
    }

    public void printSymbolTable(){
        System.out.println("SYMBOL TABLE");
        System.out.println(" Identifier     MemoryLocation      Type");
        for (Symbol x : symbolTable){
            System.out.println(x.identifier + "     " + x.location + "      " + x.type);
        }
    }



}
