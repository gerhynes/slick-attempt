package com.foram.routes

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import spray.json.DefaultJsonProtocol._
import akka.util.Timeout
import akka.pattern.ask
import com.foram.Main.userActor
import com.foram.models.User
import com.foram.actors.UserActor._

object UserRoutes {
  import com.foram.JsonFormats._

  implicit val timeout = Timeout(5 seconds)

  val routes =
    pathPrefix("api" / "users") {
      get {
//        path(Segment / "topics") { username =>
//          complete((topicActor ? GetTopicsByUsername(username)).mapTo[List[Topic]])
//        } ~
//          path(Segment / "posts") { username =>
//            complete((postActor ? GetPostsByUsername(username)).mapTo[List[Post]])
//          } ~
          path(Segment) { username =>
            complete((userActor ? GetUserByUsername(username)).mapTo[User])
          } ~
          pathEndOrSingleSlash {
            complete((userActor ? GetAllUsers).mapTo[List[User]])
          }
      } ~
        post {
          entity(as[User]) { user =>
            complete((userActor ? CreateUser(user)).map(_ => StatusCodes.OK))
          }
        } ~
        put {
          path(IntNumber) { id =>
            entity(as[User]) { user =>
              complete((userActor ? UpdateUser(id, user)).map(_ => StatusCodes.OK))
            }
          }
        } ~
        delete {
          path(IntNumber) { id =>
            complete((userActor ? DeleteUser(id)).map(_ => StatusCodes.OK))
          }
        }
    }
}
