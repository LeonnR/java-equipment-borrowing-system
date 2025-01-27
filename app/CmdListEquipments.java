public class CmdListEquipments implements Command{

    @Override
    public void execute(String[] cmdParts){
        Club c = Club.getInstance();
        c.listClubEquipments();
    }
}
