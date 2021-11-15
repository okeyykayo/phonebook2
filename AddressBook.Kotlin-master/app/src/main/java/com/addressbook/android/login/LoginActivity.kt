package com.addressbook.android.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.addressbook.android.R
import com.addressbook.android.databinding.ActivityLoginBinding
import com.addressbook.android.login.viewModel.LoginViewModel
import com.addressbook.android.main.AddressBookListingActivity
import com.addressbook.android.util.*
import com.facebook.CallbackManager


class LoginActivity : BaseAppCompatActivity() {

    var globals: Globals? = null

    //Facebook
    var callbackmanager: CallbackManager? = null
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        globals = applicationContext as Globals
        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbarTitle
                .text = getString(R.string.title_login)

        binding.btnLogin.setOnClickListener(clickListener)
        binding.btnLoginFb.setOnClickListener(clickListener)


        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]


    }

    val clickListener = View.OnClickListener { view ->

        when (view.id) {

            R.id.btn_login -> {
                globals?.hideKeyboard(getContextActivity())
                if (isValid()) {
                    if (ConnectionDetector.internetCheck(getContext(), true))
                        doRequestForLogin()
                }
            }
            R.id.btn_login_fb -> {
                globals?.hideKeyboard(getContextActivity())
                if (ConnectionDetector.internetCheck(getContext(), true))
                    FBLogin()
            }
        }
    }

    private fun doRequestForLogin() {

        globals?.showProgressDialog(this)

        viewModel.LoginUser(this,binding.etEmail.text.toString().trim(),binding.etPassword.text.toString().trim())
                .observe(this, Observer {
                    globals?.dismissDialog()
                    globals?.setUserDetails(it)
                    intentAddressBookListing()
                })
    }

    fun FBLogin() {
       viewModel.FBLogin(this).observe(this, androidx.lifecycle.Observer {
           if(it.accessToken!=null) {
               intentAddressBookListing()
           }
       })
    }

    private fun intentAddressBookListing() {
        val intent = Intent(this@LoginActivity, AddressBookListingActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun isValid(): Boolean {
        if (UtilsValidation.validateEmptyEditText(binding.etEmail)) {
            globals?.showToast(this@LoginActivity, getString(R.string.toast_err_email))
            requestFocus(binding.etEmail)
            return false
        }
        if (UtilsValidation.validateEmail(binding.etEmail)) {
            globals?.showToast(this@LoginActivity, getString(R.string.toast_err_enter_valid_email))
            requestFocus(binding.etEmail)
            return false
        }
        if (UtilsValidation.validateEmptyEditText(binding.etPassword)) {
            globals?.showToast(this@LoginActivity, getString(R.string.toast_err_password))
            requestFocus(binding.etPassword)
            return false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun getContext(): Context {
        return this@LoginActivity
    }

    private fun getContextActivity(): Activity {
        return this@LoginActivity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackmanager?.onActivityResult(requestCode, resultCode, data)
    }
}
