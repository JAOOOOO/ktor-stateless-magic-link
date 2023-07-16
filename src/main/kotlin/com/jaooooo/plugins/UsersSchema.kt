package com.jaooooo.plugins

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

@Serializable
data class ExposedUser(val email: String)

@Serializable
data class User(val id: Int, val email: String)
class UserService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val email = varchar("email", length = 50)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun getOrCreate(email: String): User = dbQuery {
        Users.select { Users.email eq email }
            .map { User(it[Users.id], it[Users.email]) }
            .singleOrNull() ?: create(email)
    }

    private suspend fun create(email: String): User = dbQuery {
        Users.insert {
            it[Users.email] = email
        }
        Users.select { Users.email eq email }
            .map { User(it[Users.id], it[Users.email]) }
            .single()

    }

    suspend fun read(id: Int): User? {
        return dbQuery {
            Users.select { Users.id eq id }
                .map { User(it[Users.id], it[Users.email]) }
                .singleOrNull()
        }
    }

    suspend fun readByEmail(email: String): User? {
        return dbQuery {
            Users.select { Users.email eq email }
                .map { User(it[Users.id], it[Users.email]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: ExposedUser) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[email] = user.email
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}