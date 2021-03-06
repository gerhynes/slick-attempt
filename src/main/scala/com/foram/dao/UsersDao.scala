package com.foram.dao

import com.foram.models.User
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

object UsersDao extends BaseDao {
  def findAll: Future[Seq[User]] = db.run(users.result)

  def findById(id: Int): Future[User] = db.run(users.filter(_.id === id).result.head)

  def findByUsername(username: String): Future[User] = db.run(users.filter(_.username === username).result.head)

  def create(user: User) = db.run(users += user)

  def update(id: Int, user: User) = db.run(users.filter(_.id === user.id).update(user))

  def delete(id: Int) = db.run(users.filter(_.id === id).delete)
}
