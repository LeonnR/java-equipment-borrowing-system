public class ExNoAvailableEquipmentSet extends Exception{
    public ExNoAvailableEquipmentSet() { super("There is no available set of this equipment for the command."); }
    public ExNoAvailableEquipmentSet(String message) { super(message); } 
}
