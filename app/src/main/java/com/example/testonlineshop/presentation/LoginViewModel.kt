package com.example.testonlineshop.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testonlineshop.Const
import com.example.testonlineshop.data.LoginDao

class LoginViewModel(private val loginDao: LoginDao) : ViewModel() {

    private val _checkingValue = MutableLiveData<Boolean>()
    val checkingValue: LiveData<Boolean> = _checkingValue

    /* Validation login data */
    fun checkUserFirstNameAndPassword(firstName: String, password: String): Int {
        return if (firstName.isEmpty()) Const.FIRST_NAME_IS_EMPTY
        else {
            if (password.isEmpty()) {
                Const.PASSWORD_IS_EMPTY
            } else {
                Const.VALID_DATA
            }

        }
    }

    suspend fun checkingIsUserAccountExist(firstName: String) =
        loginDao.isUserAccountExist(firstName)

}


class LoginViewModelFactory(
    private val loginDao: LoginDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}