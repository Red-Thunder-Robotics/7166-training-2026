# Part 1: Java for robot code

[Back to lesson 02](../) · Next: [Part 2, the elevator task](../part-2-elevator-task/)
[Video](https://youtu.be/pAHYPxFpKXI) for this part.

Read this with Grapefruit open (`7166_REBUILT` repo).

This starts from the beginning. If you have written Java before, section 0 and the first part of each section will be revision, and you can move faster.

Time box: about an hour. There is no code to write in this part.

---

## 0. What a class is

You can skip this if you are comfortable with classes already.

A **class** is a description of a kind of thing. It says what that thing knows and what it can do. It is a blueprint, not a thing.

An **object** is one actual thing built from that blueprint. Also called an **instance**.

```java
public class Elevator {
    private int currentFloor;          // a field: what it knows

    public void moveUp() {             // a method: what it can do
        currentFloor++;
    }
}
```

`Elevator` is the class. `new Elevator(1)` makes an object. You can make several, and each keeps its own `currentFloor`.

- **Fields** are the variables inside a class. They are what an object remembers between calls.
- **Methods** are the functions inside a class. They are what you can ask an object to do.
- `private` means only code inside this class can touch it. `public` means anyone can.

## 1. One class, one job

> **Each class should do one job, and you should be able to describe that job in one sentence.**


### Why this rule exists

Say you put the whole indexer in one file: the CAN IDs, the motor code, the version for the simulator, and the logic that decides when to spin. Now:

- You change a gear ratio, and you have to read the whole file to know what else you touched.
- You cannot run any of it on a laptop, because the motor code needs a robot.
- Two people cannot work on it at once without colliding.
- When it errors, you have no way to narrow down which part is wrong.

### What it looks like in the 2026 Robot Code

Open the indexer folder: `src/main/java/frc/robot/subsystems/indexer/`. Five files:

| File | Its one job |
| --- | --- |
| `IndexerConstants.java` | Numbers. CAN IDs, gear ratios, current limits, gains |
| `IndexerIO.java` | The list of things an indexer can do and report |
| `IndexerIOReal.java` | Doing those things to real motors |
| `IndexerIOSim.java` | Faking those things on a laptop |
| `IndexerSubsystem.java` | Deciding what the indexer should do |

Look at how narrow each file is. `IndexerConstants.java` is 51 lines of numbers. `IndexerIO.java` is 35 lines and does nothing at all.

### This is not a 7166 invention

>[!IMPORTANT]
>“Steal from the best, and invent the rest” -Michael Corsetto, Team 1678

The split came from **FRC 6328, Mechanical Advantage**, who built [AdvantageKit](https://github.com/Mechanical-Advantage/AdvantageKit), the logging library we use. 

Here are two other teams' **2026 competition robots**, doing the same job we are: a set of rollers that moves game pieces. Every file is a link. Go and read them if you are interested.

| | Says what it can do | Does it for real | Fakes it | Decides |
| --- | --- | --- | --- | --- |
| [**6328**](https://github.com/Mechanical-Advantage/RobotCode2026Public) rollers | [`RollerSystemIO`](https://github.com/Mechanical-Advantage/RobotCode2026Public/blob/main/src/main/java/org/littletonrobotics/frc2026/subsystems/rollers/RollerSystemIO.java) 55 | [`RollerSystemIOTalonFX`](https://github.com/Mechanical-Advantage/RobotCode2026Public/blob/main/src/main/cpp/subsystems/RollerSystemIOTalonFX.cpp) in C++ | [`RollerSystemIOSim`](https://github.com/Mechanical-Advantage/RobotCode2026Public/blob/main/src/main/java/org/littletonrobotics/frc2026/subsystems/rollers/RollerSystemIOSim.java) 58 | [`RollerSystem`](https://github.com/Mechanical-Advantage/RobotCode2026Public/blob/main/src/main/java/org/littletonrobotics/frc2026/subsystems/rollers/RollerSystem.java) 131 |
| [**1678**](https://github.com/frc1678/C2026-Public) feeder rollers | [`MotorIO`](https://github.com/frc1678/C2026-Public/blob/main/src/main/java/frc/lib/io/MotorIO.java) 778 | [`MotorIOTalonFX`](https://github.com/frc1678/C2026-Public/blob/main/src/main/java/frc/lib/io/MotorIOTalonFX.java) 302 | [`MotorIOTalonFXSim`](https://github.com/frc1678/C2026-Public/blob/main/src/main/java/frc/lib/io/MotorIOTalonFXSim.java) 76 | [`MotorSubsystem`](https://github.com/frc1678/C2026-Public/blob/main/src/main/java/frc/lib/bases/MotorSubsystem.java) 307 + [`FeederRollers`](https://github.com/frc1678/C2026-Public/blob/main/src/main/java/frc/robot/subsystems/feederrollers/FeederRollers.java) 20 |
| [**7166**](https://github.com/Red-Thunder-Robotics/7166_REBUILT) indexer | [`IndexerIO`](https://github.com/Red-Thunder-Robotics/7166_REBUILT/blob/main/src/main/java/frc/robot/subsystems/indexer/IndexerIO.java) 35 | [`IndexerIOReal`](https://github.com/Red-Thunder-Robotics/7166_REBUILT/blob/main/src/main/java/frc/robot/subsystems/indexer/IndexerIOReal.java) 148 | [`IndexerIOSim`](https://github.com/Red-Thunder-Robotics/7166_REBUILT/blob/main/src/main/java/frc/robot/subsystems/indexer/IndexerIOSim.java) 71 | [`IndexerSubsystem`](https://github.com/Red-Thunder-Robotics/7166_REBUILT/blob/main/src/main/java/frc/robot/subsystems/indexer/IndexerSubsystem.java) 104 |

Numbers are lines of code. 

<details>
<summary><b>How 6328's rollers work</b></summary>

Open [`RollerSystemIO.java`](https://github.com/Mechanical-Advantage/RobotCode2026Public/blob/main/src/main/java/org/littletonrobotics/frc2026/subsystems/rollers/RollerSystemIO.java). They use two methods:

```java
default void updateInputs(RollerSystemIOInputs inputs) {}
default void applyOutputs(RollerSystemIOOutputs outputs) {}
```

That is it. No `setVelocity`, no `stop`, no `runRollers`. 

There are two forms. `RollerSystemIOInputs` is everything the hardware tells us: connected, position, velocity, applied voltage, supply current, temperature. `RollerSystemIOOutputs` is everything we want it to do:

```java
public static class RollerSystemIOOutputs {
    public RollerSystemIOMode mode = RollerSystemIOMode.BRAKE;
    public double appliedVoltage = 0.0;
    public double velocity = 0.0;
    public double kP = 0.0;
    public double kD = 0.0;
    public double feedforward = 0.0;
    public boolean brakeModeEnabled = true;
}
```

`RollerSystemIOMode` is an enum with four values: `BRAKE`, `COAST`, `VOLTAGE_CONTROL`, `CLOSED_LOOP`. 

The deciding class, [`RollerSystem.java`](https://github.com/Mechanical-Advantage/RobotCode2026Public/blob/main/src/main/java/org/littletonrobotics/frc2026/subsystems/rollers/RollerSystem.java), never touches a motor. It fills in the form:

```java
public void runClosedLoop(double setpointVelocity) {
    outputs.mode = RollerSystemIOMode.CLOSED_LOOP;
    outputs.velocity = setpointVelocity;
    outputs.feedforward = Math.signum(setpointVelocity) * kS + setpointVelocity * kV;
}
```

`kS` is the voltage needed just to overcome friction and get moving, and `Math.signum` makes it push the way you are already going. `kV` is voltage per unit of speed. Add them and you have a first guess at the voltage before any error correction happens at all.

Their `periodic()` is worth reading too. It reads the inputs, logs them, raises an alert if the motor has stopped answering, and reports the current it drew to `FinanceDepartment` that tracks the robot's energy budget.

**The part that matters most for us** is [`RollerSystemIOSim.java`](https://github.com/Mechanical-Advantage/RobotCode2026Public/blob/main/src/main/java/org/littletonrobotics/frc2026/subsystems/rollers/RollerSystemIOSim.java):

```java
private final DCMotorSim sim;
sim = new DCMotorSim(LinearSystemId.createDCMotorSystem(motorModel, moi, reduction), motorModel);
```

Theirs is a real physics model. It knows which motor, how hard the rollers are to spin up (`moi`, moment of inertia), and the gear reduction. 

One last thing. Their real hardware implementation, [`RollerSystemIOTalonFX.cpp`](https://github.com/Mechanical-Advantage/RobotCode2026Public/blob/main/src/main/cpp/subsystems/RollerSystemIOTalonFX.cpp), is **C++**. `RollerSystem` never notices, because it only ever talks to the interface.

</details>

<details>
<summary><b>How 1678's feeder rollers work</b></summary>

Open [`FeederRollers.java`](https://github.com/frc1678/C2026-Public/blob/main/src/main/java/frc/robot/subsystems/feederrollers/FeederRollers.java). It is 20 lines and there is no motor code in it at all.

Their shared `MotorSubsystem` already knows how to run a motor. What it does not know is *which speeds this particular mechanism should ever run at*. So a mechanism is written as a short list of named speeds:

```java
public class FeederRollers extends MotorSubsystem<MotorIOTalonFX> {
    public static final Setpoint FEED_VOLTAGE = Setpoint.withVoltageSetpoint(FeederRollersConstants.kFeedVoltage);
    public static final Setpoint IDLE = Setpoint.withNeutralSetpoint();
    public static final Setpoint REVERSE = Setpoint.withVelocitySetpoint(FeederRollersConstants.kReverse);
    ...
}
```

A `Setpoint` is a small object meaning "run at this voltage" or "run at this speed" or "coast". Elsewhere the robot says `feederRollers.applySetpoint(FeederRollers.FEED_VOLTAGE)` and the shared class does the rest.

So their mechanism class only looks at: **what are the things this thing can be doing?** Feeding, feeding slowly, idle, reversing, reversing slowly. 

Everything else lives in two other places. The numbers are in [`FeederRollersConstants.java`](https://github.com/frc1678/C2026-Public/blob/main/src/main/java/frc/robot/subsystems/feederrollers/FeederRollersConstants.java): gear ratio, current limits, inversion, gains. The machinery is in [`MotorSubsystem.java`](https://github.com/frc1678/C2026-Public/blob/main/src/main/java/frc/lib/bases/MotorSubsystem.java): reading the motor, logging, and turning a `Setpoint` into something the motor understands.
</details>

6328 and 7166s code use an `interface` for the contract. 1678 use an `abstract class`, which can hold shared code as well as promises. And 1678's fake `extends` their real one, reusing the parts that work off-robot, where ours and 6328's are separate classes that each implement the contract from scratch.

**Remember this for Part 2.** You are going to build a three-class version of the same idea.

## 2. Interfaces: one job, two ways of doing it

### What an interface is

An **interface** is a list of methods with no bodies. It says what something must be able to do, but says nothing about how.

```java
public interface Light {
    void turnOn();
    void turnOff();
}
```

That is not a light. You cannot build one from it. It is the promise that anything calling itself a `Light` can be turned on and off.

A class then **implements** the interface, which means it provides the bodies:

```java
public class LedStrip implements Light {
    public void turnOn()  { /* set the LEDs */ }
    public void turnOff() { /* clear the LEDs */ }
}

public class FakeLight implements Light {
    public void turnOn()  { System.out.println("on"); }
    public void turnOff() { System.out.println("off"); }
}
```

Now any code written against `Light` works with either one.

### Why we want that

We want to run the robot code on a laptop.

The real motor code cannot run on a laptop. It needs a roboRIO/SystemCore and a CAN bus. If the logic that decides when to spin the indexer is in the same class as the code that talks to the motor, then testing the logic needs a robot. Which means you can only test it in the workshop, on the one robot, when nobody else is using it.

Put an interface in the middle and the problem disappears. Write the logic against the interface, then supply the real motors on the robot and a fake on your laptop.

### How the 2026 code does it

Open `IndexerIO.java`. The whole file:

```java
public interface IndexerIO {
    @AutoLog
    class IndexerIOInputs {
        double indexerTargetVelocityRPS;
        double indexerVelocityRPS;
        double indexerCurrentAmps;

        double topRollerTargetVelocityRPS;
        double topRollerVelocityRPS;
        double topRollerCurrentAmps;

        double lowerKickerTargetVelocityRPS;
        double lowerKickerVelocityRPS;
        double lowerKickerCurrentAmps;
    }

    default void updateInputs(IndexerIOInputs inputs) {}

    default void indexerVelocity(AngularVelocity velocity) {}

    default void indexerStop() {}

    default void topRollerVelocity(AngularVelocity velocity) {}

    default void topRollerStop() {}

    default void lowerKickerVelocity(AngularVelocity velocity) {}

    default void lowerKickerStop() {}
}
```

The file has two halves:

- `IndexerIOInputs` is **what we can measure**, coming up from the hardware.
- The six methods are **what we can command**, instructions going down.

Now open `Robot.java` and find lines 177, 197 and 214:

```java
m_indexerSubsystem = new IndexerSubsystem(new IndexerIOReal());   // 177
m_indexerSubsystem = new IndexerSubsystem(new IndexerIOSim());    // 197
m_indexerSubsystem = new IndexerSubsystem(new IndexerIO() {});    // 214
```

One small thing to notice. Line 214 says `new IndexerIO() {}`. That makes an object straight out of the interface. It works because the methods in `IndexerIO` end in `{}` instead of a semicolon. They already have empty bodies.

Three different indexers, chosen once when the robot starts:

| Line | When | What it does |
| --- | --- | --- |
| 177 | Real robot | Talks to three motors over CAN |
| 197 | Simulation | Fakes the motion on your laptop |
| 214 | Replaying a log | Nothing at all |

`IndexerSubsystem` is the same code in all three cases. 

**This is why you can run robot code on a laptop with no robot attached.** 

## 3. Enums that carry behaviour

### What an enum is

An **enum** is a type with a fixed list of allowed values.

```java
public enum Alliance {
    Red,
    Blue
}
```

An `Alliance` can be `Red` or `Blue` and nothing else. Compare that to using a `String`, where `"red"`, `"Red"`, `"RED"` and `"blu"` are all possible. The compiler cannot help you with strings. Use an enum whenever a value has a small, known set of options. Robot states, control modes, etc.

### Robot enums usually carry behaviour too

In most Java you have written, an enum is just a list of names. In robot code it usually has methods on it as well.

Open `src/main/java/frc/robot/state_machine/GeneralRobotState.java`. The whole file:

```java
public enum GeneralRobotState {
    Idle,
    HubTracking,
    HubTrackingFiring,
    AllianceFeed,
    AllianceFeedFiring;

    public boolean isFiring() {
        switch (this) {
            case Idle:
            case HubTracking:
            case AllianceFeed:
                return false;
            case HubTrackingFiring:
            case AllianceFeedFiring:
                return true;
        }

        return false;
    }
}
```

Five states, and the enum itself knows which ones count as firing.

Two questions before you move on. Try to answer them before you open the box.

**What does `this` mean inside `isFiring()`?**

<details>
<summary>Answer</summary>

`this` is the enum value the method was called on.

`isFiring()` is not a method on the `GeneralRobotState` type in general. It runs on one particular value. If somewhere in the robot the state is `HubTrackingFiring` and that code calls `state.isFiring()`, then inside the method `this` is `HubTrackingFiring`.

So `switch (this)` is the value asking itself which one it is.

</details>

**Every case is already covered. So why is there a `return false;` after the switch?**

<details>
<summary>Answer</summary>

Because Java does not check that a `switch` statement covers every value of the enum.

A method that returns `boolean` has to return something on every possible path through it. The compiler looks at the switch, sees a series of cases, and has no way to know they are exhaustive. As far as it is concerned the switch could match nothing and run off the end of the method, which would be a method that returns nothing. So it refuses to compile without a return at the bottom.

The line is there to satisfy the compiler. Today it can never run.

Tomorrow it can. Add a sixth value to the enum and `isFiring()` matches nothing, falls past the switch, and returns `false` for a state that might be firing. Nothing warns you. Section 4 is about the version of `switch` that does.

</details>


## 4. `final`, and why there is so much of it

Look at almost any field in `IndexerIOReal.java`:

```java
private final TalonFX m_indexerMotor = new TalonFX(indexerMotorId, Constants.CANBUS);
private final TalonFX m_topRollerMotor = new TalonFX(topRollerMotorId, Constants.CANBUS);
private final TalonFX m_lowerKickerMotor = new TalonFX(lowerKickerMotorId, Constants.CANBUS);
```

`final` means you cannot point that name at anything else later. The general rule for variables is to make it `final` unless you have a reason not to.

---

## What to write down

Put this in `submissions/<your-name>/lesson-02.md` under a heading `## Part 1`. Short answers are fine.

1. Pick any mechanism in `subsystems/` other than the indexer. List its files and say what each one's job is.
2. What does `this` refer to inside an enum's method?
3. `IndexerIO` has no logic in it. What is it for? What would break if we deleted it and put the TalonFX code straight into `IndexerSubsystem`?
4. You add a value to an enum. What do you need to change?
5. What could go wrong if `m_indexerMotor` were not `final`?
6. One thing in this part you are still unsure about.

---

Next: [Part 2, the elevator task](../part-2-elevator-task/)
