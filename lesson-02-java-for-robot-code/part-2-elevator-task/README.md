# Part 2: The elevator task

[Back to lesson 02](../) · Before this: [Part 1](../part-1-java-for-robot-code/)

Adapted from FRC 971's public [java-evaluation](https://github.com/frc971/training-2026/tree/main/tasks/java-evaluation) task.

Time box: 1-2 hours.

---

# Virtual Elevator Control System

**For the purposes of this task, you may not use AI tools.**

## Goal

Build a five-floor elevator in Java, using the same style every mechanism on Grapefruit uses: an interface, a class that implements it, and a class that decides what should happen.

The elevator can:

- move up or down between floors, one floor at a time
- be sent to a floor
- report what it is doing as it moves

## What your program should do

- Start with the elevator at floor 1.
- Accept a target floor from 1 to 5.
- Print each floor it passes, one at a time.
- Refuse floors outside 1 to 5, and not move.

## How to structure your code

Three files:

| Your file | The robot code file it matches |
| --- | --- |
| `ElevatorIO.java` | `IndexerIO.java` |
| `ElevatorIOReal.java` | `IndexerIOReal.java` |
| `ElevatorSubsystem.java` | `IndexerSubsystem.java` |

### `ElevatorIO`

An interface. It declares three methods and nothing else. 

| Method | Takes | Returns | What it means |
| --- | --- | --- | --- |
| `moveUp` | nothing | nothing | go up exactly one floor |
| `moveDown` | nothing | nothing | go down exactly one floor |
| `getCurrentFloor` | nothing | `int` | which floor it is on now |

### `ElevatorIOReal`

A real elevator. It implements `ElevatorIO`.

- Keeps one piece of state: the current floor. Make it `private`.
- Its constructor takes one thing, the floor to start on.
- `moveUp` and `moveDown` change the floor by exactly one.
- **Prints nothing.** There should not be a single `System.out.println` in this file.

### `ElevatorSubsystem`

Decides how to reach a floor, and does all the printing.

- It stores an `ElevatorIO`. **Not** an `ElevatorIOReal`. 
- Its constructor takes three things in this order: the `ElevatorIO`, the lowest floor, the highest floor. 
- The constructor also prints the `Elevator instantiated at floor #` line. 
- `goToFloor` takes an `int` and returns nothing. If the floor is outside the limits, print the refusal line and stop without moving. Otherwise move one floor at a time towards the target, printing a line for each floor, and print the arrival line at the end.
- `getCurrentFloor` returns an `int`, and hands back whatever the IO says.

### Why the field is an `ElevatorIO`

`ElevatorSubsystem` stores the interface, so it never knows which kind of elevator it got. Any class implementing `ElevatorIO` can be handed to it and it will work unchanged.

That is the only reason the interface exists, and `IndexerSubsystem` holds an `IndexerIO` for the same reason.

### Why the printing is all in one place

`ElevatorIOReal` knows where it is. `ElevatorSubsystem` decides what should happen and reports it. Putting a `println` in `ElevatorIOReal` would work, and the test would still pass, but it would be wrong for the same reason `IndexerIOReal` does not decide when to spin the rollers. A class that both holds state and announces things has two jobs.

## Required output

The test makes these calls, in this order:

```java
ElevatorIO io = new ElevatorIOReal(1);
ElevatorSubsystem elevator = new ElevatorSubsystem(io, 1, 5);

elevator.goToFloor(3);
elevator.goToFloor(2);
elevator.goToFloor(0);
elevator.goToFloor(6);
elevator.goToFloor(5);
elevator.goToFloor(1);
```

Your three files must produce exactly this, in this order:

```
Elevator instantiated at floor 1
Moving up... now at floor 2
Moving up... now at floor 3
Arrived at floor 3
Moving down... now at floor 2
Arrived at floor 2
Floor 0 is not a valid floor
Floor 6 is not a valid floor
Moving up... now at floor 3
Moving up... now at floor 4
Moving up... now at floor 5
Arrived at floor 5
Moving down... now at floor 4
Moving down... now at floor 3
Moving down... now at floor 2
Moving down... now at floor 1
Arrived at floor 1
```


## Setting up

Work in a plain folder of your own, not inside a repo. This is ordinary Java, not robot code, and putting it inside a cloned repo makes VS Code try to build it as part of that project.

1. Make a new folder, somewhere like `C:\Users\<you>\frc\elevator`. Not inside OneDrive, same as lesson 01.
2. Copy `EvaluationTest.java` from this folder into it.
3. In WPILib VS Code: **File**, then **Open Folder**, and pick your new folder.
4. Make three new files in it: `ElevatorIO.java`, `ElevatorIOReal.java` and `ElevatorSubsystem.java`. Right-click in the file list, then **New File**.

You should end up with four files and nothing else:

```
elevator/
  ElevatorIO.java
  ElevatorIOReal.java
  ElevatorSubsystem.java
  EvaluationTest.java
```

## Testing Instructions

`EvaluationTest.java` is already in this folder, so there is nothing to download.

Run it from WPILib VS Code:

1. Open `EvaluationTest.java`.
2. Click **Run** above `public static void main`, or press `Ctrl+F5`.
3. Output appears in the terminal panel at the bottom.

Green means every test passed. Red names the first thing that did not match, and stops there, so fix them one at a time and run again.

If the **Run** link does not appear above `main`, Java support has not finished loading. Wait, then reload the window with `Ctrl+Shift+P` and **Developer: Reload Window**.

**Important:** You are not allowed to modify the `EvaluationTest.java` file. All tests must pass with the provided test file.

## Submitting

Copy your three `.java` files into `submissions/<your-name>/lesson-02-elevator/` in the training repo, then add this to `submissions/<your-name>/lesson-02.md` under a heading `## Part 2`:

1. A screenshot of the test passing, or the `All tests passed.` output pasted in.
2. Roughly how long it took.
3. Where you got stuck, and what got you unstuck.

### Optional: Extension

Grapefruit has a fourth file, `IndexerIOSim`, which fakes the motors so the code runs on a laptop. Yours has no second implementation yet.

Write `ElevatorIOBroken.java`, implementing `ElevatorIO`, where `moveUp()` adds two floors instead of one. Change one line in the test file to build it instead of `ElevatorIOReal`, and run. The test will fail. This is how simulation be useful for FRC, we can see what could fail before ever testing it on a robot.
