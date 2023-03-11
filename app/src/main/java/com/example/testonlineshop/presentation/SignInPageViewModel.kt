package com.example.testonlineshop.presentation

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.*
import com.example.testonlineshop.Const.EMAIL_IS_EMPTY
import com.example.testonlineshop.Const.FIRST_NAME_IS_EMPTY
import com.example.testonlineshop.Const.INVALID_EMAIL
import com.example.testonlineshop.Const.LAST_NAME_IS_EMPTY
import com.example.testonlineshop.Const.VALID_DATA
import com.example.testonlineshop.data.LoginDao
import com.example.testonlineshop.data.LoginDataDb
import com.example.testonlineshop.data.UserLoginData
import kotlinx.coroutines.launch

class SignInPageViewModel(private val loginDao: LoginDao) : ViewModel() {

    private var _checkingValue = MutableLiveData<Boolean>()
    val checkingValue: LiveData<Boolean> = _checkingValue

    /* Validation sign in data */
    fun checkUserLoginData(firstName: String, lastName: String, email: String): Int {
        if (firstName.isEmpty()) return FIRST_NAME_IS_EMPTY
        else {
            if (lastName.isEmpty()) return LAST_NAME_IS_EMPTY
            else {
                if (email.isEmpty()) return EMAIL_IS_EMPTY
                else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) return VALID_DATA
                    else return INVALID_EMAIL
                }
            }
        }
    }

    suspend fun insertUserLoginDataToDb(userLoginData: UserLoginData) =
        loginDao.insert(userLoginData)

    suspend fun checkingIsUserExist(email: String) {
        _checkingValue.value = loginDao.isUserExist(email)}
}

class SignInPageViewModelFactory(
    private val loginDao: LoginDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignInPageViewModel(loginDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}