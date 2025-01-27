public class ExMemberExists extends Exception{
    public ExMemberExists(String usedMemberID, String usedMemberName) { super("Member ID already in use: " + usedMemberID + " " + usedMemberName); } 
}
