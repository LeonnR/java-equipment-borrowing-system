import java.util.*;

public class Equipment implements Comparable<Equipment> {
    private String code;
    private String name;
    private int numberOfSets;
    private ArrayList<EquipmentSet> setList = new ArrayList<>();
    private HashMap<String, String> borrowedSets = new HashMap<>();

    public Equipment(String code, String name) {
        this.code = code;
        this.name = name;
        Club.getInstance().addEquipment(this);
    }

    public static void list(ArrayList<Equipment> allEquipments) {
        System.out.printf("%-5s%-15s%5s\n", "Code", "Name", "#sets");
        for (Equipment equ : allEquipments) {
            System.out.printf("%-5s%-15s%5d %s\n", equ.code, equ.name,
            equ.numberOfSets, equ.getBorrowedSetsInfo());
        }
    }

    public static void listStatus(ArrayList<Equipment> allEquipments) {
        String memberName = "";
        for (Equipment e : allEquipments) {
            System.out.println("[" + e.code + " " + e.name + "]");
            if (e.setList.isEmpty()) {
                System.out.println("  We do not have any sets for this equipment.");
            } else {
                for (EquipmentSet es : e.setList) {
                    System.out.print("  " + es.getCode() + "\n    Current status: ");
                    if (es.getStatus() && es.getCurrentBorrower() != null) {
                        Club c = Club.getInstance();
                        for(Member m : c.getAllMembers()){
                            if(m.getID().equals(e.borrowedSets.get(es.getCode()))){
                                memberName = m.getName();
                            }
                        }
                        System.out.println(e.borrowedSets.get(es.getCode()) + " " + 
                            memberName + " borrows for " + 
                            es.getBorrowStartDate() + " to " + 
                            es.getBorrowEndDate());
                    } else {
                        System.out.println("Available");
                    }
                    
                    ArrayList<Request> requests = es.getRequests();
                    if (!requests.isEmpty()) {
                        System.out.print("    Requested period(s): ");

                        ArrayList<Request> sortedRequests = new ArrayList<>(requests);
                        Collections.sort(sortedRequests, (r1, r2) -> 
                            r1.getStartDate().compareTo(r2.getStartDate())
                        );
                        
                        for (int i = 0; i < sortedRequests.size(); i++) {
                            System.out.print(sortedRequests.get(i).getStartDate() + " to " + 
                                sortedRequests.get(i).getEndDate());
                            if (i < sortedRequests.size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println();
                    }
                }
            }
            System.out.println();
        }
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public int getSets() {
        return this.numberOfSets;
    }

    public void addSet() {
        numberOfSets += 1;
        String strNumberOfSets = Integer.toString(numberOfSets);
        EquipmentSet newSet = new EquipmentSet(this.code, strNumberOfSets, this.name);
        setList.add(newSet);
        Collections.sort(setList);
    }

    public void removeSet(String code, String numOfSets) {
        Iterator<EquipmentSet> iterator = setList.iterator();
        while (iterator.hasNext()) {
            EquipmentSet es = iterator.next();
            if (es.getCode().equals(code + "_" + numOfSets)) {
                iterator.remove();
                this.numberOfSets -= 1;
                break;
            }
        }
    }

    public void addToBorrowedSets(String equipmentSetCode, String memberId) {
        borrowedSets.put(equipmentSetCode, memberId);
        sortBorrowedSets();
    }

    public void removeFromBorrowedSets(String equipmentSetCode, String memberId) {
        borrowedSets.remove(equipmentSetCode, memberId);
    }

    public String getBorrowedSetsInfo() {
        if (borrowedSets.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("(Borrowed set(s): ");
        for (Map.Entry<String, String> entry : borrowedSets.entrySet()) {
            sb.append(entry.getKey()).append("(").append(entry.getValue()).append("), ");
        }
        sb.setLength(sb.length() - 2); 
        sb.append(")");
        return sb.toString();
    }

    public ArrayList<EquipmentSet> getSetList() {
        return setList;
    }

    private void sortBorrowedSets() {
        borrowedSets = borrowedSets.entrySet()
            .stream()
            .sorted((e1, e2) -> {
                String[] parts1 = e1.getKey().split("_");
                String[] parts2 = e2.getKey().split("_");
                
                int codeCompare = parts1[0].compareTo(parts2[0]);
                if (codeCompare != 0) return codeCompare;

                int num1 = Integer.parseInt(parts1[1]);
                int num2 = Integer.parseInt(parts2[1]);
                return Integer.compare(num1, num2);
            })
            .collect(LinkedHashMap::new,
                     (m, e) -> m.put(e.getKey(), e.getValue()),
                     Map::putAll);
    }

    @Override
    public int compareTo(Equipment another) {
        int myCode = Integer.parseInt(this.code.substring(1));
        int otherCode = Integer.parseInt(another.code.substring(1));

        if (myCode == otherCode) return 0;
        else if (myCode > otherCode) return 1;
        else return -1;
    }
}