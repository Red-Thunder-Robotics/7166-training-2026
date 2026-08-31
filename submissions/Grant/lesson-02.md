Pick any mechanism in subsystems/ other than the indexer. List its files and say what each one's job is.    
    VisionConstants.java - sets all the vision constants like poses 
    VisionIo.java - sets what we can do with the vision 
    VisionOLimelight.java - tell it what to do if you are using limelight for the vision
    VisionOMackinac.java - tells it what to do if your using Mackinac(MacMini)
    VisionSubsystem.java - Tell it when to use the vision and what type


What does "this" refer to inside an enum's method? It refers to the to the state Machine

IndexerIO has no logic in it. What is it for? What would break if we deleted it and put the TalonFX code straight into IndexerSubsystem? We could not run SIM and you would not be setting it up right.

You add a value to an enum. What do you need to change? Checking that state or having a defualt state.

What could go wrong if m_indexerMotor were not final? The Motor ID could switch mid code(match) and not run.

One thing in this part you are still unsure about. State Machine and having to memorize the set up.