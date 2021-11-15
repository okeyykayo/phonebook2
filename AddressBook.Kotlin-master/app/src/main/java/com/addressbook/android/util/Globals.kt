package com.addressbook.android.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.addressbook.android.R
import com.addressbook.android.login.LoginActivity
import com.addressbook.android.model.UserLoginDetail
import com.facebook.login.LoginManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.dmoral.toasty.Toasty

/**
 * Created by Administrator on 3/14/18.
 */
class Globals : MultiDexApplication() {

    internal var sp: SharedPreferences? = null
    internal var editor: SharedPreferences.Editor? = null
    var TAG = "Globals"
    private var instance: Globals? = null

    var ACPDialog: ACProgressFlower? = null

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        instance = this

    }


    @Synchronized
    fun getInstance(): Globals? {
        return instance
    }


    fun hideKeyboard(activity: Activity) {
        try {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    private var toast: Toast? = null

    /**
     * @param context
     * @param message
     */
    fun showToast(context: Context?, message: String?) {
        if (message == null || message.isEmpty() || context == null)
            return

        //1st way to instantly update Toast message: with toasty library
        if (toast == null) {
            toast = Toasty.normal(context, message)
        }
        val v = toast!!.view
        if (v != null) {
            val tv = v.findViewById(R.id.toast_text) as TextView
            if (tv != null)
                tv.text = message
        }
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()

    }

    fun getSharedPref(): SharedPreferences? {

        sp = if (sp == null) getSharedPreferences(Constant.MM_secrets, Context.MODE_PRIVATE) else sp
        return sp

    }

    fun getEditor(): SharedPreferences.Editor? {
        editor = if (editor == null) getSharedPref()?.edit() else editor
        return editor
    }


    // storing model class in prefrence
    fun toJsonString(params: UserLoginDetail?): String? {
        if (params == null) {
            return null
        }
        val mapType = object : TypeToken<UserLoginDetail>() {

        }.type
        val gson = Gson()
        return gson.toJson(params, mapType)
    }

    fun toUserDetails(params: String?): UserLoginDetail? {
        if (params == null)
            return null

        val mapType = object : TypeToken<UserLoginDetail>() {

        }.type
        val gson = Gson()
        return gson.fromJson(params, mapType)
    }

    fun setUserDetails(userMap: UserLoginDetail?) {
        getEditor()?.putString(Constant.AB_USER_MAP, toJsonString(userMap))
        getEditor()?.commit()
    }

    fun getUserDetails(): UserLoginDetail? {
        return toUserDetails(getSharedPref()?.getString(Constant.AB_USER_MAP, null))
    }

    fun trimString(textView: AppCompatEditText): String {
        return textView.text.toString().trim { it <= ' ' }
    }

    fun logoutProcess(context: Context) {
        // logout from facebook
        if (LoginManager.getInstance() != null)
            LoginManager.getInstance()?.logOut()
        val i_logout = Intent(context, LoginActivity::class.java)

        // set the new task and clear flags
        i_logout.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(i_logout)
    }

    /**
     * @param listener           listener to get click event of dialog
     * @param title              title of dialog
     * @param desc               description of dialog
     * @param positiveButtonText text of positive button
     * @param negativeButtonText text of negative
     * @param isCancelable       set true if you want cancelable dialog
     */
    fun showDialog(context: Context, listener: OnDialogClickListener,
                   title: String, desc: String?, positiveButtonText: String?,
                   negativeButtonText: String?,
                   isCancelable: Boolean, position: Int) {
        if (desc == null || desc.isEmpty())
            return
        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(isCancelable)
        dialog.setTitle(title).setMessage(desc)

        if (positiveButtonText != null && !positiveButtonText.isEmpty())
            dialog.setPositiveButton(positiveButtonText) { dialog, which ->
                dialog.cancel()
                listener.OnDialogPositiveClick(position)
            }
        else
            dialog.setPositiveButton("", null)
        if (negativeButtonText != null && !negativeButtonText.isEmpty())
            dialog.setNegativeButton(negativeButtonText) { dialog, which ->
                dialog.cancel()
                listener.OnDialogNegativeClick()
            }
        else {
            dialog.setNegativeButton("", null)
        }

        dialog.create().show()
    }

    interface OnDialogClickListener {
        fun OnDialogPositiveClick(position: Int)

        fun OnDialogNegativeClick()
    }


    fun showProgressDialog(context: Context) {
        ACPDialog = ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build()
        ACPDialog?.setCanceledOnTouchOutside(false)
        ACPDialog?.show()
    }

    fun dismissDialog() {
        if (ACPDialog != null && ACPDialog!!.isShowing) {
            ACPDialog?.dismiss()
        }
    }
}