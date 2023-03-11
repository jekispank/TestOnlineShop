package com.example.testonlineshop.presentation

import android.app.Application
import com.example.testonlineshop.data.LoginDataDb

class SignInApplication: Application() {
    val database: LoginDataDb by lazy { LoginDataDb.getDatabase(this) }
}