lazy val root = (project in file("."))
  .settings(
    name := "Gym Class Scheduler",
    version := "0.0.1",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
        "io.circe" %% "circe-core" % "0.11.1",
        "io.circe" %% "circe-parser" % "0.11.1"     // for json parsing
    ),
    assemblyJarName in assembly := "gym-class-scheduler.jar",
    test in assembly := {},
    mainClass in assembly := Some("com.gym.scheduler.GymClassScheduler")
  )
