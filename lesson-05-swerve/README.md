# Lesson 05: Swerve

> [!IMPORTANT]
> **Watch this before you start the lesson.** [FRC: Using the CTRE Swerve Generator](https://www.youtube.com/watch?v=3QqltCdH_Qk), by Ed Yazbec.
>
> It walks through Tuner X's Swerve Project Generator: the tool that wrote `TunerConstants.java`, and the tool you run yourself later. Watch the whole thing first. 

## Purpose

Grapefruit's drivetrain is AdvantageKit's swerve template with 7166's numbers in it, and next season's robot starts from the same template (adapted for SystemCore of course). This lesson's goal is to help you find your way around it, then run its tuning steps in simulation, so that you know what each step measures.

In this lesson you will change numbers and read plots. You do not write new code.

## Prerequisites

- **[FRC: Using the CTRE Swerve Generator](https://www.youtube.com/watch?v=3QqltCdH_Qk), watched.** Not optional.
- Lesson 04: write-up merged, code pull request reviewed and closed.
- The `7166-training-robot` repo, cloned in lesson 03.
- AdvantageScope, from lesson 04.

Start from `lesson-04-solution`. There are no TODOs for code in this lesson.

```
git checkout lesson-04-solution
git pull
git checkout -b <your-name>_lesson-05
```

Keep [AdvantageKit's TalonFX(S) Swerve Template](https://docs.advantagekit.org/getting-started/template-projects/talonfx-swerve-template) page open in another tab the whole time. This lesson follows its **Setup** and **Tuning** sections.

## Time box

2-3 hours. (One meeting)

## Learning goals

- [ ] Find `subsystems/drive/` and say what each file in it does.
- [ ] Say which numbers Tuner X wrote, which a routine measured, and which were manually tuned.
- [ ] Know why the simulation has its own gains instead of reading `TunerConstants`.
- [ ] Run the feedforward characterization routine.
- [ ] Say which tuning steps mean something in simulation and which need carpet.
- [ ] Say what order you would tune a real swerve chassis in, and why that order.

### Words this lesson uses

| Word | What it means here |
| --- | --- |
| Template | A starting project somebody else wrote |
| Module | One corner: a wheel, a drive motor, a steer motor, a CANcoder |
| CANcoder | An encoder that knows its angle the moment it powers on |
| Encoder offset | The correction that makes straight ahead read zero |
| Characterization | Driving the robot in a known way and working a constant out from what it did |
| Slip current | The current at which the wheels break loose from the carpet |
| `kV` | Volts per unit of speed. The feedforward's guess before anything is measured |
| Rolling radius | What a worn wheel pressed into carpet rolls on, which is less than the box says |

---

## 1. Where the drivetrain came from

[Video for part 1](https://youtu.be/k8MDorIF35k)

Open `subsystems/drive/Drive.java`. 

## 2. The files

The same split as the indexer, one level deeper.

| File | Its one job | The indexer file it matches |
| --- | --- | --- |
| `ModuleIO.java` | What one corner can do and report | `IndexerIO.java` |
| `ModuleIOTalonFX.java` | Doing it to real motors | `IndexerIOReal.java` |
| `ModuleIOSim.java` | Faking it on a laptop | `IndexerIOSim.java` |
| `Module.java` | One corner, holding a `ModuleIO`, never touching a motor | no twin |
| `Drive.java` | The subsystem, holding four `Module`s | `IndexerSubsystem.java` |
| `generated/TunerConstants.java` | Every number, written by a program | `IndexerConstants.java` |

`Robot.java` picks the implementation the same way lesson 02 showed: `ModuleIOTalonFX` and `GyroIOPigeon2` on the real robot at lines 169 to 174, `ModuleIOSim` and an empty `new GyroIO() {}` in simulation at lines 189 to 194. There is no gyro in simulation, so the heading comes from adding up what the wheels did. In a simulation wheels never slip, so it is perfect. On carpet it will not be.

## 3. Where each number comes from

[Video for part 2](https://youtu.be/5Lfwtaq59cQ)

Every value in `TunerConstants.java` came from one of three places.

| Source | Which numbers | How you change it |
| --- | --- | --- |
| Tuner X wrote it | CAN ids, gear ratios, module positions, inverts, encoder offsets | Run the generator again |
| A routine measured it | Wheel radius, drive `kS` and `kV`, top speed, slip current | Run the routine again |
| A person tuned it with a plot open | The two `kP` values | Tune it again |
| It came with the template | The sim inertias, which only the simulation reads | Section 5 |

Two of these, `kSteerInertia` and `kDriveInertia` on lines 92 and 93, are read only by the simulation.

### The generated file and the saved project

Open `tuner-swerve-project.json` in the repo root, which is the project Tuner X saved, and compare.

| Setting | The saved project | `TunerConstants.java` |
| --- | --- | --- |
| Wheel radius | 1.75 in | 1.696 in, line 84, with `// real - 1.75; 1.707; 1.719; 1.713` |
| Slip current | 120 A | 105 A |
| Encoder offsets | 0.0 | Four measured numbers |
| Encoder inverted | No | Yes |

The **right-hand column is the better data**. That comment on line 84 is four wheel measurements taken across the season as the tread wore, and the offsets are real measured angles. The generator writes the left-hand column, so before you regenerate, copy the right-hand column somewhere first and put it back afterwards.

## 4. The simulation has its own gains

Open `ModuleIOSim.java`, lines 33 to 40. Line 33 says why:

```java
// TunerConstants doesn't support separate sim constants, so they are declared locally
```

The robot's gains live in `TunerConstants` and run on the Talon about a thousand times a second. The simulation runs its own loop fifty times a second in this file. Lesson 04 showed you a gain that is calm at 1000 Hz and unstable at 50 Hz, so the two sets have to be separate.

Line 37 is the one that matters today:

```java
private static final double DRIVE_KV_ROT = 0.91035; // Same units as TunerConstants: (volt * secs) / rotation
```

The template's example robot has a 7.36:1 drive gearbox. Ours is 4.94:1. So our simulation starts with the feedforward for somebody else's robot.

## 5. Tune it

[Video for part 3](https://youtu.be/8IL9aruYWg0)

Set AdvantageScope up in this order:

1. Start the simulation and let the Sim GUI open.
2. **File**, **Connect to Simulator**. Take the plain one.
3. **File**, **Import Layout...**, and pick `AdvantageScope Swerve Calibration.json` from this folder.

The cycle for every change: close the Sim GUI, edit the line, run `WPILib: Simulate Robot Code` with **Sim GUI** ticked, drag **Keyboard 0** onto `Joystick[0]`, set **Teleoperated**, reconnect AdvantageScope.

### Step 1: turn PID

Turn gets tuned first because the feedforward routine drives in a straight line, and it can only do that if the wheels hold their angle. Tap `W`, then tap `A`: the modules keep the last angle they were given, so a tap is a clean 90 degree step.

Edit `TURN_KP`, `ModuleIOSim.java` line 39, and watch the **Turn PID Tuning** tab.

| `TURN_KP` | Within 2 degrees after | Overshoots by |
| --- | --- | --- |
| 8 (the template's) | 0.24 s | nothing |
| 16 | 0.12 s | 1.5 degrees |
| 32 | 0.16 s | 5.8 degrees |
| 4 | 0.48 s | nothing |

16 is the last good doubling. Doubling again makes it slower and worse, which is where you stop. Then `TURN_KD` on line 40 takes the overshoot out: 0.1 costs six hundredths of a second and removes it, 0.5 brakes too early. Leave it at 16 and 0.1.

### Step 2: drive feedforward

**Drive PID Tuning** tab, hold the left arrow. Spinning on the spot at full stick asks each wheel for 3.89 m/s, under the top speed, so nothing is capped.

It measures 5.15. Thirty-two percent fast, and the robot turns at 715 degrees a second when the driver asked for 540. That is not a `kP` problem. The feedforward is for a different gearbox.

Run the routine: Sim GUI, **NetworkTables**, **SmartDashboard**, **Auto Choices**, pick **Drive Simple FF Characterization**, set **Autonomous**, wait seven seconds, set **Disabled**. It prints:

```
********** Drive FF Characterization Results **********
	kS: 0.01897
	kV: 0.61084
```

Put those on lines 36 and 37. Measured becomes 3.90 against a setpoint of 3.89, and the robot turns at 541. On the real robot the same routine runs on carpet and its answer goes into `driveGains` in `TunerConstants`, line 33.

### Step 3: drive PID

`DRIVE_KP`, line 34, with the feedforward fixed.

| `DRIVE_KP` | Within 2 percent after | Settles off by |
| --- | --- | --- |
| 0 | 0.18 s | 0.21 percent |
| 0.05 (the template's) | 0.14 s | 0.14 percent |
| 0.1 | 0.12 s | 0.11 percent |
| 0.2 | 0.12 s | 0.07 percent |
| 0.4 | never | 10.7 percent after 4 s |
| 1.0 | never | swings between about 0.6 and 4.6 m/s |

With the feedforward right, the feedback has almost nothing left to do. Use 0.1, a full doubling below the first bad value, because nothing warns you as you walk up to that edge.

## 6. The steps that need carpet

### Step 4: wheel radius

Choose **Drive Wheel Radius Characterization**, **Autonomous**, let it turn once, which takes about 16 seconds, then **Disabled**.

```
********** Wheel Radius Characterization Results **********
	Wheel Delta: 126.337 radians
	Gyro Delta: 13.148 radians
	Wheel Radius: 0.043 meters, 1.693 inches
```

The model's wheel is 1.696 and it found 1.693, which proves the routine works. In simulation it can only find the number somebody typed in. On carpet it measures what a worn, compressed wheel really rolls on.

### Step 5: max speed

**Max Speed Measurement** tab, hold `W`. It reads 5.01. Before the feedforward fix the same test read 5.32, which is the Kraken's free speed through this gearbox. `Drive` never asks for more than `kSpeedAt12Volts`, which is 5, and with the feedforward right the robot does what it is asked.

### Step 6: slip current

Robot only. Push against a wall, watch the current rise with the speed flat, and the moment the speed jumps the wheel has broken loose. Nothing in this simulation can slip.


## Exercise

### 1. Issue and branch

Open an issue on `7166-training-robot`, titled `Lesson 05: swerve tuning, <your name>`, assigned to yourself. Your branch is `<your-name>_lesson-05`, made off `lesson-04-solution` in the prerequisites.

### 2. Read first

Sections 1 to 4, with the files open. Nothing to type yet.

### 3. Tune the simulation

Section 5, in order: turn PID, feedforward, drive PID. Five numbers change in `ModuleIOSim.java` and nothing else. Then run the two routines in section 6 and write down what they told you.

### 4. Three screenshots

Turn PID Tuning on one tap of `A`. Drive PID Tuning before and after the feedforward. Both routines' terminal output.

### 5. Open the pull request

Into `lesson-04-solution`, the branch you started from, so the diff is five numbers. Reviewed, then closed.

### 6. Then read the solution

Branch `lesson-05-solution`, after your pull request is open.

### 7. Hand in the write-up

`submissions/<your-name>/lesson-05.md` in `7166-training-2026`, as a pull request that does get merged. Bring it to the Tuner session on 10/5.

---

## Troubleshooting

| Symptom | What it means | What to do |
| --- | --- | --- |
| Fields have a line through them | The layout's names do not match the live ones. Live keys start `NT:/AdvantageKit/` | Use the layout in this folder, not the one from the AdvantageKit release |
| The odometry tab is empty, the rest fill | `Odometry/Robot` comes from the vision pose estimator, not from `Drive` | Expected in Grapefruit. Not this lesson's problem |
| Autonomous does nothing | The chooser is on its default | Pick the routine before you enable |
| The routine printed nothing | Never disabled, or disabled inside the first two seconds | It prints when it ends, and records nothing before the ramp starts |
| A tap of `A` is not a 90 degree step | The wheels were already sideways from the last tap | Tap `W` first |
| `W` drives the wrong way | The Sim GUI alliance is set to red | Unset it, or expect the flip. `DriveCommands.java` line 193 |
| You edited `driveGains` and nothing changed | The simulation never reads `TunerConstants` gains | Section 4. Edit `ModuleIOSim.java` |
| Measured speed is still above setpoint after step 2 | `kV` went into `DRIVE_KP`, or into the radians line 38 | Line 37 is `DRIVE_KV_ROT` |
| The start button does not reset the heading | `resetGyro()` does nothing until an alliance is reported | Expected in simulation |

---

## Template

Put this in `submissions/<your-name>/lesson-05.md`.

```markdown
# Lesson 05: <your name>

## The tuning
- Pull request in `7166-training-robot`:
- Turn PID screenshot:
- Drive PID before and after:
- Both routines' output:
- Roughly how long it took:

## Questions

Short answers. One line each is fine.

1. Which file did you edit?
2. Did you tune turn or drive first?
3. What `TURN_KP` and `TURN_KD` did you end up with?
4. What two numbers did the feedforward routine print?
5. What happened when `DRIVE_KP` was 1.0?
6. Which of the six steps cannot run in simulation?
7. List the six steps in the order you ran them.

## Against the solution
- Anything you tuned differently from `lesson-05-solution`:

## Notes
- Where you got stuck:
- Anything you still do not understand:
```

## Submit

Code pull request first, then the write-up. Ping Brandon on Slack when both are up.

---

## Where to read more

| Source | What it is good for |
| --- | --- |
| [FRC: Using the CTRE Swerve Generator](https://www.youtube.com/watch?v=3QqltCdH_Qk) | **Watch first.** Edwin Yazbec walking through the generator that wrote `TunerConstants.java` |
| [AdvantageKit: TalonFX(S) Swerve Template](https://docs.advantagekit.org/getting-started/template-projects/talonfx-swerve-template) | The setup and tuning steps this lesson follows, in full |
| [CTRE: Tuner X Swerve Project Generator](https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/index.html) | What you will click through on 10/5. The link is in `TunerConstants.java` line 19 |
| [AdvantageKit releases](https://github.com/Mechanical-Advantage/AdvantageKit/releases) | Where the template comes from |
| [6328's RobotCode2026Public](https://github.com/Mechanical-Advantage/RobotCode2026Public) | The team that wrote the template, using their own swerve code in 2026 |

The layout file in this folder, `AdvantageScope Swerve Calibration.json`, is from AdvantageKit `v26.0.2`, © 2021-2026 Littleton Robotics, BSD 3-Clause licence, with one key renamed for Grapefruit.
