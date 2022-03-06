package com.foram.routes

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import spray.json.DefaultJsonProtocol._
import akka.util.Timeout
import akka.pattern.ask
import com.foram.Main.topicActor
import com.foram.models.Topic
import com.foram.actors.TopicActor._

import scala.concurrent.duration._

object TopicRoutes {

  import com.foram.JsonFormats._

  implicit val timeout = Timeout(5 seconds)

  val routes =
    pathPrefix("api" / "topics") {
      get {
//        path(IntNumber / "posts") { topic_id =>
//          complete((postDB ? GetPostsByTopic(topic_id)).mapTo[List[Post]])
//        } ~
          path(IntNumber) { id =>
            complete((topicActor ? GetTopicByID(id)).mapTo[Topic])
          } ~
          pathEndOrSingleSlash {
            complete((topicActor ? GetAllTopics).mapTo[List[Topic]])
          }
      } ~
        post {
          entity(as[Topic]) { topic =>
            complete((topicActor ? CreateTopic(topic)).map(_ => StatusCodes.OK))
          }
        } ~
        put {
          path(IntNumber) { id =>
            entity(as[Topic]) { topic =>
              complete((topicActor ? UpdateTopic(id, topic)).map(_ => StatusCodes.OK))
            }
          }
        } ~
        delete {
          path(IntNumber) { id =>
            complete((topicActor ? DeleteTopic(id)).map(_ => StatusCodes.OK))
          }
        }
    }
}

