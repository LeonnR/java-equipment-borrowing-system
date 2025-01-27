public class Day implements Cloneable, Comparable<Day> {
    private int year;
    private int month;
    private int day;
    private static final String MonthNames = "JanFebMarAprMayJunJulAugSepOctNovDec";

    public Day(int y, int m, int d){
        this.year = y;
        this.month = m;
        this.day = d;
    }

    public Day(String sDay){
        set(sDay);
    }

    @Override
    public Day clone(){
        Day copy = null;
        try{
            copy = (Day) super.clone();
        }catch(CloneNotSupportedException e){
            // TODO auto-generated catch block
            e.printStackTrace();
        }
        return copy;
    }

    public int getDay(){
        return this.day;
    }

    public int getMonth(){
        return this.month;
    }

    public int getYear(){
        return this.year;
    }

    //Check if a given year is a leap year
    static public boolean isLeapYear(int y){
        if(y % 400 == 0)
            return true;
        else if(y % 100 == 0)
            return false;
        else if(y % 4 == 0)
            return true;
        else
            return false;
    }

    static public String getAllMonthNames(){
        return MonthNames;
    }

    //Check if y, m, d valid
    static public boolean valid(int y, int m, int d){
        if(m < 1 || m > 12 || d < 1)
            return false;
        switch(m){
            case 1: case 3: case 5: case 7: 
            case 8: case 10: case 12:
                return d <= 31;

            case 4: case 6: case 9: case 11:
                return d <= 30;

            case 2:
                if(isLeapYear(y))
                    return d <= 29;
                else
                    return d <= 28;
        }
        return false;
    }

    public void set(String sDay){
        String[] sDayParts = sDay.split("-");
        this.day = Integer.parseInt(sDayParts[0]);
        this.year = Integer.parseInt(sDayParts[2]);
        this.month = MonthNames.indexOf(sDayParts[1])/3+1;
    }

    public Day addDays(int days) {
        int newDay = this.day;
        int newMonth = this.month;
        int newYear = this.year;
        
        newDay += days;

        while (true) {
            int daysInMonth = daysInMonth(newYear, newMonth);
            if (newDay <= daysInMonth) {
                break;
            }
            newDay -= daysInMonth;
            newMonth++;
            if (newMonth > 12) {
                newMonth = 1;
                newYear++;
            }
        }

        return new Day(newYear, newMonth, newDay);
    }

    private int daysInMonth(int year, int month) {
        switch (month) {
            case 1: case 3: case 5: case 7: 
            case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return isLeapYear(year) ? 29 : 28;
            default:
                return 0;
        }
    }
    
    
    @Override
    public String toString(){
        return day + "-" + MonthNames.substring((month-1)*3, month*3) + "-" + year;
    }

    public static Day validateDate(String dateStr) throws ExInvalidDate, ExInvalidNewDay {
        String[] sDayParts = dateStr.split("-");
        SystemDate sd = SystemDate.getInstance();
        
        if(sDayParts.length < 3) {
            throw new ExInvalidDate();
        }

        try {
            int day = Integer.parseInt(sDayParts[0]);
            int monthIndex = MonthNames.indexOf(sDayParts[1]);
            int month = monthIndex / 3 + 1;
            int year = Integer.parseInt(sDayParts[2]);

            Day newDay = new Day(year, month, day);

            if (newDay.getYear() < sd.getYear() || 
                (newDay.getYear() == sd.getYear() && newDay.getMonth() < sd.getMonth()) || 
                (newDay.getYear() == sd.getYear() && newDay.getMonth() == sd.getMonth() && newDay.getDay() < sd.getDay())) {
                throw new ExInvalidNewDay(sd.toString());
            }

            if (monthIndex == -1 || monthIndex % 3 != 0) {
                throw new ExInvalidDate();
            }

            if (!valid(year, month, day)) {
                throw new ExInvalidDate();
            }

            Day currentDate = SystemDate.getInstance();
            Day newDate = new Day(year, month, day);
            
            if (newDate.compareTo(currentDate) < 0) {
                throw new ExInvalidDate();
            }
            
            return newDate;
        } catch (NumberFormatException e) {
            throw new ExInvalidDate();
        } catch (ExInvalidNewDay e) {
            throw new ExInvalidNewDay(sd.toString());
        }
    }

    @Override
    public int compareTo(Day other) {

        if (other == null) {
            return 1; 
        }

        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }

        if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }

        return Integer.compare(this.day, other.day);
    }
}