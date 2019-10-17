package com.gym.scheduler

import java.time.{DayOfWeek, LocalTime}

import io.circe.Decoder

/**
  * models define the structure of entities used application.
  * It maintains the Days for class, Times (Lunch, Post office), Rooms. These can be updated easily in case more days, rooms etc are available in future
  */
package object models {

  val numOfDays = 5

  sealed trait ClassType{
    def duration: Int
  }

  object ClassType {
    case object StrengthAndConditioning extends ClassType{
      def duration = 45
    }
    case object Cardio extends ClassType{
      def duration = 60
    }
    case object MindAndBody extends ClassType{
      def duration = 30
    }
    case object Dance extends ClassType{
      def duration = 60
    }

    def toString(cType: ClassType): String = cType match {
      case StrengthAndConditioning => "Strength and Conditioning"
      case Cardio => "Cardio"
      case MindAndBody => "Mind and Body"
      case Dance => "Dance"
    }

    implicit class CaseInsensitiveRegex(sc: StringContext) {
      def ci = ( "(?i)" + sc.parts.mkString ).r
    }

    def fromString(cType: String): Option[ClassType] = cType match {
      case ci"Strength and Conditioning" => Some(StrengthAndConditioning)
      case ci"Cardio" => Some(Cardio)
      case ci"Mind and Body" => Some(MindAndBody)
      case ci"Dance" => Some(Dance)
      case _ => None
    }
  }

  case class ClassDetails(name: String, `type`: ClassType, frequency: Int, trainer: String)

  object ClassDetails {
    implicit val decoder: Decoder[ClassDetails] = Decoder.instance { h =>
      for {
        name <- h.get[String]("name")
        classType <- h.get[String]("type")
        frequency <- h.get[Int]("frequency")
        trainer <- h.get[String]("trainer")
      } yield ClassDetails(name, ClassType.fromString(classType).getOrElse(ClassType.Dance), frequency, trainer)
    }
  }

  sealed trait Room{
    def id: Int
  }
  object Room {
    case object Red extends Room {
      def id: Int = 1
    }
    case object Blue extends Room {
      def id: Int = 2
    }
  }

  sealed trait TimeOfDay {
    def start: LocalTime
    def end: LocalTime
  }
  object TimeOfDay{
    case object Lunch extends TimeOfDay {
      def start: LocalTime = LocalTime.of(11,0)
      def end: LocalTime = LocalTime.of(13,0)
    }
    case object PostOffice extends TimeOfDay {
      def start: LocalTime = LocalTime.of(18,0)
      def end: LocalTime = LocalTime.of(20,0)
    }
  }

  case class Slot(dayOfWeek: DayOfWeek, room: Room, timeOfDay: TimeOfDay, time: LocalTime)
  case class Schedule(slot: Slot, classDetails: ClassDetails)

  sealed trait BaseException
  object BaseException {
    case class InvalidJson(message: String = "Invalid Json") extends BaseException
  }
}
