public class Request {
    private Member requester;
    private EquipmentSet requestedSet;
    private Day startDate;
    private Day endDate;
    
    public Request(Member requester, EquipmentSet requestedSet, Day startDate, int duration) {
        this.requester = requester;
        this.requestedSet = requestedSet;
        this.startDate = startDate;
        this.endDate = startDate.clone().addDays(duration);
    }

    public Member getRequester() {
        return requester;   
    }

    public EquipmentSet getRequestedSet() {
        return requestedSet;
    }

    public Day getStartDate() {
        return startDate;
    }

    public Day getEndDate() {
        return endDate;
    }   

    public boolean overlapsWith(Day startDate, Day endDate) {
        return this.startDate.compareTo(endDate) <= 0 && this.endDate.compareTo(startDate) >= 0;
    }

    public Equipment getEquipment(){
        return this.requestedSet.getEquipment();
    }

     
}
