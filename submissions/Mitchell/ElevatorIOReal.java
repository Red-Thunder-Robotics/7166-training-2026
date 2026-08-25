package submissions.Mitchell;

public class ElevatorIOReal implements ElevatorIO {
    private int Floor;

    public ElevatorIOReal(int startingFloor){
        Floor = startingFloor;
    }
    
    @Override
    public int getCurrentFloor(){
        return Floor;
    }

    @Override
    public void moveDown(){
        Floor--;
    }

    @Override
    public void moveUp(){
        Floor++;
    }
}
