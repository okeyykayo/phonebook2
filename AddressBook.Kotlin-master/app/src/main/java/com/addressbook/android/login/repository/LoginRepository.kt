package com.addressbook.android.login.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.addressbook.android.login.LoginActivity
import com.addressbook.android.main.respository.AddressBookRepository
import com.addressbook.android.model.UserLoginDetail
import com.addressbook.android.util.API
import com.addressbook.android.util.Globals
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginRepository() {

    private lateinit var callbackmanager: CallbackManager
    val loginResults = MutableLiveData<LoginResult>()
    val responseData = MutableLiveData<UserLoginDetail>()

    suspend fun FBLogin(loginActivity: LoginActivity): LiveData<LoginResult> {

            callbackmanager = CallbackManager.Factory.create()
            // Set permissions
            LoginManager.getInstance().logInWithReadPermissions(loginActivity, listOf("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackmanager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(loginResult: LoginResult) {
                    loginResults.value = loginResult
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                }
            })

        return loginResults
    }

    suspend fun doRequestForLoginUser(activity: LoginActivity, email: String, pwd: String):MutableLiveData<UserLoginDetail> {

        val body = API.LoginBody(email,pwd)
        val globals = activity.applicationContext as Globals

        API.getInstance().login(body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError({ e -> globals.showToast(activity, e.message) })
                .subscribe({ response ->

                    responseData.value = response

                }, { error ->

                })

        return responseData
    }


}