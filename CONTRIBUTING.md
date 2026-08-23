# How we work

Adapted from FRC 971's public [CONTRIBUTING.md](https://github.com/frc971/training-2026).

Read this before your first pull request. It is the same process we will try to use on the robot code.

## The short version

```
issue -> branch -> commits -> push -> pull request -> checks -> review -> merge
```

Every change should go through all eight steps. Even a one-line change.

## Issues

Every change starts with an issue. An issue says what needs doing and why, before anyone writes code.

Open one on the repository's **Issues** tab, then **New issue**.

- Title: what needs doing. `Lesson 01: setup notes` or `Turret kP is 0`.
- Body: what the problem is, and how you will know it is fixed.
- Assign it to yourself or who should do it, so nobody else picks it up.

Note the number GitHub gives it. You will use it in the pull request.

## Branches

- **Never commit directly to `main`.** `main` should always be something stable.
- **Branch off `main` for every change.**
- Name it `<your-name>_<short-description>`. For example, `brandon_lesson-01-setup`.
- Keep branches short. If one has been open for more than a few days, it is likely too big.

| Tool | How |
| --- | --- |
| GitHub Desktop | **Current Branch**, then **New Branch** |
| VS Code | Click the branch name in the bottom-left status bar, then **Create new branch** |
| Command line | `git checkout -b brandon_lesson-01-setup` |

## Commits

A commit is a save point with a note attached. Commit each time you finish one small thing. Do not save it all up for the end.

- Keep each commit to one thing.
- Start the summary with a verb: Add, Fix, Update, Remove.
- Summary on the first line, 50 characters or fewer. Blank line. Then more detail if it needs it.

Format:

```
Short summary, max 50 characters

Longer description if it needs one.
```

Example:

```
Tune flywheel PID

Adjust velocity gains to reduce overshoot and improve flywheel recovery after shooting.
```

## Pushing

A commit sits on your laptop until you push it. Until then nobody can review it. If the laptop dies, the work is gone.

| Tool | How |
| --- | --- |
| GitHub Desktop | **Publish branch** the first time, **Push origin** after that |
| VS Code | **Publish Branch** in the Source Control panel, then the sync arrows |
| Command line | `git push origin HEAD` |

Publish and push are the same thing. Both tools say "publish" the first time because the branch is not on GitHub yet.

## Pull requests

You can open one three ways. All three open the same form.

| Tool | How |
| --- | --- |
| GitHub Desktop | **Branch**, then **Create Pull Request** |
| Browser | The banner GitHub shows on the repository page after you push |
| Command line | `gh pr create` |

Rules:

- Always open a pull request into `main`.
- Title: `Lesson 01: setup notes` or `Fix: turret constants comment`.
- The description must say:
  - what the change is,
  - why it is needed,
  - **how you tested it, or how you know it works.**
- End the description with `Closes #12`, using your issue number. GitHub then closes the issue for you when the pull request is merged.
- **Reviews required:** atleast one other person on every pull request.
- Never merge your own without review.
- Prefer **Squash and merge**.

## Reviewing someone else's pull request

You are not hunting for typos. Ask three things:

- Does it do what the description says?
- Can you explain what the changes are doing? If not, ask in a comment. "I do not understand this line" is a good review comment on its own.
- Could you fix this if it broke at a competition?

Approving a pull request means you take on answering questions about it later. Reading each other's work is the point. Do not approve something you have not read.

## Naming things in code

Google's [Java style guide](https://google.github.io/styleguide/javaguide.html#s5-naming):

- Constants `ALL_CAPS`
- Classes `PascalCase`
- Everything else `camelCase`

Grapefruit puts `m_` in front of member variables. It is what the file does, so keep doing it in that file.

## Formatting code

Spotless formats the robot code, and it runs as part of every build. You do not have to run it yourself or remember any rules. Build once before you commit and the formatting is already right.

If you want to run it on its own:

```bash
./gradlew spotlessApply
```

## Windows

We are all on Windows.

Install **Git for Windows** as well as GitHub Desktop. GitHub Desktop keeps its own copy of git inside its program folder. VS Code cannot reach that copy, so its Source Control panel says git is missing. Lesson 01 covers this.

> [!IMPORTANT]
> Keep repositories under `C:\Users\<you>`. Never inside OneDrive. 

## Checks

When you push to the robot code, two checks run on their own.

| Check | What it does | What a red mark means |
| --- | --- | --- |
| **Build** | Compiles the robot code from scratch on a clean machine | Your code does not compile. Usually a file you forgot to commit |
| **Formatter** | Runs `./gradlew spotlessCheck` | Your code is formatted differently from the rest of the repo |

Click a red check to see the log. The error is near the bottom.

### Fixing a red Build

Read the compiler error. It names a file and a line.

The most common cause is a file you didn't commit, `git status` and look for anything untracked that should not be.

### Fixing a red Formatter

Build the code once. That is all.

Formatting runs as part of every build, so `WPILib: Build Robot Code` fixes it. Then commit the changed files and push.

A red Formatter almost always means you edited a file and committed it without building first.

## Getting your pull request merged

On the robot code, `main` is protected. You cannot push to it and you cannot merge until all of this is true:

- [ ] Both checks are green.
- [ ] One person has approved it.
- [ ] Every comment thread is resolved. Reply, then click **Resolve conversation**.
- [ ] You are merging with **Squash and merge**. It is the only option available.

### If you are stuck waiting

Say so! Send a message on slack and ask a mentor or another student to review your code.

### The training repo

This repo has no checks. Only the robot code does. Everything else on this page still applies here: branch, pull request, review, squash merge.

## General

- Keep `main` deployable.
- Respect `.gitignore`. Never commit build output.
- If something is hard to work out from the code, write it down in the code or in the README.
