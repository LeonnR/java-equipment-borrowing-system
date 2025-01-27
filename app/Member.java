import java.util.ArrayList;
import java.util.Collections;

public class Member implements Comparable<Member> {
    private String id;
    private String name;
    private Day joinDate;
    private int borrowedItems;
    private ArrayList<EquipmentSet> setItem = new ArrayList<>();
    private ArrayList<Request> requestedItems = new ArrayList<>();

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.joinDate = SystemDate.getInstance().clone();
        this.borrowedItems = 0;
        Club.getInstance().addMember(this);
    }

    public static void list(ArrayList<Member> allMembers) {
        System.out.printf("%-5s%-9s%11s%11s%13s\n", "ID", "Name",
        "Join Date ", "#Borrowed", "#Requested");
        for (Member m : allMembers) {
            System.out.printf("%-5s%-9s%11s%7d%13d\n", m.id, m.name,
            m.joinDate, m.borrowedItems, m.getNumberOfRequests());
        }
    }

    public static void listStatus(ArrayList<Member> allMembers) {
        for (Member m : allMembers) {
            System.out.println("[" + m.id + " " + m.name + "]");
            if (m.setItem.isEmpty() && m.requestedItems.isEmpty()) {
                System.out.println("No record.");
            } else { 
                for (EquipmentSet es : m.setItem) {
                    System.out.println("- borrows " + es.getCode() + " (" + es.getName() + ") for " + es.getBorrowStartDate() + " to " + es.getBorrowEndDate());
                }
                for(Request request : m.requestedItems){
                    System.out.println("- requests " + request.getRequestedSet().getCode() + " (" + request.getRequestedSet().getName() + ") for " + request.getStartDate() + " to " + request.getEndDate());
                }
            }
            System.out.println();
        }
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void addBorrowedItems(EquipmentSet es) {
        this.borrowedItems += 1;
        setItem.add(es);
        sortSetItems();
    }

    private void sortSetItems() {
        Collections.sort(setItem, (es1, es2) -> {
            String[] parts1 = es1.getCode().split("_");
            String[] parts2 = es2.getCode().split("_");
            
            int codeCompare = parts1[0].compareTo(parts2[0]);
            if (codeCompare != 0) return codeCompare;
            
            int num1 = Integer.parseInt(parts1[1]);
            int num2 = Integer.parseInt(parts2[1]);
            return Integer.compare(num1, num2);
        });
    }

    public void removeBorrowedItem(EquipmentSet es) {
        this.borrowedItems -= 1;
        setItem.remove(es);
    }

    public ArrayList<EquipmentSet> getBorrowedSets() {
        return this.setItem;
    }

    public void addRequest(Request request) {
        requestedItems.add(request);
        sortRequests();
    }

    private void sortRequests() {
        Collections.sort(requestedItems, (r1, r2) -> {

            int dateCompare = r1.getStartDate().compareTo(r2.getStartDate());
            if (dateCompare != 0) return dateCompare;
            

            return r1.getRequestedSet().getCode().compareTo(r2.getRequestedSet().getCode());
        });
    }

    public void removeRequest(Request request){
        requestedItems.remove(request);
    }

    public ArrayList<Request> getRequestList(){
        return this.requestedItems;
    }

    public int getNumberOfRequests() {
        return requestedItems.size();
    }

    public boolean hasRequestOverlap(Equipment equipment, Day startDate, Day endDate) {
        for (Request request : requestedItems) {
            if (request.getRequestedSet().getEquipment().equals(equipment) && 
                request.getStartDate().compareTo(endDate) <= 0 && 
                request.getEndDate().compareTo(startDate) >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Member another) {
        if (this.id.equals(another.id)) return 0;
        else if (this.id.compareTo(another.id) > 0) return 1;
        else return -1;
    }
}