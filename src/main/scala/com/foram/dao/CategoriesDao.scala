package com.foram.dao

import com.foram.models.Category
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future

object CategoriesDao extends BaseDao {
  def findAll: Future[Seq[Category]] = db.run(categories.result)
  def findById(id: Int): Future[Category] = db.run(categories.filter(_.id === id).result.head)
  def create(category: Category) = categories.returning(categories.map(_.id)) += category
  def delete(id: Int) = db.run(categories.filter(_.id === id).delete)
}
