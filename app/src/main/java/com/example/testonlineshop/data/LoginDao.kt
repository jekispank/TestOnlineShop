package com.example.testonlineshop.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoginDao {

    @Insert
    suspend fun insert(loginData: UserLoginData)

    @Query("SELECT EXISTS(SELECT email FROM login_data WHERE email = :email)")
    suspend fun isUserExist(email: String): Boolean

    @Query("SELECT EXISTS(SELECT first_name FROM login_data WHERE first_name = :firstName)")
    suspend fun isUserAccountExist(firstName: String): Boolean

}