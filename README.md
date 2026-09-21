# 7166 Software Training, 2026

Read a lesson. Do the task. Open a pull request. Wait for review.

Each lesson is one folder with a README. You can work through it on your own at your own pace.

## The lessons

| # | Lesson | Time | Submission |
| --- | --- | --- | --- |
| 01 | [Tooling and git](lesson-01-tooling-and-git/README.md) | 2-3 h | A merged PR with your toolchain versions in it |
| 02 | [Java for robot code](lesson-02-java-for-robot-code/README.md) | 2-3 h | Two parts: notes on real robot code, then an elevator program that passes tests. |
| 03 | [Subsystem](lesson-03-subsystem/README.md) | 2-3 h | The indexer roller, written by you, turning in simulation |
| 04 | [Simulation and telemetry](lesson-04-simulation-and-telemetry/README.md) | 2-3 h | An AdvantageScope plot of the indexer spinning up |
| 05 | [Swerve](lesson-05-swerve/README.md) | 3 h | A swerve drive you tuned yourself in simulation, with the template's own routines |
| 06 | Phoenix Tuner and CAN | | In person, on the test chassis. An ID map for the bus, and swerve constants you generated yourself |
| 07 | Commands | | Two commands that interrupt each other correctly |
| 08 | States | | A robot state you added, end to end |
| 09 | Closed loop control | | A plot of setpoint against measurement, and an explanation of what each gain did |
| 10 | Everybot in sim | | A simulated Everybot, built one mechanism at a time |

From 03 on you write robot code rather than read it, in a second repo called `7166-training-robot`. It is a copy of Grapefruit with pieces taken out for you to fill back in. 

## Rules of pace

1. **Do the lessons in order.** 02 needs 01. 04 needs 03.
2. **Wait for your last PR to be reviewed before you start the next lesson.** Write-ups get merged. Code pull requests get reviewed and then closed. Both count as done. If nobody has reviewed it, say so. Do not work ahead.
3. **No AI on lesson 02, Part 2.** That part is about what you can write yourself. Everywhere else, use whatever helps. You still have to explain every line you hand in.
4. **If a lesson takes much more than the time block, stop and ask.** That usually means the lesson is wrong or something is broken.

## How you hand things back

You have two ways to use git: **GitHub Desktop** and **VS Code's Source Control panel**. Pick either. They do the same thing. Lesson 01 covers both.

Everything goes in through a pull request. You each get one folder:

```
submissions/<your-name>/lesson-01.md
                        lesson-02.md
                        ...
```

`<your-name>` is your first name, lowercase. So `submissions/brandon/lesson-01.md`.

Nobody else writes in your folder.

The workflow is in [CONTRIBUTING.md](CONTRIBUTING.md). Read it before your first PR.

## The other repos

Some lessons send you into repos outside this one:

| Repo | What it is |
| --- | --- |
| [`7166-training-robot`](https://github.com/Red-Thunder-Robotics/7166-training-robot) | Where you write the code from lesson 03 on. A full-history copy of Grapefruit with pieces removed for you to fill back in |
| [`7166_REBUILT`](https://github.com/Red-Thunder-Robotics/7166_REBUILT) | Grapefruit. The 2026 robot code. Most lessons point at files in here |
| `7166-ProjectThunder` | A second robot, with an arm and an elevator. Might be used later for controls work |
| `604-controls-lab` | The controls workshop. Not yet delivered; lesson 09 covers the same ground |
| `7166-eventlogs` | Every match log from the 2026 season. Private to the team, and you never need it to write code |

## Credit

The shape of this course, the lesson layout, and the elevator task in lesson 02 come from FRC 971 Spartan Robotics' public [training-2026](https://github.com/frc971/training-2026) repo.

Other public code these lessons point at:

| Team | Repo | Why |
| --- | --- | --- |
| 6328 Mechanical Advantage | [RobotCode2026Public](https://github.com/Mechanical-Advantage/RobotCode2026Public), [AdvantageKit](https://github.com/Mechanical-Advantage/AdvantageKit) | They wrote the logging library Grapefruit runs, and the IO pattern every mechanism uses. Our whole `subsystems/drive/` folder is their code. Lessons 03, 04, 08 and 09 quote their 2026 Java |
| 971 Spartan Robotics | [training-2026](https://github.com/frc971/training-2026) | The course this one is based on |
| 1678 Citrus Circuits | [C2026-Public](https://github.com/frc1678/C2026-Public) | Where our CI workflows and formatting config came from. Every mechanism reuses one shared motor class |
| 254 The Cheesy Poofs | [2025-Public](https://github.com/Team254/FRC-2025-Public) | Lesson 08 reads their superstructure state machine, to show what a state graph is for and why we do not need one |
