public class ExDaysIsNotInteger extends Exception{
    public ExDaysIsNotInteger() { super("Please provide an integer for the number of days."); }
    public ExDaysIsNotInteger(String message) { super(message); } 
}
