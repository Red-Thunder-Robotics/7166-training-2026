# Lesson 03: Subsystem

## Purpose

Write robot code for the first time. You fill in the two files that talk to the indexer's roller motor, build the project, and watch the roller turn in simulation.

## Prerequisites

- Lesson 02, merged.
- The tools from lesson 01: WPILib 2026 VS Code, Git for Windows, GitHub Desktop.

## Time box

2-3 hours.

## Learning goals

- [ ] Say what a subsystem is and why there is one per mechanism.
- [ ] Say what `periodic()` is and how often it runs.
- [ ] Read a `TalonFXConfiguration` and say what each of its settings does.
- [ ] Say what a status signal is and why they are refreshed together.
- [ ] Say which computer runs `Slot0.kP`, and how many times a second.
- [ ] Fill in the indexer roller in `IndexerIOReal` and `IndexerIOSim`, build them, and see the roller turn in simulation.

**You may use AI or any other tools in this lesson.** You still have to be able to explain every line you hand in.

---

## 1. From here on, two repos

| Repo | What is in it | What you do in it |
| --- | --- | --- |
| `7166-training-2026` | The lessons, including this page, and everyone's write-ups | Read the lesson. Write `submissions/<your-name>/lesson-03.md` |
| `7166-training-robot` | Robot code with pieces taken out | Fill the pieces back in |

`7166-training-robot` is a copy of Grapefruit at commit `b418b00`, with the indexer's roller taken out of two files. Everything else is the real 2026 competition code, unchanged, including its full history.

**Video:** [Lesson 03, part 1: setup](https://youtu.be/HZ2ITfLBNZ8)

### Clone it

Same steps as lesson 01. In GitHub Desktop: **File**, then **Clone repository**, then the **URL** tab.

```
https://github.com/Red-Thunder-Robotics/7166-training-robot
```

Put it next to your other two clones, in `C:\Users\<you>\frc`. Check the path has no OneDrive in it.

<details>
<summary>Command line</summary>

```bash
git clone https://github.com/Red-Thunder-Robotics/7166-training-robot.git
```
</details>

### Open it and build it once

1. WPILib VS Code, **File**, then **Open Folder**, and pick `7166-training-robot`.
2. Wait. The first open downloads Gradle dependencies. The status bar at the bottom stops moving when it is done.
3. `Ctrl+Shift+P`, then `WPILib: Build Robot Code`, then Enter. It should end with `BUILD SUCCESSFUL`.

Build before you read the code, even though you have not written a line yet.

`IndexerIO.java` has `@AutoLog` on its `IndexerIOInputs` class. During the build, AdvantageKit reads that annotation and writes a new file called `IndexerIOInputsAutoLogged`, which is not in the repo. `IndexerSubsystem.java` line 16 uses that class. Until a build has run once, the editor underlines it in red and says `IndexerIOInputsAutoLogged cannot be resolved to a type`. The code is fine.

---

## 2. What a subsystem is

A subsystem is one class that owns one mechanism, and every motor and sensor on that mechanism. `IndexerSubsystem` owns the indexer. Nothing else in the robot code commands motor 14.

The training robot has eight of them, one folder each:

```
subsystems/climber  drive  ground_intake  indexer
           light_emitting_diodes  shooter  turret  vision
```

In WPILib, a subsystem is a class that `extends SubsystemBase`. Extending it buys two things.

**The scheduler calls `periodic()` on it every loop.** You never call `periodic()` yourself, and no other class does either.

**Only one command may use it at a time.** When a command is created with a subsystem listed as a requirement, the scheduler will not let a second command that requires the same subsystem run alongside it. The newer one takes the subsystem and the older one is cancelled.

One subsystem per mechanism, because two pieces of code driving the same motor cannot both get what they asked for. Ownership gives the scheduler a rule it can apply instead of letting the two of them alternate every loop.

### The indexer has three motors, and you are writing one of them

Open `IndexerIOReal.java`. It drives three motors:

| Motor | CAN id | What it does |
| --- | --- | --- |
| Indexer roller | 14 | The one you are writing |
| Top roller | 15 | Complete, in the same file |
| Lower kicker | 16 | Complete, in the same file |

---

## 3. `periodic()` runs every 20 milliseconds

The scheduler calls `periodic()` on every subsystem 50 times a second, from the moment the robot program starts until it stops. It runs while the robot is disabled. It runs when nobody has pressed anything.

Your lesson 02 elevator worked the other way round. `goToFloor(5)` looped, printed a floor, looped again, and returned once it had arrived. It could take as long as it liked, because nothing was waiting on it.

Robot code has no spare time. Everything the robot does happens inside **one 20 millisecond loop**: read the joysticks, run every subsystem's `periodic()`, run the commands, send the motor commands, write the log. If a `periodic()` sat in a loop waiting for the roller to reach speed, none of the rest would happen while it waited. The drive would not update, buttons would not be read, and the driver station would report a loop overrun.

So a subsystem does one loop's worth of work and returns.

### Read, log, then act

You are not writing `IndexerSubsystem` in this lesson, but you have to be able to read it. Open it and look at `periodic()`, line 37:

```java
m_io.updateInputs(m_inputs);            // 1. read the hardware

Logger.processInputs("Indexer", m_inputs);  // 2. write those readings to the log

switch (StateMachine.getShooterState()) {   // 3. decide what to do
```

That order is deliberate. AdvantageKit can replay a log: it runs this same code again later on a laptop, with the recorded readings fed back in instead of a motor, and you get the robot's exact behaviour back. That only works if the numbers in the log are the numbers the decision was made from. Log after the action and the replay can drift from what the robot did.

Step 3 is a `switch` on `StateMachine.getShooterState()`, which is one of `Idle`, `Shooting` or `Reversing`. A later lesson will focus on that switch and the state machine behind it. For now you only need one branch of it:

```java
case Reversing:
    m_io.indexerVelocity(indexerOutputVelocityReverse);
    m_io.topRollerVelocity(topRollerVelocityReverse);
    m_io.lowerKickerVelocity(lowerKickerVelocityReverse);
    break;
```

No conditions on it. When the robot is in the `Reversing` state, all three motors run backwards. `indexerVelocity` is one of the methods you are writing.

### The button that gets you there

`Controls.java` line 25:

```java
public static final Trigger reverseButton = operatorController.leftTrigger().or(driveController.b());
```

`driveController.b()` is the B button on an Xbox controller, which is button 2, which the simulator keyboard map puts on the **`L` key**. `Robot.java` binds it to `RobotCommands.generalReversing()`, which calls `setShooterState(ShooterState.Reversing)`.

So: press `L` in the simulator, the robot goes into `Reversing`, and `periodic()` calls the method you wrote.

---

## 4. The five files again

You read these in lesson 02, part 1.

| File | Its one job | You write |
| --- | --- | --- |
| `IndexerConstants.java` | Numbers. CAN ids, gear ratios, current limits, speeds, gains | Nothing |
| `IndexerIO.java` | The contract: what an indexer can be asked to do and what it reports | Nothing |
| `IndexerIOReal.java` | Doing those things to real motors over CAN | **8 TODOs** |
| `IndexerIOSim.java` | Faking them on a laptop | **4 TODOs** |
| `IndexerSubsystem.java` | Deciding what the motors should be doing | Nothing |

---

## 5. The motor controller, and its configuration

### The motor is not the only computer

The indexer roller is driven by a Kraken X60. Built into the back of that motor is a **Talon FX**, a motor controller with its own processor, its own reading of the motor's encoder, and its own control loop.

The roboRIO does not send voltage down a wire to the motor. It sends short messages over the **CAN bus**: one pair of wires that every motor controller on the robot shares, with devices taking turns. Each device has a number so messages can be addressed to it. The indexer's is `indexerMotorId = 14`.

```java
private final TalonFX m_indexerMotor = new TalonFX(indexerMotorId, Constants.CANBUS);
```

That object is Java's handle on device 14 of the bus named `7166CANivore`. This line is `TODO 1`.

### A configuration is a form you fill in once

`TalonFXConfiguration` is a plain object full of settings, each already holding a default. You change the few you care about, hand the whole object to the controller, and the controller keeps them until a different configuration arrives. It is stored on the motor controller, not in your program.

Here is what the indexer sets. Every value is already sitting in `IndexerConstants.java`.

| Setting | Value | What it does |
| --- | --- | --- |
| `MotorOutput.NeutralMode` | `Brake` | What the motor does when nothing is commanding it. `Brake` makes it resist being turned, so the roller stops promptly. `Coast` lets it free-spin down |
| `MotorOutput.Inverted` | `CounterClockwise_Positive` | Which direction counts as positive. Decided by which way the gearbox was bolted on. If a positive command spins the roller the wrong way, this is the setting to change, not the sign of every number in the code |
| `CurrentLimits.SupplyCurrentLimitEnable` | `true` | Turns the next line on. The limit is off by default |
| `CurrentLimits.SupplyCurrentLimit` | `25` | Amps the controller may draw from the battery before it backs off. A jammed roller would otherwise pull whatever the battery can give, brown out the robot and trip a breaker |
| `Feedback.SensorToMechanismRatio` | `24 / 12`, so 2 | The gearbox. See below |
| `Slot0.kP`, `Slot0.kV` | `1.75`, `12 / (5800 / 60)` | The gains |
| `MotionMagic.MotionMagicAcceleration` | `266` | How fast the target itself is allowed to change, in rotations per second, per second |

**`SensorToMechanismRatio`.** The encoder is inside the motor, so it counts motor turns. A 12-tooth gear drives a 24-tooth gear, so the motor turns twice for every one turn of the roller. Tell the controller that ratio and it divides by it before reporting anything and before acting on anything you send, so every number crossing the CAN bus is in roller turns.

**`MotionMagicAcceleration`** applies to the target the controller is chasing, rather than to the motor itself. 2600 RPM is 43.33 rotations per second. Without this line, asking for full speed asks for all 43.33 instantly, and the controller answers a huge error with every volt it has. Motion Magic walks the target up at 266 rotations per second per second instead, so it takes about 0.16 seconds to finish asking, and the motor is chasing something it can deliver the whole way.

### Sending it

```java
MotorAction.configureMotor("Indexer Roller", m_indexerMotor, indexerConfig).run();
```

`MotorAction` is in `frc/robot/util/PhoenixUtil.java`. Open it and read `tryRun()` and `run()`.

`run()` sends the configuration and waits up to a quarter of a second for the controller to acknowledge it. If nothing comes back it tries again, five attempts in total, and then throws.

---

## 6. Status signals

A **status signal** is one reading coming back from the motor controller: velocity, supply current, position, temperature. The controller puts them on the CAN bus at a fixed rate, and the Phoenix library keeps the newest copy of each one in memory.

```java
private final StatusSignal<AngularVelocity> m_indexerVelocitySignal = m_indexerMotor.getVelocity();
private final StatusSignal<Current> m_indexerCurrentSignal = m_indexerMotor.getSupplyCurrent();
```

`getVelocity()` does not read the motor. It hands you a `StatusSignal` object, which is a handle on that stored copy. Ask for the handle once, in a field, and keep it for the life of the robot program. These two lines are `TODO 2`.

Then, once per loop, update the copies:

```java
BaseStatusSignal.refreshAll(m_indexerVelocitySignal, m_indexerCurrentSignal, /* and the other four */);
```

`refreshAll` updates every signal you hand it in one pass. Refreshing them one at a time costs a separate wait each. Six signals is barely anything, but count the ones in `drive/ModuleIOTalonFX.java`: ten per swerve module, forty across the four modules, every loop. Doing those one at a time is a measurable part of the loop time.

`setUpdateFrequencyForAll` is a different job:

```java
BaseStatusSignal.setUpdateFrequencyForAll(50d, /* every signal */);
```

That tells the controller how often to broadcast those readings: 50 times a second, once per robot loop.

One of the three numbers in `updateInputs` is not a signal:

```java
inputs.indexerTargetVelocityRPS = PhoenixUtil.getRequestVelocity(m_indexerMotor.getAppliedControl());
```

The target is what we asked for, read back out of the last control request sent to the motor rather than measured. With the request logged beside the measurement, the two can be plotted against each other. A later lesson will cover that.

---

## 7. Two computers, two loops

Your Java runs on the roboRIO, 50 times a second. `Slot0.kP` and `Slot0.kV` do not run there.

They were sent to the Talon FX once, in the constructor, and the Talon FX uses them itself, about a thousand times a second, whether or not your code is doing anything at that moment.

```java
m_indexerMotor.setControl(m_indexerVelocityRequest.withVelocity(velocity));
```

That sends a target, not a voltage. It says "run at 43.33 rotations per second" and stops there. The controller then measures its own velocity, compares it with the target and picks a voltage, roughly twenty times for every one time your `periodic()` runs. The roller holds its speed through the 19 milliseconds in every 20 when none of your code is running at all.

Building the request once, in a field, and calling `.withVelocity(...)` on it each time is deliberate. Building a new `MotionMagicVelocityVoltage` every loop would create 50 objects a second for no gain. That field is TODO 3.

`kV` is the guess made before any error is measured: volts per rotation per second of requested speed. `kP` is the correction applied to whatever error is left over: volts per rotation per second of error.

---

## 8. Stopping is not commanding zero

`indexerStop()` calls `disable()` on the motor.

`disable()` stops sending a control request at all. The controller applies no output and falls back to its neutral mode, which is `Brake` here.

`setControl(request.withVelocity(0))` would be an instruction: hold zero. The control loop keeps running, with zero as its target. Push the roller by hand and the controller pushes back. Jam a ball against it and the controller fights the ball until the current limit stops it.

For a roller, stopping should mean letting go.

That is on the real robot. In `IndexerIOSim` there is no motor controller and no neutral mode, so stopping there is commanding zero. The two files answer the same question differently because they are talking to different things. TODO 8 and TODO 4 are that difference.

---

## 9. The simulation file

`IndexerIOSim.java` is the file that runs when you press a key on your laptop. `Robot.java` line 197 builds it:

```java
m_indexerSubsystem = new IndexerSubsystem(new IndexerIOSim());
```

So nothing you write in `IndexerIOReal` can be tested by running it today. The compiler is the only check on that file until the code goes on a robot. What the simulation checks is `IndexerIOSim`.

The whole model, once you have written it, is three lines:

```java
inputs.indexerTargetVelocityRPS = m_indexerTargetVelocity;
m_indexerVelocity += m_indexerPID.calculate(m_indexerVelocity);
inputs.indexerVelocityRPS = m_indexerVelocity;
```

A PID controller is asked how far the current speed is from the target, and its answer is added straight onto the speed. Ask for 43.33 and the number climbs to 43.33. There is no voltage in it, no gearbox, no mass, and no motor.

Write it anyway, exactly as the top roller does it. Lesson 04 is about everything wrong with those three lines, and it is easier to see once you have written them yourself.

---

## 10. Running it in simulation

**Video:** [Lesson 03, part 2: the simulator](https://youtu.be/gHHWqOSpkeE)

`Ctrl+Shift+P`, then `WPILib: Simulate Robot Code`.

VS Code asks which simulation extensions to use. Tick **Sim GUI** and click OK.

The Sim GUI window is the driver station, the joysticks and the robot's state in one place.

1. Find the **System Joysticks** window. Drag **Keyboard 0** onto the first slot, `Joystick[0]`, in the **Joysticks** window.
2. In **Robot State**, click **Teleoperated**. A disabled robot ignores every button, in the simulator and on the field.
3. `W` `A` `S` `D` drive the chassis. Use them first to check the keyboard is connected to the right slot.
4. **Press `L`.** That is B on the drive controller, which is `Controls.reverseButton`.

The simulator keeps its keyboard map in a file called `simgui-ds.json` in the project folder. `W` `A` `S` `D` are the left stick, the arrow keys are the right stick, `Q` and `O` are the triggers, and `K` `L` `J` `I` are the A, B, X and Y buttons.

### Seeing the number

The roller has no picture in the Sim GUI. Read it off NetworkTables:

1. Menu **NetworkTables**, then **NetworkTables View**.
2. Expand `AdvantageKit`, then `Indexer`.
3. Nine values, three per motor. Yours are `IndexerTargetVelocityRPS`, `IndexerVelocityRPS` and `IndexerCurrentAmps`.

Press `L` and `IndexerTargetVelocityRPS` should go to **−16.67**, which is `indexerOutputVelocityReverse` of −1000 RPM in rotations per second. `IndexerVelocityRPS` should fall to meet it. Press `L` again and both should return to zero.

`TopRollerVelocityRPS` and `LowerKickerVelocityRPS` move at the same time, because the `Reversing` case runs all three. Those two were already written. If yours is the only one of the three sitting at zero, your TODO 3 in `IndexerIOSim` is missing.

`IndexerCurrentAmps` stays at 0.0 forever. `IndexerIOSim` never sets it, because there is no physics in it to draw current from. We will fix that in the next lesson.

---

## 11. What other teams do differently

> Every lesson from here ends with one of these.

You wrote a third copy of the same twenty lines. `IndexerIOReal` now has three motor fields, three pairs of status signals, three control requests, three nearly identical configuration blocks and six nearly identical methods. Change how one motor is configured and you have to remember the other two.

Team 6328 solved this once. In `subsystems/rollers/RollerSystem.java` they have a single class that owns one roller: its IO, its inputs, its logging, and an `Alert` that fires on the driver station if the motor stops answering. Their `Kicker` subsystem holds two of them:

```java
private final RollerSystem rollerFront;
private final RollerSystem rollerBack;
```

One class, used twice, instead of two copies of everything. Their input struct also carries a `connected` flag, so a motor that falls off the bus shows up as a message rather than as a mechanism going down.

You do not need to change anything right now, but keep this in mind for the 2027 season.

---

## Exercise

### 1. Issue and branch

Open an issue on `7166-training-robot`, as in lesson 01. Title it `Lesson 03: indexer roller, <your name>`. Assign it to yourself and note the number.

Branch off `main` and name it `<your-name>_lesson-03`, for example `brandon_lesson-03`.

### 2. Fill in `IndexerIOReal.java`

Eight TODOs, numbered in the file. Every one has a finished top roller or lower kicker version nearby.

| TODO | Line | What |
| --- | --- | --- |
| 1 | 17 | The motor. One `private final TalonFX` field |
| 2 | 25 | The two status signals, velocity and supply current, as fields |
| 3 | 35 | The control request. One `MotionMagicVelocityVoltage`, built at 0, with `.withEnableFOC(true)` |
| 4 | 44 and 84 | The configuration, eight settings, then `MotorAction.configureMotor(...).run()` |
| 5 | 92 | Add your two signals to `setUpdateFrequencyForAll` |
| 6 | 105 and 113 | Add your two signals to `refreshAll`, then fill in the three `inputs.indexer*` numbers |
| 7 | 131 | `indexerVelocity`. One line |
| 8 | 137 | `indexerStop`. One line |

### 3. Fill in `IndexerIOSim.java`

Five TODOs.

| TODO | Line | What |
| --- | --- | --- |
| 1 | 9 | The two doubles, target and current speed |
| 2 | 18 | The `PIDController` |
| 3 | 26 | Three lines in `updateInputs` |
| 4 | 41 | `indexerVelocity`. Two lines |
| 5 | 47 | `indexerStop`. One line |

### 4. Do not change anything else

`IndexerConstants`, `IndexerIO`, `IndexerSubsystem`, `Controls.java` and `Robot.java` are all complete and are not yours to edit this lesson. If you find yourself editing one to make your code compile, your code is wrong.

### 5. Build

`Ctrl+Shift+P`, then `WPILib: Build Robot Code`. Fix errors until it says `BUILD SUCCESSFUL`.

The build runs Spotless, which reformats your code for you. Build once before every commit and the formatter check on your pull request stays green.

### 6. Simulate

Section 10. Press `L` and watch `IndexerVelocityRPS` leave zero.

Take a screenshot with the NetworkTables values visible, or record a few seconds of it. That goes in your pull request.

### 7. Open the pull request

Into `main` of `7166-training-robot`. The description says what you changed, why, and how you know it works, which is the screenshot. End it with `Closes #<your issue number>`.

Your code pull request will get reviewed and then **closed, not merged.** Merging one of them would delete the exercise for the other two, so `main` stays as the starter code. The review is what the pull request is for.

### 8. Then read the solution

After your pull request is open, **not before.**

The solution is on the branch `lesson-03-solution` in the same repo. In GitHub Desktop, **Current Branch**, then pick it. Read `IndexerIOReal.java` and `IndexerIOSim.java`, then switch back to your branch.

**It is fine for these to differ.** There is more than one way to write most of it.

### 9. Hand in the write-up

Two pull requests for this lesson, in two repos:

| Repo | Branch | Contents | What happens to it |
| --- | --- | --- | --- |
| `7166-training-robot` | `<your-name>_lesson-03` | Your two filled-in files | Reviewed, then closed |
| `7166-training-2026` | `<your-name>_lesson-03` | `submissions/<your-name>/lesson-03.md` | Reviewed, then merged |

---

## Troubleshooting

| Symptom | What it means | What to do |
| --- | --- | --- |
| `IndexerIOInputsAutoLogged cannot be resolved to a type` | The project has never been built, so the generated file does not exist yet | `WPILib: Build Robot Code` once. The error goes away on its own |
| `cannot find symbol: m_indexerMotor` | TODO 1 is missing, or the field is named something the later TODOs do not use | Name it the same way the top roller is named |
| The simulator runs but no window appears | The Sim GUI extension was not ticked | Stop it, run `WPILib: Simulate Robot Code` again, tick **Sim GUI** |
| `W` `A` `S` `D` do not drive | `Keyboard 0` was never dragged onto `Joystick[0]`, or **Robot State** is still **Disabled** | Drag it across, then click **Teleoperated** |
| The chassis drives but `L` does nothing | The keyboard slot is right, so the key is not reaching the button. Press `L` and watch the `Joystick[0]` window: button 2 should light up | Check `simgui-ds.json` is still in your project folder. Without it the simulator writes its own map, which gives `Keyboard 0` only four buttons, on `Z`, `X`, `C` and `V`. Restore it with `git checkout simgui-ds.json` and restart |
| `TopRollerVelocityRPS` moves on `L` but `IndexerVelocityRPS` stays at 0 | The state machine is reaching the indexer, so the wiring is fine. Your sim model is not reporting | TODO 3 in `IndexerIOSim` |
| Nothing under `AdvantageKit/Indexer` at all | The robot is disabled, or the simulation is not running | Check **Robot State** is **Teleoperated** |
| `IndexerCurrentAmps` reads 0.0 forever | Correct for this lesson. `IndexerIOSim` has no physics in it and never sets that number | Nothing. Lesson 04 fixes it |
| The Formatter check is red on your pull request | You committed without building | Build once, commit the changed files, push again |

---

## Template

Put this in `submissions/<your-name>/lesson-03.md`.

```markdown
# Lesson 03: <your name>

## The code
- Pull request in `7166-training-robot`:
- Screenshot or clip of the roller running in simulation:
- Roughly how long it took:

## Questions

Short answers are fine.

1. What calls `periodic()`, and how often?
2. Your lesson 02 elevator printed each floor as it moved. Why can `periodic()` not work that way?
3. `periodic()` reads, then logs, then acts. What goes wrong if it acts first and logs afterwards?
4. `Feedback.SensorToMechanismRatio` is 24/12. What would the roller do if it were left at 1?
5. What is a status signal, and why are all six refreshed in one call?
6. Which computer runs `Slot0.kP`, and about how many times a second?
7. Why does `indexerStop()` call `disable()` in `IndexerIOReal` but command zero in `IndexerIOSim`?
8. `IndexerIOReal` does not run in simulation. Which file runs instead, and what did pressing `L` check?
9. `IndexerCurrentAmps` stayed at 0.0 the whole time. Why?
10. Section 11 says you wrote a third copy of the same twenty lines. Name one thing that gets harder because of that.

## Against the solution
- Anything you wrote differently from `lesson-03-solution`:
- Better, worse or the same? Why?

## Notes
- Where you got stuck, and what got you unstuck:
- Anything you had to look up:
- Anything you still do not understand:
```

## Submit

Open the code pull request in `7166-training-robot` first, then the write-up pull request in `7166-training-2026`. Ping Brandon on Slack when both are up.

Lesson 04 continues in this repo and starts from the code you wrote here. Wait until your write-up is merged before you start it.
