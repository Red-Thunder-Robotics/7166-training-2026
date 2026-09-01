# Lesson 03: <Grant>

## The code
- Pull request in `7166-training-robot`: 
- Screenshot or clip of the roller running in simulation: 
- Roughly how long it took: an hour to an hour and a half

## Questions

Short answers are fine.

1. What calls `periodic()`, and how often? every 20 miliseconds
2. Your lesson 02 elevator printed each floor as it moved. Why can `periodic()` not work that way? - It would not loop it would just move on and go again
3. `periodic()` reads, then logs, then acts. What goes wrong if it acts first and logs afterwards? - If it fails then it doesn't log
4. `Feedback.SensorToMechanismRatio` is 24/12. What would the roller do if it were left at 1? - They would spin less than we want to spin it
5. What is a status signal, and why are all six refreshed in one call?  - for space saving and so it doesn't update at different times 
6. Which computer runs `Slot0.kP`, and about how many times a second? - 50 times a second roboRIO
7. Why does `indexerStop()` call `disable()` in `IndexerIOReal` but command zero in `IndexerIOSim`? - Becuase we can't call disable on an inmageary object
8. `IndexerIOReal` does not run in simulation. Which file runs instead, and what did pressing `L` check? - Run in sim and IndexerIOSIM.java
9. `IndexerCurrentAmps` stayed at 0.0 the whole time. Why? - Because it is a SIM it doesn't need a Current
10. Section 11 says you wrote a third copy of the same twenty lines. Name one thing that gets harder because of that. - Finding where your at

## Against the solution
- Anything you wrote differently from `lesson-03-solution`: idk
- Better, worse or the same? Why? idk

## Notes
- Where you got stuck, and what got you unstuck:
- Anything you had to look up:
- Anything you still do not understand: