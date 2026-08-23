# 7166 Software Training, 2026

Read a lesson. Do the task. Open a pull request. Wait for review.

Each lesson is one folder with a README. You can work through it on your own at your own pace.

## The lessons

| # | Lesson | Time | Submission |
| --- | --- | --- | --- |
| 01 | [Tooling and git](lesson-01-tooling-and-git/README.md) | 2-3 h | A merged PR with your toolchain versions in it |
| 02 | [Java for robot code](lesson-02-java-for-robot-code/README.md) | 2-3 h | Two parts: notes on real robot code, then an elevator program that passes tests. |
| 03 | [Subsystems and IO](lesson-03-subsystems-and-io/README.md) | 2-3 h | A written trace of one request from the state machine down to the motor |
| 04 | [Superstructure and state](lesson-04-superstructure-and-state/README.md) | 2-3 h | A branch where you added a robot state. |

Lessons 05 and 06 are not in this repo yet. 05 is controls, which means PID and feedforward. The plan is to do both in-person.

## Rules of pace

1. **Do the lessons in order.** 02 needs 01. 04 needs 03.
2. **Wait for your last PR to be merged before you start the next lesson.** If nobody has reviewed it, say so. Do not work ahead.
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
| [`7166_REBUILT`](https://github.com/Red-Thunder-Robotics/7166_REBUILT) | Grapefruit. The 2026 robot code. Most lessons point at files in here |
| `7166-ProjectThunder` | A second robot, with an arm and an elevator. Might be used later for controls work |
| `604-controls-lab` | The controls workshop, for lesson 05 |
| `7166-eventlogs` | Every match log from the 2026 season. Private to the team, and you never need it to write code |

## Credit

The shape of this course, the lesson layout, and the elevator task in lesson 02 come from FRC 971 Spartan Robotics' public [training-2026](https://github.com/frc971/training-2026) repo.

Other public code these lessons point at:

| Team | Repo | Why |
| --- | --- | --- |
| 6328 Mechanical Advantage | [RobotCode2026Public](https://github.com/Mechanical-Advantage/RobotCode2026Public), [AdvantageKit](https://github.com/Mechanical-Advantage/AdvantageKit) | They wrote the logging library Grapefruit runs, and the IO pattern every mechanism uses. Their 2026 hardware code is in C++ |
| 971 Spartan Robotics | [training-2026](https://github.com/frc971/training-2026) | The course this one is based on |
| 1678 Citrus Circuits | [C2026-Public](https://github.com/frc1678/C2026-Public) | Where our CI workflows and formatting config came from. Every mechanism reuses one shared motor class |
