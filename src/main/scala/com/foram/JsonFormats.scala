package com.foram
import com.foram.models.Category
import spray.json.DefaultJsonProtocol

object JsonFormats {
  import DefaultJsonProtocol._

  implicit val categoryFormat = jsonFormat5(Category)
}
