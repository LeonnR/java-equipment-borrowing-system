public class ExMemberIsBorrowingSameEquipment extends Exception{
    public ExMemberIsBorrowingSameEquipment() { super("The member is currently borrowing a set of this equipment. He/she cannot borrow one more at the same time."); }
    public ExMemberIsBorrowingSameEquipment(String message) { super(message); } 
}
