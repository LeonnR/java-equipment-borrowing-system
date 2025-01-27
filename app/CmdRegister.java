public class CmdRegister extends RecordedCommand{
    private Member m;

    @Override
    public void execute(String[] cmdParts){
        try{
            Club c = Club.getInstance();
            if(cmdParts.length < 3){
                throw new ExInsufficientArguments();
            }
            //register 010 helena
            for(Member m : c.getAllMembers()){
                if(m.getID().equals(cmdParts[1])){
                    throw new ExMemberExists(cmdParts[1], m.getName());
                }
            }
            m = new Member(cmdParts[1], cmdParts[2]);
            addUndoCommand(this);
            clearRedoList();
            System.out.println("Done.");
        }catch(ExInsufficientArguments e){
            System.out.println(e.getMessage());
        }catch(ExMemberExists e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe(){
        Club c = Club.getInstance();
        c.removeMember(m);
        addRedoCommand(this);
    }

    @Override
    public void redoMe(){
        Club c = Club.getInstance();
        c.addMember(m);
        addUndoCommand(this);
    }
}   
