package com.shivashriti.scheduler

import java.io.File

import com.shivashriti.scheduler.models.ClassType
import com.shivashriti.scheduler.services._
import com.shivashriti.scheduler.services.{ReportGenerator, Scheduler}
import com.shivashriti.scheduler.utils.IOUtil

/**
  * This is main application that reads input, generates schedule and prepares report
  */
object ClassScheduler extends App {

  val inputFilePath = if(args.nonEmpty) args(0) else "classes.json"
  val outputFilePath = if(args.length > 1) args(1) else "schedule.txt"

  // Step 1: Read input classes
  IOUtil
    .readClassDetails(new File(inputFilePath)) match {
    case Left(e) => println(e)
    case Right(classes) =>
      // Step 2: Generate Schedule for classes
      val (schedules, excessClasses) =
        new Scheduler().schedule(classes)

      // Step 3: Generate Schedule Report in output file
      ReportGenerator.generateReport(schedules, outputFilePath)

      // Notify if some classes could not be scheduled because of time shortage (print number of sessions missed)
      if (excessClasses.nonEmpty) {
        println("Could not schedule following classes due to time limit:")
        excessClasses.map(c => println(s"${c.name} - ${ClassType.toString(c.`type`)}" +
          s" - by ${c.trainer} - missed ${c.frequency} times"))
      }
  }
}
