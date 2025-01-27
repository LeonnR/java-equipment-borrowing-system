import java.util.ArrayList;

public class EquipmentSet implements Comparable<EquipmentSet>{
    private String setCode;
    private String equipmentCode;
    private boolean borrowed;
    private String name;
    private ArrayList<Request> requests;
    private Member currentBorrower;
    private Day borrowStartDate;
    private Day borrowEndDate;
    private Equipment equipment;

    public EquipmentSet(String equipmentCode, String setNumber, String name){
        this.setCode = equipmentCode + "_" + setNumber;
        this.equipmentCode = equipmentCode;
        this.name = name;
        this.borrowed = false;
        requests = new ArrayList<>();
        equipment = Club.getInstance().getEquipment(equipmentCode);
    }

    public String getCode(){
        return this.setCode;
    }

    public boolean getStatus(){
        return borrowed;
    }

    public String getName(){
        return this.name;
    }

    public void toggleStatus(){
        if(borrowed){
            borrowed = false;
        }else{
            borrowed = true;
        }
    }

    public String getEquipmentCode(){
        return this.equipmentCode;
    }

    public Equipment getEquipment(){
        return this.equipment;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void removeRequest(Request request) {
        requests.remove(request);
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    @Override
    public int compareTo(EquipmentSet another){
        if (this.setCode.equals(another.setCode)) return 0;
        else if (this.setCode.compareTo(another.setCode)>0)return 1;
        else return -1;
    }

    public boolean isAvailableOnDate(Day requestDate, Day requestEndDate) {

        if (getStatus()) {
            if (requestDate.compareTo(borrowEndDate) <= 0) {
                return false;
            }
        }
    

        for (Request existingRequest : requests) {
            if (requestDate.compareTo(existingRequest.getEndDate()) <= 0 && 
                requestEndDate.compareTo(existingRequest.getStartDate()) >= 0) {
                return false;
            }
        }
        
        return true;
    }

    public Member getCurrentBorrower() {
        return currentBorrower;
    }

    public Day getBorrowStartDate() {
        return borrowStartDate;
    }

    public Day getBorrowEndDate() {
        return borrowEndDate;
    }

    public void setBorrowing(Member borrower, Day startDate, Day endDate) {
        this.currentBorrower = borrower;
        this.borrowStartDate = startDate;
        this.borrowEndDate = endDate;
        this.toggleStatus(); 
    }
}
