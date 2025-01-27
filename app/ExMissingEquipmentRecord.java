public class ExMissingEquipmentRecord extends Exception{
    public ExMissingEquipmentRecord(String equipmentCode) { super("Missing record for Equipment " + equipmentCode + ". Cannot mark this item arrival." ); } 
}
