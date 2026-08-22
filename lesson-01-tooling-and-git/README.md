# Lesson 01: Tooling and git

## Purpose

Get a working development environment, create an issue, and make a pull request. By the end of this lesson you will have changed a file in a shared repository and had someone else merge it. This is how every other lesson will work.

## Prerequisites

- A team laptop you are allowed to install software on.

## Time box

2-3 hours. (Most of it is downloads)

## Learning goals

- [ ] Install VS Code and WPILib 2026.
- [ ] Have a GitHub account with SSH access working.
- [ ] Clone a repo, make a branch, commit, push, open a PR, get it reviewed, merge it.
- [ ] Open Grapefruit codebase and build it.

---

## What you are installing

Three things. Start the first one downloading, then do the other two while you wait.

| # | What | Why you need it |
| --- | --- | --- |
| 1 | WPILib 2026 | Robot tooling, and its own copy of VS Code |
| 2 | Git for Windows | The program that does version control |
| 3 | GitHub Desktop | A window onto git, so you do not have to type commands |

These instructions are for Windows. If you ever work on a Mac or on Linux, each step has the other commands in a drop-down underneath.

## 1. WPILib 2026

Download and run the installer:
<https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html>

> This is a big download. Start it now. Do steps 2 and 3 while it runs.

Choose these options:

- **Install for this User**
- **Everything**
- **Download for this computer only**

This gives you your own copy of VS Code with the robot tools already in it. It is called **2026 WPILib VS Code**.

## 2. GitHub account

Make one if you do not have one: <https://github.com/signup>

Pick a username you would be happy putting on a college or job application. You will have it for a long time. Post the username on slack in #programming (if you have not already).

## 3. Git for Windows

Download it from <https://git-scm.com/download/win> and run it.

The installer asks a lot of questions. Only three of them matter. Take the default on everything else.

| Page | What to pick | Why |
| --- | --- | --- |
| Choosing the default editor | **Use Visual Studio Code as Git's default editor** | The default is Vim. |
| Adjusting the name of the initial branch | **Override**, and type `main` | GitHub calls the main branch `main`. Git's old default is `master`. Matching them saves confusion. |
| Adjusting your PATH environment | Leave it on the middle option, *Git from the command line and also from 3rd-party software* | This is what lets VS Code find git. |

<details>
<summary>macOS and Linux</summary>

**macOS:** run `git --version`. If git is missing, macOS offers to install the developer tools. Say yes.

**Linux:**
```bash
sudo apt install git
```
</details>

### Does WPILib not come with git?

No, WPILib's VS Code has a **Source Control panel** built in. That panel is a screen, not a program. It needs a copy of git to drive, and it looks for one on your computer. WPILib does not install one.

## 4. GitHub Desktop

Download it from <https://desktop.github.com> and run it.

1. Open it. Choose **Sign in to GitHub.com**.
2. Your browser opens. Log in and approve it.
3. GitHub Desktop asks for a name and email for your commits. Use your real name and
   the email on your GitHub account.

That sign-in is also what lets you push. GitHub Desktop saves the login, and VS Code uses the same one.

### Do I need both GitHub Desktop and Git for Windows?

Yes.

GitHub Desktop does carry its own copy of git, but it hides it inside its own program folder where nothing else can reach it. VS Code cannot use that copy. Install only GitHub Desktop and VS Code's Source Control panel will tell you git is missing. Git for Windows puts one copy of git where every program can find it.

## 5. Clone the repos

Cloning means downloading a copy you can work in.

### Pick a folder first

Make a new folder like `C:\Users\<your name>\frc` (does not have to match exactly).

> [!IMPORTANT]
> **Check the folder is not inside OneDrive.** On a lot of Windows laptops, Documents and Desktop are really OneDrive folders, even though the name does not say so. Open the folder in File Explorer and look at the address bar. **If you see OneDrive anywhere in the path, move it.**

### Clone them

In GitHub Desktop: **File**, then **Clone repository**, then the **URL** tab.

Do these one at a time, and set the local path to the `frc` folder you just made:

```
https://github.com/Red-Thunder-Robotics/7166-training-2026
https://github.com/Red-Thunder-Robotics/7166_REBUILT
```

The second one is Grapefruit, the 2026 robot code. Later lessons will use files in it.

<details>
<summary>Command line</summary>

```bash
git clone https://github.com/Red-Thunder-Robotics/7166-training-2026.git
git clone https://github.com/Red-Thunder-Robotics/7166_REBUILT.git
```
</details>

## 6. Open Grapefruit and build it

Open the `7166_REBUILT` folder in WPILib VS Code: **File**, then **Open Folder**.

The first time, it spends several minutes downloading Gradle dependencies. Let it finish before you touch anything. The status bar at the bottom stops moving when it is done.

Then build it **from inside VS Code**:

1. Press `Ctrl+Shift+P`.
2. Type `WPILib: Build Robot Code`.
3. Press Enter.

It should end with `BUILD SUCCESSFUL`. On my machine a clean build takes about 25 seconds. Your first one will take longer.

### Building from terminal

You will see `./gradlew build` in a lot of FRC guides. Type that into a plain terminal and you get this:

```
ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
```

Nothing is broken. WPILib installs its own copy of Java and keeps it to itself. VS Code knows where it is, but a plain terminal does not. Building from VS Code is usually better.

---

## Exercise

Make your first pull request.

There are two routes below. **Pick one.** They do the same thing, because both of them are using the same git. Route A is GitHub Desktop. Route B is VS Code.

Before you start, create the file. Both routes need it.

1. Open the `7166-training-2026` folder in VS Code.
2. Make a folder called `submissions/<your-github-username>/`.
3. In it, make a file called `lesson-01.md`.
4. Copy the template below into it and fill it in.
5. Save.

### Route A: GitHub Desktop

1. Check that **Current Repository** at the top says `7166-training-2026`.
2. Click **Current Branch**, then **New Branch**.
3. Name it `<your-name>/lesson-01`. Click **Create Branch**. (ex `brandon/lesson-01`)
4. Your new file shows up in the list on the left.
5. In the box at the bottom left, type a summary: `Add lesson 01 setup notes`.
6. Click **Commit to `<your-name>/lesson-01`**.
7. Click **Publish branch** at the top.
8. A banner appears. Click **Preview Pull Request**.
9. Write the description. Click **Create pull request**.

### Route B: VS Code

1. Click the branch name in the bottom left of the status bar.
2. Choose **Create new branch**. Name it `<your-name>/lesson-01`. (ex `brandon/lesson-01`)
3. Open the **Source Control** panel. It is the branching icon on the left, or press
   `Ctrl+Shift+G`.
4. Your file is under Changes. Hover over it and click **+**. This is called staging.
5. Type a message in the box: `Add lesson 01 setup notes`.
6. Click **Commit**.
7. Click **Publish Branch**.
8. Open the repository on github.com. A banner offers to open a pull request.
9. Click it, write the description, and create it.

### Then, either way

Ask for a review. **Do not merge it yourself.**

Moving the code is the easy part. The review is what we are practising.

If you get asked to sign in partway through, GitHub Desktop's login did not carry over. Sign in and keep going. It will not ask again.

### Template

```markdown
# Lesson 01: <your name>

> Answers to these can be short, don't feel like you need to write paragraphs.

## Machine
- Windows version:
- Laptop Number/Id:

## Versions
Paste the actual output or what the window says.

- Git for Windows version (in GitHub Desktop: About, or run `git --version`):
- GitHub Desktop version (Help, then About):
- WPILib version (in WPILib VS Code: Help, then About):

## Grapefruit build
- Did `WPILib: Build Robot Code` succeed?
- How long did the first build take? (it's okay to estimate)
- If it failed, the exact error:

## Which route did you use
- GitHub Desktop, VS Code, or both:
- Anything about it that did not make sense:

## Questions

- What is a branch? Why make one instead of editing `main`?
- What is the difference between committing and pushing?
- Your pull request is open. Nobody has merged it. Where does your work exist right now: on your laptop, on GitHub, or both?
- GitHub Desktop and VS Code showed you the same changes. Why?

## Notes
- Anything that took much longer than expected:
- Anything you had to look up:
- Anything you still do not understand:

```

## Submit

Once your PR is open ping Brandon on slack and he will review and merge it.
Please do not start lesson 02 until this PR is merged.
