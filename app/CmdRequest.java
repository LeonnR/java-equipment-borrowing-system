public class CmdRequest extends RecordedCommand{
    Member myMember = null;
    Equipment myEquipment = null;
    EquipmentSet targetSet = null;
    Day requestDate = null;
    Day endDate = null;
    int duration = 0;
    Request newRequest = null;
    Club c = Club.getInstance();
    String memberId = null;
    String itemCode = null;
 
    @Override
    public void execute(String[] cmdParts) {
        try{
            if(cmdParts.length < 5){
                throw new ExInsufficientArguments();
            }else{
                try{
                    memberId = cmdParts[1];
                    itemCode = cmdParts[2];
                    requestDate = Day.validateDate(cmdParts[3]);
                    duration = Integer.parseInt(cmdParts[4]);
                    endDate = requestDate.clone().addDays(duration);
                    
                    if(duration < 1){
                        throw new ExDaysLessThanOne();
                    }
                }catch(NumberFormatException e){
                    throw new ExDaysIsNotInteger();
                }

                boolean memberFound = false;
                boolean equipmentRecordFound = false;

                for(Member m : c.getAllMembers()){
                    if(memberId.equals(m.getID())){
                        myMember = m;
                        memberFound = true;
                        break;
                    }
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

                for(EquipmentSet es : myMember.getBorrowedSets()) {
                    if(es.getEquipment() == myEquipment && 
                       ((es.getBorrowStartDate().compareTo(endDate) <= 0 && es.getBorrowEndDate().compareTo(requestDate) >= 0) || (es.getBorrowStartDate().compareTo(requestDate) <= 0 && es.getBorrowEndDate().compareTo(requestDate) >= 0) || (es.getBorrowStartDate().compareTo(requestDate) >= 0 && es.getBorrowEndDate().compareTo(endDate) <= 0) || (es.getBorrowStartDate().compareTo(requestDate) <= 0 && es.getBorrowEndDate().compareTo(endDate) >= 0))) {
                        throw new ExOverlappingPeriod();
                    }
                }

                for(Request r : myMember.getRequestList()) {
                    if(r.getEquipment() == myEquipment) {
                        if(!(endDate.compareTo(r.getStartDate()) < 0 || requestDate.compareTo(r.getEndDate()) > 0)) {
                            throw new ExOverlappingPeriod();
                        }
                    }
                }

                if(!equipmentRecordFound){
                    System.out.println();
                    throw new ExEquipmentRecordNotFound();
                }

                for(EquipmentSet es : myEquipment.getSetList()) {
                    endDate = requestDate.clone().addDays(duration);
                    if (es.isAvailableOnDate(requestDate, endDate)) {
                        targetSet = es;
                        break;
                    }
                }

                if(targetSet == null) {
                    throw new ExNoAvailableEquipmentSet();
                }

                newRequest = new Request(myMember, targetSet, requestDate, duration);
                targetSet.addRequest(newRequest);
                myMember.addRequest(newRequest);

                System.out.println(myMember.getID() + " " + myMember.getName() + 
                    " requests " + targetSet.getCode() + " (" + myEquipment.getName() + 
                    ") for " + requestDate + " to " + requestDate.clone().addDays(duration));
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
        }catch(ExDaysLessThanOne e){
            System.out.println(e.getMessage());
        }catch(ExDaysIsNotInteger e){
            System.out.println(e.getMessage());
        }catch(ExInvalidDate e){
            System.out.println(e.getMessage());
        }catch(ExOverlappingPeriod e){
            System.out.println(e.getMessage());
        }catch(ExInvalidNewDay e){
            System.out.println(e.getMessage());
        }
       
    }

    @Override
    public void undoMe() {
        myMember.removeRequest(newRequest);
        targetSet.removeRequest(newRequest);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        targetSet.addRequest(newRequest);
        myMember.addRequest(newRequest);
        addUndoCommand(this);
    }
}
