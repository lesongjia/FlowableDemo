## FlowableDemo

Run locally
```
./gradlew clean bootRun
```
And the output
```
   : Started FlowableDemoApplication in 7.782 seconds (JVM running for 8.318)
   : Number of process definitions : 1
   : Running instance# : 0
   : Execute action: add user to group
   : Running instance# : 1
   : Action: add user to group finished with failure
   : Running instance# : 0
```
Check out `WorkflowExecutionProcessTest` for process testing