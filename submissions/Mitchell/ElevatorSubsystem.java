package submissions.Mitchell;

public class ElevatorSubsystem{
    public static ElevatorSubsystem instance = null;
    private final ElevatorIO m_io;
    private final int minFloor;
    private final int maxFloor;
    
    public ElevatorSubsystem(ElevatorIO io, int minFloor, int maxFloor){
        instance = this;
        m_io = io;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;

        System.out.println("Elevator instantiated at floor " + io.getCurrentFloor());
    }

    public void goToFloor(int goalFloor){
        if(goalFloor < minFloor || goalFloor > maxFloor){
            System.out.print("Floor " + goalFloor + " is not a valid floor\n");
            return;
        }

        while(m_io.getCurrentFloor() < goalFloor) {
            m_io.moveUp();
            System.out.println("Moving up... now at floor " + m_io.getCurrentFloor());
        }

        while(m_io.getCurrentFloor() > goalFloor) {
            m_io.moveDown();
            System.out.println("Moving down... now at floor " + m_io.getCurrentFloor());
        }

        System.out.println("Arrived at floor " + getCurrentFloor());
    }

    public int getCurrentFloor(){
        return m_io.getCurrentFloor();
    }
} 