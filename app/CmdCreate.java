public class CmdCreate extends RecordedCommand{
    private Equipment e;

    @Override
    public void execute(String[] cmdParts){
        try{
            Club c = Club.getInstance();
            if(cmdParts.length < 3){
                throw new ExInsufficientArguments();
            }
            
            for(Equipment e : c.getAllEquipments()){
                if(e.getCode().equals(cmdParts[1])){
                    throw new ExEquipmentCodeInUse(cmdParts[1], e.getName());
                }
            }
            e = new Equipment(cmdParts[1], cmdParts[2]);
            addUndoCommand(this);
            clearRedoList();
            System.out.println("Done.");
        }catch(ExInsufficientArguments e){
            System.out.println(e.getMessage());
        }catch(ExEquipmentCodeInUse e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe(){
        Club c = Club.getInstance();
        c.removeEquipment(e);
        addRedoCommand(this);
    }

    @Override
    public void redoMe(){
        Club c = Club.getInstance();
        c.addEquipment(e);
        addUndoCommand(this);
    }
}   
