package com.foram.routes

import scala.concurrent._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import akka.util.Timeout
import akka.pattern.ask
import com.foram.models.{Category, Categories}
import com.foram.actors.CategoryRepository._
import com.foram.Main.{categoryRepository}

import scala.concurrent.duration._

class CategoryRoutes {

  import com.foram.JsonFormats._

  implicit val timeout = Timeout(2 seconds)

  val categoryRoutes =
    pathPrefix("api" / "categories") {
      get {
          path(IntNumber) { id =>
            complete((categoryRepository ? GetCategoryByID(id)).mapTo[Option[Category]])
          } ~
          pathEndOrSingleSlash {
            complete((categoryRepository ? GetAllCategories).mapTo[List[Category]])
          }
      }
    }
}
