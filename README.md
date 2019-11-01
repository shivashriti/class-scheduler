## Class Scheduler

### Quick Start
####Running App using jar

`java –jar class-scheduler.jar`

####Running App with sbt

- On root of the project, run `sbt run`, this will use default input file **classes.json** to read the list of classes, so ensure to keep that file ready before running app.

    `~/myWorkspace/class-scheduler 👉 $sbt run`

*This will execute app for input file **classes.json** and write the generated schedule to **schedule.txt** in the root directory. If some classes could not be scheduled or partially scheduled due to time shortage, their list will be collected and reported on console mentioning the number of sessions missed.*

- To provide other input/output files, give them as arguments when you run the app

    `~/myWorkspace/class-scheduler 👉 $sbt "run inputClasses.json outputSchedule.txt"`
