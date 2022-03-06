package com.foram

import scala.concurrent.ExecutionContext.Implicits.global
import com.foram.routes._
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.foram.actors._

import scala.util.{Failure, Success}

object Main extends App {
  // Set up actor system
  implicit val system = ActorSystem("foramSystem")
  implicit val materializer = ActorMaterializer()

  // Set up actors
  val categoryActor = system.actorOf(Props[CategoryActor], "categoryActor")
  val userActor = system.actorOf(Props[UserActor], "userActor")

  // Get all routes
  val routes = MainRouter.routes

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)

  bindingFuture.onComplete {
    case Success(bound) => println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) => Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }
}
