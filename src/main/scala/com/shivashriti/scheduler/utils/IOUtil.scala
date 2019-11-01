package com.shivashriti.scheduler.utils

import java.io.File

import com.shivashriti.scheduler.models.{BaseException, ClassDetails}
import com.shivashriti.scheduler.models.BaseException._
import io.circe.jawn.JawnParser

/**
  * Utility for reading input file
  */
object IOUtil {
  private[this] val parser = new JawnParser

  def readClassDetails(file: File): Either[BaseException, List[ClassDetails]] = {

    // parse input json file to get list of classes
    parser.parseFile(file) match {
      case Left(failure) =>
        println("invalid json: "+ failure)
        Left(InvalidJson(failure.message))  // FAIL: notify when error in input file existence or format

      case Right(json) => json.as[List[ClassDetails]] match {
        case Left(failure) =>
          println("decoding failure: "+ failure)
          Left(InvalidJson(failure.message))    // FAIL: notify when input json has incorrect structure, can not decode

        case Right(classList) => Right(classList) // Success: return parsed list of classes
      }
    }
  }
}
