public class ExInvalidNewDay extends Exception{
    public ExInvalidNewDay() { super("Invalid new day.  The new day has to be later than the current date 6-Jan-2024."); }
    public ExInvalidNewDay(String currentDate) { super("Invalid new day.  The new day has to be later than the current date " + currentDate + "."); }  
}
