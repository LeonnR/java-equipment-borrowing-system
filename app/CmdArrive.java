public class CmdArrive extends RecordedCommand{
    private Equipment myEquipment;

    @Override
    public void execute(String[] cmdParts) {
        Club c = Club.getInstance();
        String code = cmdParts[1];

        try{
            boolean equipmentExists = false;
            for(Equipment e : c.getAllEquipments()){
                if(e.getCode().equals(code)){
                    myEquipment = e;
                    e.addSet();
                    equipmentExists = true;
                    break;
                }
            }

            if(equipmentExists){
                addUndoCommand(this);
                clearRedoList();
                System.out.println("Done.");
            }else{
                throw new ExMissingEquipmentRecord(code);
            }
        }catch(ExMissingEquipmentRecord e){
            System.out.println(e.getMessage());
        }
       
    }

    @Override
    public void undoMe() {
        if (myEquipment != null && myEquipment.getSets() > 0) {
            myEquipment.removeSet(myEquipment.getCode(), Integer.toString(myEquipment.getSets()));
            addRedoCommand(this);
        }
    }

    @Override
    public void redoMe(){
        myEquipment.addSet();
        addUndoCommand(this);
    }
}
