public class ExEquipmentRecordNotFound extends Exception{
    public ExEquipmentRecordNotFound() { super("Equipment record not found."); }
    public ExEquipmentRecordNotFound(String message) { super(message); } 
}
