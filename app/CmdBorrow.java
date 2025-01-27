public class CmdBorrow extends RecordedCommand{
    Member myMember = null;
    Equipment myEquipment = null;
    EquipmentSet borrowedEquipment = null;
    private Day startDate;
    private Day endDate;
    private int duration;
    Club c = Club.getInstance();
    String memberId = null;
    String itemCode = null;

    @Override
    public void execute(String[] cmdParts) {
        try{
            if(cmdParts.length < 3){
                throw new ExInsufficientArguments();
            }else{
                try{
                    memberId = cmdParts[1];
                    itemCode = cmdParts[2];
    
                    if(cmdParts.length == 4){
                        duration = Integer.parseInt(cmdParts[3]);
                        if(duration < 1){
                            throw new ExDaysLessThanOne();
                        }
                    }
                }catch(NumberFormatException e){
                    throw new ExDaysIsNotInteger();
                }

                boolean memberFound = false;
                boolean equipmentRecordFound = false;
                boolean foundAvailableSet = false;

                for(Member m : c.getAllMembers()){
                    if(memberId.equals(m.getID())){
                        myMember = m;
                        memberFound = true;
                        break;
                    }
                }

                if(cmdParts.length == 4){
                    startDate = SystemDate.getInstance().clone();
                    endDate = SystemDate.getInstance().clone().addDays(duration);
                }else{
                    startDate = SystemDate.getInstance().clone();
                    endDate = SystemDate.getInstance().clone().addDays(7);
                }

                if(!memberFound){
                    throw new ExMemberNotFound();
                }

                for(Equipment e : c.getAllEquipments()){
                    if(itemCode.equals(e.getCode()) && e.getSets() != 0){
                        myEquipment = e;
                        equipmentRecordFound = true;
                        break;
                    }else if(itemCode.equals(e.getCode()) && e.getSets() == 0){
                        throw new ExNoAvailableEquipmentSet();
                    }
                }

                if(!equipmentRecordFound){
                    System.out.println();
                    throw new ExEquipmentRecordNotFound();
                }

                for(EquipmentSet es : myEquipment.getSetList()){
                    if(!es.getStatus() && es.isAvailableOnDate(startDate, endDate)){
                        foundAvailableSet = true;
                        break;
                    }
                }
                

                if(!foundAvailableSet){
                    throw new ExNoAvailableEquipmentSet();
                }

                for(Request r : myMember.getRequestList()) {
                    if(r.getEquipment().equals(myEquipment) && 
                       r.getStartDate().compareTo(endDate) <= 0 &&
                       r.getEndDate().compareTo(startDate) >= 0) {
                        throw new ExOverlappingRequestPeriod();
                    }
                }
               
                for(EquipmentSet es : myEquipment.getSetList()){
                    if(!es.getStatus() && es.isAvailableOnDate(startDate, endDate)){
                        for(EquipmentSet borrowed : myMember.getBorrowedSets()){
                            if(es.getEquipmentCode().equals(borrowed.getEquipmentCode())){
                                throw new ExMemberIsBorrowingSameEquipment();
                            }
                        }
                        borrowedEquipment = es;
                        borrowedEquipment.setBorrowing(myMember, startDate, endDate);
                        myMember.addBorrowedItems(borrowedEquipment);
                        break;
                    }
                }

                myEquipment.addToBorrowedSets(borrowedEquipment.getCode(), myMember.getID());

                System.out.println(myMember.getID() + " " + myMember.getName() + " borrows " + borrowedEquipment.getCode() + " (" + myEquipment.getName() + ") for " + startDate + " to " + endDate);
                addUndoCommand(this);
                clearRedoList();
                System.out.println("Done.");
            }

        }catch(ExInsufficientArguments e){
            System.out.println(e.getMessage());
        }catch(ExMemberNotFound e){
            System.out.println(e.getMessage());
        }catch(ExEquipmentRecordNotFound e){
            System.out.println(e.getMessage());
        }catch(ExNoAvailableEquipmentSet e){
            System.out.println(e.getMessage());
        }catch(ExMemberIsBorrowingSameEquipment e){
            System.out.println(e.getMessage());
        }catch(ExDaysLessThanOne e){
            System.out.println(e.getMessage());
        }catch(ExDaysIsNotInteger e){
            System.out.println(e.getMessage());
        }catch(ExOverlappingRequestPeriod e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe(){
        myMember.removeBorrowedItem(borrowedEquipment);
        myEquipment.removeFromBorrowedSets(borrowedEquipment.getCode(), myMember.getID());
        borrowedEquipment.setBorrowing(null, null, null);
        addRedoCommand(this);
    }

    @Override
    public void redoMe(){
        myMember.addBorrowedItems(borrowedEquipment);
        myEquipment.addToBorrowedSets(borrowedEquipment.getCode(), myMember.getID());
        borrowedEquipment.setBorrowing(myMember, startDate, endDate);
        addUndoCommand(this);
    }
}
