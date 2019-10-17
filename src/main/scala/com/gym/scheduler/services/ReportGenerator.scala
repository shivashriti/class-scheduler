package com.gym.scheduler.services

import com.gym.scheduler.models._
import java.io.{BufferedWriter, FileWriter}
import scala.collection.immutable.ListMap

/**
  * Utility used for schedule report generation
  */
object ReportGenerator {

  def generateReport(schedules: List[Schedule], filePath: String): Unit = {
    val bufferedWriter = new BufferedWriter(new FileWriter(filePath))

    // group the schedules by day and sort
    val everydaySchedule = schedules.groupBy(_.slot.dayOfWeek).toSeq
    val everydayScheduleSorted = ListMap(everydaySchedule.sortBy(_._1.getValue) :_*)
    bufferedWriter.append("Gym Class Schedule:\n\n")

    everydayScheduleSorted
      .map {
        case (day, schedulesByDay) =>

          // // group the schedules by time of day and sort
          val timeSchedule = schedulesByDay.groupBy(_.slot.timeOfDay).toSeq
          val timeScheduleSorted = ListMap(timeSchedule.sortBy(_._1.start): _*)

          timeScheduleSorted
            .map {
              case (timeOfDay, schedulesByTime) =>

                // // group the schedules by room and sort
                val roomSchedule = schedulesByTime.groupBy(_.slot.room).toSeq
                val roomScheduleSorted = ListMap(roomSchedule.sortBy(_._1.id): _*)

                roomScheduleSorted
                  .map {
                    case (room, schedulesByRoom) =>
                      bufferedWriter.append(s"$day, Room $room, ${timeOfDay.start} - ${timeOfDay.end}\n")

                      schedulesByRoom
                        .sortWith((s1, s2) => s1.slot.time.compareTo(s2.slot.time) <= 0)  // sort by time of class
                        .map(s => {
                          bufferedWriter.append(s"${s.slot.time} - ${s.classDetails.name} - " +
                            s"${ClassType.toString(s.classDetails.`type`)} - by ${s.classDetails.trainer}\n")
                        })
                  }
            }
          bufferedWriter.append("- - - - -\n")
      }

    bufferedWriter.flush()
    bufferedWriter.close()
    println(s"** Generated a Schedule and saved at $filePath **\n")
  }
}
