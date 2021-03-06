package com.foram.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.foram.Main.categoryActor
import com.foram.actors.CategoryActor._
import com.foram.models.Category
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object CategoryRoutes {

  import com.foram.JsonFormats._

  implicit val timeout = Timeout(5 seconds)

  val routes =
    pathPrefix("api" / "categories") {
      get {
        path(IntNumber) { id =>
          complete((categoryActor ? GetCategoryByID(id)).mapTo[Category])
        } ~
          pathEndOrSingleSlash {
            complete((categoryActor ? GetAllCategories).mapTo[List[Category]])
          }
      } ~
        post {
          entity(as[Category]) { category =>
            complete((categoryActor ? CreateCategory(category)).map(_ => StatusCodes.OK))
          }
        } ~
        put {
          path(IntNumber) { id =>
            entity(as[Category]) { category =>
              complete((categoryActor ? UpdateCategory(id, category)).map(_ => StatusCodes.OK))
            }
          }
        } ~
        delete {
          path(IntNumber) { id =>
            complete((categoryActor ? DeleteCategory(id)).map(_ => StatusCodes.OK))
          }
        }
    }
}
