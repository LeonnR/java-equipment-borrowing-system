public class CmdStartNewDay extends RecordedCommand{
    private String sOld, sNew;

    @Override
    public void execute(String[] cmdParts) {
        try {
            if (cmdParts.length < 2) {
                throw new ExInsufficientArguments();
            }
    
            SystemDate sd = SystemDate.getInstance();
            Day.validateDate(cmdParts[1]);
            
            sOld = sd.toString();
            sNew = cmdParts[1];
    
            sd.set(sNew);
            addUndoCommand(this);
            clearRedoList();
            System.out.println("Done.");

        } catch (ExInsufficientArguments e) {
            System.out.println(e.getMessage());
        } catch (ExInvalidNewDay e) {
            System.out.println(e.getMessage());
        } catch (ExInvalidDate e) {
            System.out.println(e.getMessage());
        } 
    }

    @Override
    public void undoMe(){
        SystemDate sd = SystemDate.getInstance();
        sd.set(sOld);
        addRedoCommand(this);
    }

    @Override
    public void redoMe(){
        SystemDate sd = SystemDate.getInstance();
        sd.set(sNew);
        addRedoCommand(this);
    }
}
