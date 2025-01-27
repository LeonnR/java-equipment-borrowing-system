public class ExEquipmentCodeInUse extends Exception{
    public ExEquipmentCodeInUse(String usedEquipmentCode, String usedEquipmentName) { super("Equipment code already in use: " + usedEquipmentCode + " " + usedEquipmentName); } 
}
