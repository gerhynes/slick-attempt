package com.foram
import com.foram.models.{User, Category, Topic}
import spray.json.DefaultJsonProtocol

object JsonFormats {
  import DefaultJsonProtocol._

  implicit val categoryFormat = jsonFormat5(Category)
  implicit val userFormat = jsonFormat4(User)
  implicit val topicFormat = jsonFormat7(Topic)

}
