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

        for (int i = 0; i < index; i++){
            if (codeListing[i].instruction.equals(instr)) {
                return count;
            }
            count++;
        }

        return -5;
    }

    public void checkJumps(){
        String jump = "JUMP";
        String label = "LABEL";
        for (int i = 0; i < index; i++){
            if (codeListing[i].address == -1){
                codeListing[i].address = getIndex(jump) + 1;
            }
            else if (codeListing[i].address == -3){
                codeListing[i].address = getIndex(label);
            }
        }
    }

    public void printAssemblyTable(){
        int count = 1;
        System.out.println("ASSEMBLY CODE");
        for (int i = 0; i < index; i++){
            if(codeListing[i].address != 0){
                System.out.println(count + "     " + codeListing[i].instruction + "      " + codeListing[i].address);
            }
            else{

                System.out.println(count + "     " + codeListing[i].instruction);
            }

            count++;
        }
    }
}
