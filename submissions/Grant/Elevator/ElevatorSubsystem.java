package Elevator;

public class ElevatorSubsystem {
    public static ElevatorSubsystem instance = null;

    private final ElevatorIO io;
    private int min;
    private int max;

    public ElevatorSubsystem(ElevatorIO Io, int min, int max) {
        instance = this;

        io = Io;

        this.min = min;

        this.max = max;

        System.out.println("Elevator instantiated at floor " + io.getCurrentFloor());
    }

    public void goToFloor(int GoalFloor) {
        if (GoalFloor < min || GoalFloor > max) {
            System.out.println("Floor " + GoalFloor + " is not a valid floor\n");
            return;
        }
        while (io.getCurrentFloor() < GoalFloor) {
            io.moveUp();
            System.out.println("Moving up... now at floor " + io.getCurrentFloor());
        }
        while (io.getCurrentFloor() > GoalFloor) {
            io.moveDown();
            System.out.println("Moving down... now at floor " + io.getCurrentFloor());
        }
        System.out.println("Arrived at floor " + io.getCurrentFloor());
    }
    public int getCurrentFloor() {return io.getCurrentFloor();}
}
