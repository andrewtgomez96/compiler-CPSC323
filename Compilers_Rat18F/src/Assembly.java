public class Assembly {
    String instruction;
    int address;
    int index = 0;

    Assembly[] codeListing = new Assembly[1000];

    public Assembly (){ }

    public void addCode(String instruction, int address){
        Assembly temp = new Assembly();
        temp.address = address;
        temp.instruction = instruction;

        codeListing[index] = temp;
        index++;
    }

    public int getIndex(String instr) {
        int count = 1;

        for (Assembly x : codeListing){
            if (x.instruction.equals(instr)){
                return count;
            }
            count++;
        }
        return -5;
    }

    public void checkJumps(){
        int index;
        for (int i = 0; i < codeListing.length; i++){
            if (codeListing[i].address == -1){
                index = getIndex("JUMP");
                codeListing[i].address = index + 1;
                System.out.println("This is where i am. " + codeListing[i].address);
                System.out.println(i);
            }
            else if (codeListing[i].address == -3){
                codeListing[i].address = getIndex("LABEL");
            }
        }
    }

    public void printAssemblyTable(){
        int count = 1;
        System.out.println(index+1);
        System.out.println("ASSEMBLY CODE");
        for (Assembly x : codeListing){
            if(x.address != 0){
                System.out.println(count + "     " + x.instruction + "      " + x.address);
            }
            else{

                System.out.println(count + "     " + x.instruction);
            }

            count++;
        }
    }
}
