package com.foram.actors

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{Actor, ActorLogging, Props}
import com.foram.dao.CategoriesDao
import com.foram.models.{Category, Categories}
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
      println(s"CategoryRepositoryActor Searching for categories")
      val allCategories = CategoriesDao.findAll
      allCategories.onComplete {
        case Success(categories) => sender() ! Categories(categories)
        case Failure(failure) => println("Data not found")
      }

    case GetCategoryByID(id) =>
      println(s"Finding category with id: $id")
      val category = CategoriesDao.findById(id)
      category.onComplete {
        case Success(category) => sender() ! category
        case Failure(failure) => println(s"$id category not found")
      }

    case CreateCategory(category) =>
      println(s"Creating category $category")
      CategoriesDao.create(category)
      sender() ! ActionPerformed(s"Category ${category.name} created.")

    // TODO
    //    case UpdateCategory(id, category) =>
    //      log.info(s"Updating category $category")
    //      categories = categories + (id -> category)
    //      sender() ! OperationSuccess

    case DeleteCategory(id) =>
      println(s"Removing category id $id")
      val category = CategoriesDao.delete(id)
      category.onComplete {
        case Success(category) => sender() ! ActionPerformed(s"Category $id deleted")
        case Failure(failure) => println(s"Unable to delete category $id")
      }
  }
}

