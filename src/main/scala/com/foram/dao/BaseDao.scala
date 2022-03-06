package com.foram.dao

import com.foram.models.{CategoriesTable, UsersTable}
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._

trait BaseDao {
  def db = Database.forConfig("postgresDB")
  val categories = TableQuery[CategoriesTable]
  val users = TableQuery[UsersTable]
}
