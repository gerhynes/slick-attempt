package com.foram.actors

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{Actor, ActorLogging, Props}
import com.foram.dao.UsersDao
import com.foram.models.{User}
import scala.util.{Failure, Success}

object UserActor {
  case class ActionPerformed(action: String)
  case object GetAllUsers
  case class GetUserByUsername(username: String)
  case class GetUserByID(id: Int)
  case class CreateUser(user: User)
  case class UpdateUser(id: Int, user: User)
  case class DeleteUser(id: Int)
  case object OperationSuccess
}


class UserActor extends Actor with ActorLogging {

  import UserActor._

  override def receive: Receive = {
    case GetAllUsers =>
      println(s"Searching for users")
      val allUsers = UsersDao.findAll
      val originalSender = sender
      allUsers.onComplete {
        case Success(users) => originalSender ! users.toList
        case Failure(failure) => println("Users not found")
      }

    case GetUserByID(id) =>
      println(s"Finding user with id: $id")
      val user = UsersDao.findById(id)
      val originalSender = sender
      user.onComplete {
        case Success(user) => originalSender ! user
        case Failure(failure) => println(s"User $id not found")
      }

    case GetUserByUsername(username) =>
      println(s"Finding user with username: $username")
      val user = UsersDao.findByUsername(username)
      val originalSender = sender
      user.onComplete {
        case Success(user) => originalSender ! user
        case Failure(exception) => println(s"User $username not found")
      }

    case CreateUser(user) =>
      println(s"Creating user $user")
      UsersDao.create(user)
      sender() ! ActionPerformed(s"User ${user.name} created.")

    case UpdateUser(id, user) =>
      log.info(s"Updating user $user")
      val result = UsersDao.update(id, user)
      val originalSender = sender
      result.onComplete {
        case Success(success) => originalSender ! ActionPerformed(s"User $id updated")
        case Failure(failure) => println(s"Unable to update user $id")
      }

    case DeleteUser(id) =>
      println(s"Removing user id $id")
      val user = UsersDao.delete(id)
      val originalSender = sender
      user.onComplete {
        case Success(success) => originalSender ! ActionPerformed(s"User $id deleted")
        case Failure(failure) => println(s"Unable to delete user $id")
      }
  }
}
