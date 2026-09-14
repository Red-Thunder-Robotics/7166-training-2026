# Lesson 04: Grant

## The code
- Pull request in `7166-training-robot`:
- AdvantageScope screenshot, target and measured and current:
- Roughly how long it took:a day and a half 

## Questions

1. What does `sim.update(0.02)` do, and what happens without it? - It updates the Motor so you can input stuff. If you didn't have it the Motor would not move.
2. Name something `velocity += pid.calculate(velocity)` leaves out. - volts
3. Ask both models for 1667 rotations per second. What does each one do, and where does the limit on the new one come from? - Where I looked in the sim file and the constants file and the lesson four github.com file and didn't find the limit.
4. What does a moment of inertia measure? Why is it not the same as mass? - The intertia measures how much it will spin without moment applyed
5. What value did you use for moi, and where did the number come from? - It is used for interia calulations and came from the karen motor's config.
6. Which of feedforward and feedback predicts, and which corrects? -  feedforward predicts and feedback corrects
7. Why can you not simply turn `kP` up until the feedback does the whole job? - Becuase it can still go wild.
8. `IndexerCurrentAmps` now moves. Describe its shape while you hold the button, and say why it has that shape. - It is goes up then down when I let go.
9. Why does `Logger.recordOutput` exist separately from `Logger.processInputs`? - It records the output not the inputs 
10. You set the inertia to 0.1. What changed on the plot, and why? - The speed slowed down slower.
11. The top roller still uses the old model. Put both velocity curves on one plot and describe the difference. - One goes down fastly and one tracks a motor     more accutly.
12. Name one thing this simulation still gets wrong about the real roller. - gravity and friction

## Against the solution
- Anything you wrote differently from `lesson-04-solution`:where is that

## Notes
- Where you got stuck:step 5
- Anything you still do not understand: All the math.