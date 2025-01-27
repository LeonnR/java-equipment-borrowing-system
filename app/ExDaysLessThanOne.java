public class ExDaysLessThanOne extends Exception{
    public ExDaysLessThanOne() { super("The number of days must be at least 1."); }
    public ExDaysLessThanOne(String message) { super(message); } 
}
