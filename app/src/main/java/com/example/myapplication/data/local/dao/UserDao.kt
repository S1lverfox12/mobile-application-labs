package com.example.myapplication


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): User?

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User>


    @Query("DELETE FROM users")
    suspend fun clearAllUsers()

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}