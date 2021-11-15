package com.addressbook.android.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.addressbook.android.R
import com.addressbook.android.main.AddressBookListingActivity
import com.addressbook.android.util.BaseAppCompatActivity
import com.addressbook.android.util.Constant
import com.addressbook.android.util.Globals

class SplashActivity : BaseAppCompatActivity() {

    internal var handler: Handler? = null
    internal var globals: Globals? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        globals = applicationContext as Globals
    }

    override fun onResume() {
        performOperation()
        super.onResume()

    }

    fun performOperation() {

        startHandler()
    }

    private fun startHandler() {
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(runnable, Constant.AB_SPLASH_TIME)//call runnable
    }

    internal var runnable: Runnable = Runnable { openNavigationActivity() }


    fun openNavigationActivity() {

        if (globals?.getUserDetails() == null) {
            // intent to login screen
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
        } else {
            // intent to home screen
            val intent = Intent(this@SplashActivity, AddressBookListingActivity::class.java)
            startActivity(intent)
        }
        finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        if (handler != null && runnable != null) {
            handler?.removeCallbacks(runnable) // remove handler callbacks
        }
    }
}
