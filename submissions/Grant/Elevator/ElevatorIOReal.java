package Elevator;
public class ElevatorIOReal implements ElevatorIO {
    private int floor;

    public ElevatorIOReal(int startingFloor) {
        floor = startingFloor;
    }

    @Override
    public void GoToElevatorFloor(int Floor) {
        floor = Floor;
    }

    @Override
    public int getCurrentFloor() {
        return floor;
    }

    @Override
    public void moveDown() {
        floor--;
    }

    @Override
    public void moveUp() {
        floor++;
    }
}
