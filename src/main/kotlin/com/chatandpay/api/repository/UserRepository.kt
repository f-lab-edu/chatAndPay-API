package com.chatandpay.api.repository

import com.chatandpay.api.domain.User
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class UserRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(user: User): User? {
        entityManager.persist(user)
        return user
    }

    fun findByName(name: String): User? {
        val query = entityManager.createQuery("SELECT u FROM User u WHERE u.name = :name", User::class.java)
        query.setParameter("name", name)
        return query.resultList.firstOrNull()
    }

    fun findByCellphone(cellPhone: String) : User? {
        val query = entityManager.createQuery("SELECT u FROM User u WHERE u.cellphone = :cellphone", User::class.java)
        query.setParameter("cellphone", cellPhone)
        return query.resultList.firstOrNull()
    }

    fun findByUlid(ulid: String) : User? {
        val query = entityManager.createQuery("SELECT u FROM User u WHERE u.ulid = :ulid", User::class.java)
        query.setParameter("ulid", ulid)
        return query.resultList.firstOrNull()
    }

    fun findByUserId(userId: String) : User? {
        val query = entityManager.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User::class.java)
        query.setParameter("userId", userId)
        return query.resultList.firstOrNull()
    }

    fun existsByCellphoneAndIdNot(cellPhone: String, id: Long) : Boolean {

        val query = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.cellphone = :cellphone AND u.id != :id",
            java.lang.Long::class.java
        )

        query.setParameter("cellphone", cellPhone)
        query.setParameter("id", id)
        return query.singleResult > 0
    }

    fun existsByUserIdAndIdNot(userId: String, id: Long) : Boolean {
        val query = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.userId = :userId AND u.id != :id",
            java.lang.Long::class.java
        )

        query.setParameter("userId", userId)
        query.setParameter("id", id)
        return query.singleResult > 0
    }

    fun findById(id: Long): User? {
        return entityManager.find(User::class.java, id)
    }

    fun findByEmail(email: String) : User? {
        val query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User::class.java)
        query.setParameter("email", email)
        return query.resultList.firstOrNull()
    }

    fun delete(user: User) : Boolean {
         return try {
             val managedUser = entityManager.merge(user)
             entityManager.remove(managedUser)
             true
        } catch (e: Exception) {
             false
        }
    }

}
