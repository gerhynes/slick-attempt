package com.foram.actors

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{Actor, ActorLogging, Props}
import com.foram.dao.CategoriesDao
import com.foram.models.{Category}
import scala.util.{Failure, Success}

object CategoryRepository {
  case class ActionPerformed(action: String)

  case object GetAllCategories

  case class GetCategoryByID(id: Int)

  case class CreateCategory(category: Category)

  case class UpdateCategory(id: Int, category: Category)

  case class DeleteCategory(id: Int)

  case object OperationSuccess

  def props = Props[CategoryRepository]
}

class CategoryRepository extends Actor with ActorLogging {

  import CategoryRepository._

  override def receive: Receive = {
    case GetAllCategories =>
      println(s"Searching for categories")
      val allCategories = CategoriesDao.findAll
      val originalSender = sender
      allCategories.onComplete {
        case Success(categories) => originalSender ! categories.toList
        case Failure(failure) => println("Data not found")
      }

    case GetCategoryByID(id) =>
      println(s"Finding category with id: $id")
      val category = CategoriesDao.findById(id)
      val originalSender = sender
      category.onComplete {
        case Success(category) => originalSender ! category
        case Failure(failure) => println(s"$id category not found")
      }

    case CreateCategory(category) =>
      println(s"Creating category $category")
      CategoriesDao.create(category)
      sender() ! ActionPerformed(s"Category ${category.name} created.")

    case UpdateCategory(id, category) =>
      log.info(s"Updating category $category")
      val result = CategoriesDao.update(id, category)
      val originalSender = sender
      result.onComplete {
        case Success(success) => originalSender ! ActionPerformed(s"Category $id updated")
        case Failure(failure) => println(s"Unable to update category $id")
      }

    case DeleteCategory(id) =>
      println(s"Removing category id $id")
      val category = CategoriesDao.delete(id)
      val originalSender = sender
      category.onComplete {
        case Success(success) => originalSender ! ActionPerformed(s"Category $id deleted")
        case Failure(failure) => println(s"Unable to delete category $id")
      }
  }
}

