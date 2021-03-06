package com.foram.dao

import com.foram.models.Category
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

object CategoriesDao extends BaseDao {
  def findAll: Future[Seq[Category]] = db.run(categories.result)

  def findById(id: Int): Future[Category] = db.run(categories.filter(_.id === id).result.head)

  def create(category: Category) = db.run(categories += category)

  def update(id: Int, category: Category) = db.run(categories.filter(_.id === category.id).update(category))

  def delete(id: Int) = db.run(categories.filter(_.id === id).delete)
}
