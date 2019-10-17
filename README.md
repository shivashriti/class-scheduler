## Gym Class Scheduler

### Instructions
**To execute App using jar**

`java –jar gym-class-scheduler.jar`

**To execute App with sbt**

- On the root of the project, run `sbt compile` to compile the app.

    `~/myWorkspace/gym-class-scheduler 👉 $sbt compile`

- On successful compilation, run `sbt run` to execute the app, this will use default input file **classes.json**  so ensure to keep that file ready before running app.

    `~/myWorkspace/gym-class-scheduler 👉 $sbt run`

*This will execute app for input file **classes.json** and write the generated schedule to **schedule.txt** in the root directory. If some classes could not be scheduled or partially scheduled due to time shortage, their list will be collected and reported on console mentioning the number of sessions missed.*

- To provide other input/output files, simply give them as arguments when you run the app

    `~/myWorkspace/gym-class-scheduler 👉 $sbt "run inputClasses.json outputSchedule.txt"`

        
### Solution Approach

- The list of all classes is read from json file and given to scheduler.
- Scheduler maintains a list of all available (empty) slots for classes. A Slot is combination of Day of week, Room and The Time of day (Lunch or Post Office). For each class, scheduler first checks what are the available slots where this class can be scheduled, then it **randomly** chooses one of those slots and update the list of schedule. The used slot is updated and class frequency is reduced. Scheduler then proceeds to another class and continuous to do so till either all classes have been scheduled or there are no more time slots available.
- The list of Scheduled classes is then given to Report generator, which sorts the list based on Day, Time and Room and prepares a readable schedule.
- Scheduler also maintains a list of the excess classes, that could not be scheduled due to shortage of overall time. If there are any excess classes, it notifies them on console as follows. *This will occur only if the input is large enough for consuming the overall time available. (Not for the sample input provided)*
```
Could not schedule following classes due to time limit
Hot Yoga - MindAndBody - by Kimberly Chroma - missed 2 times
Fit Hop - Dance - by Richard Drop - missed 1 times
```
- All entities/ constants and configurations are maintained [here](src/main/scala/com/gym/scheduler/models/package.scala), so in future if there is need to add extra rooms, or slots in morning or Saturday etc, this can be changed at single place very easily without disturbing the scheduler's main function.

### Design
** Models:**
- ClassType: Represents the categories of a gym class e.g. Strength and Conditioning
- TimeOfDay: Represents Lunch or Post office hours with their start and end timings
- Room: Possible rooms currently Red and Blue
- Slot: Combination of a day, room, time of day and actual time duration.
- Schedule: Combination of a Slot and class scheduled in that slot.

**Utils:**
- IOUtil: To facilitate reading and writing to csv file.

**Services**
- Scheduler: generates a schedule by randomly choosing slots for classes one by one. t also maintains a list of classes that could not be scheduled after the lunch and post officetime are over in all rooms.
- ReportGenerator: Generates the report in output file based on the schedule generated.

**Scheduler App**
- The Main application that reads input, generates schedule and prepares report using the services and utils.
