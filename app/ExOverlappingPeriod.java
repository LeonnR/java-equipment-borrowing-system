public class ExOverlappingPeriod extends Exception {
    public ExOverlappingPeriod(){ super("The period overlaps with a current period that the member borrows / requests the equipment.");}
    public ExOverlappingPeriod(String message){ super(message);}
}

