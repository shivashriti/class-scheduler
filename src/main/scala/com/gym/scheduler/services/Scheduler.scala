package com.gym.scheduler.services

import java.time.{DayOfWeek, Duration}
import java.time.temporal.ChronoUnit._
import com.gym.scheduler.models._
import TimeOfDay._ , Room._

import scala.util.Random

/**
  * Scheduler generates a schedule by randomly choosing slots for classes one by one
  * It also maintains a list of classes that could not be scheduled after the lunch and post officetime are over in all rooms
  */
class Scheduler {

  def schedule(classes: List[ClassDetails]): (List[Schedule], List[ClassDetails]) = {
    val allSlots =
      (1 to numOfDays).toList.flatMap(day => {
        List(Red, Blue).flatMap(room => {
          List(Lunch, PostOffice).map( timeOfDay =>
            Slot(DayOfWeek.of(day), room, timeOfDay, timeOfDay.start)
          )
        })
      })
    generateRandomSchedule(classes, allSlots, Nil, Nil)
  }

  // recursively schedule class and maintain empty slot and list of excess classes
  private def generateRandomSchedule(classes: List[ClassDetails],
                     availableSlots: List[Slot],
                     schedules: List[Schedule],
                     excessClasses: List[ClassDetails]): (List[Schedule], List[ClassDetails]) = {
    classes match {
      case Nil => (schedules, excessClasses)
      case h :: tail =>
        randomSlotForClass(h.`type`.duration, availableSlots) match {
          case Left(_) =>
            generateRandomSchedule(tail, availableSlots, schedules, h +: excessClasses)

          case Right(slot) =>
            val leftOverList =
              if(h.frequency == 1) tail
              else tail :+ h.copy(frequency = h.frequency - 1)
            val updatedSlots =
              slot.copy(time = slot.time.plus(h.`type`.duration, MINUTES)) +:
              availableSlots.filterNot(_ == slot)
            generateRandomSchedule(leftOverList, updatedSlots, Schedule(slot, h) +: schedules, excessClasses)
        }
    }
  }

  // choose one slot for a class from the list of available empty slots, notify when no fitting slot is available
  private def randomSlotForClass(classDuration: Int, availableSlots: List[Slot]): Either[Boolean, Slot] = {
    val validSlots = availableSlots.filter(s => {
      s.timeOfDay match {
        case Lunch => Duration.between(s.time, Lunch.end).toMinutes >= classDuration
        case PostOffice => Duration.between(s.time, PostOffice.end).toMinutes >= classDuration
      }
    })
    validSlots match {
      case Nil => Left(false)
      case _ => Right(validSlots(Random.nextInt(validSlots.length)))
    }
  }
}
