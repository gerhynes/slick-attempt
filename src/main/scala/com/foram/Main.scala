package com.foram

import com.foram.routes._
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.foram.actors._

object Main extends App {
  // Set up actor system
  implicit val system = ActorSystem("foramSystem")
  implicit val materializer = ActorMaterializer()
  // import system.dispatcher

  // Set up actors
  val categoryRepository = system.actorOf(Props[CategoryRepository], "categoryRepository")

  val categoryRouter = new CategoryRoutes()

  val routes = categoryRouter.categoryRoutes

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)

  println(s"Server now online at http://localhost:8080")
}
