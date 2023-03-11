package com.example.testonlineshop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserLoginData::class], version = 1, exportSchema = false)
abstract class LoginDataDb : RoomDatabase() {

    abstract fun getDao(): LoginDao

    companion object {

        @Volatile
        private var INSTANCE: LoginDataDb? = null

        fun getDatabase(context: Context): LoginDataDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, LoginDataDb::class.java,
                    "login_data"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}