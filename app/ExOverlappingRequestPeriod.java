public class ExOverlappingRequestPeriod extends Exception {
    public ExOverlappingRequestPeriod(){ super("The period overlaps with a current period that the member requests the equipment.");}
    public ExOverlappingRequestPeriod(String message){ super(message);}
}

