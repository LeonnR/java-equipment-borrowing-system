public class ExWrongCommand extends Exception{
    public ExWrongCommand() { super("Unknown command - ignored."); }
    public ExWrongCommand(String message) { super(message); } 
}
