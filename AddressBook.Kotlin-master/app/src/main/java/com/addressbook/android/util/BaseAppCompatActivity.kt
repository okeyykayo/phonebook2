package com.addressbook.android.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseAppCompatActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}