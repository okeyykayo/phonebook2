package com.addressbook.android.util

import com.addressbook.android.BuildConfig
import com.addressbook.android.model.UserLoginDetail
import com.ihsanbal.logging.LoggingInterceptor
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.*

/**
 * Created by Administrator on 3/15/18.
 */
interface API {


    @POST("post")
    fun login(@Body body: LoginBody): Observable<UserLoginDetail>

    data class LoginBody(var Email: String, var Password: String)


    /** * Companion object to create the API */
    companion object {
        val base_url = "https://postman-echo.com/"

        // create an instance of OkLogInterceptor using a builder()
        val httpClient = OkHttpClient.Builder()

        private var api: API? = null
        fun getInstance(): API {
            httpClient.addInterceptor(LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .request("Request")
                    .response("Response")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .build());

            if (api == null) {
                val restAdapter = Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(base_url)
                        .client(httpClient.build())
                        .build()

                api = restAdapter.create(API::class.java)
            }
            return api!!
        }
    }
}