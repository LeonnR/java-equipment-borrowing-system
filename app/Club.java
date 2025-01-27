import java.util.*;

public class Club {
    private ArrayList<Member> allMembers;
    private ArrayList<Equipment> allEquipments;
    private static Club instance = new Club();
    private Club(){
        allMembers = new ArrayList<>();
        allEquipments = new ArrayList<>();
    }

    public static Club getInstance(){return instance;}


    public void addMember(Member mem){
        allMembers.add(mem);
        Collections.sort(allMembers);
    }

    public void removeMember(Member m){
        allMembers.remove(m);
    }

    public void listClubMembers(){
        Member.list(this.allMembers);
    }

    public void listClubMemberStatus(){
        Member.listStatus(this.allMembers);
    }

    ArrayList<Member> getAllMembers(){
        return allMembers;
    }

    public void addEquipment(Equipment equ){
        allEquipments.add(equ);
        Collections.sort(allEquipments);
    }

    public void removeEquipment(Equipment e){
        allEquipments.remove(e);
    }

    public Equipment getEquipment(String code){
        for(Equipment e : allEquipments){
            if(e.getCode().equals(code)) return e;
        }
        return null;
    }

    public void listClubEquipments(){
        Equipment.list(this.allEquipments);
    }

    public void listClubEquipmentStatus(){
        Equipment.listStatus(this.allEquipments);
    }

    ArrayList<Equipment> getAllEquipments(){
        return allEquipments;
    }
}
