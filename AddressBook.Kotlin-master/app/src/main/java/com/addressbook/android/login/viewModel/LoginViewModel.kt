package com.addressbook.android.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addressbook.android.login.LoginActivity
import com.addressbook.android.login.repository.LoginRepository
import com.addressbook.android.model.UserLoginDetail
import com.facebook.login.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val repository = LoginRepository()
    val loginResults = MutableLiveData<LoginResult>()
    val responseData = MutableLiveData<UserLoginDetail>()

    fun FBLogin(loginActivity: LoginActivity): LiveData<LoginResult>{
        viewModelScope.launch {
            repository.FBLogin(loginActivity).observe(loginActivity, Observer {
                loginResults.value = it
            })
        }

        return loginResults
    }


    fun LoginUser(loginActivity: LoginActivity, email: String, pwd: String): LiveData<UserLoginDetail>{
        viewModelScope.launch {
            repository.doRequestForLoginUser(loginActivity,email,pwd).observe(loginActivity, Observer {
                responseData.value = it
            })
        }

        return responseData
    }

}