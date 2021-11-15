package com.addressbook.android.main.respository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AddressViewFactory(val repository: AddressBookRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(AddressBookRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: Exception) {
            Log.v("tag",e.message.toString())
        }
        return super.create(modelClass)
    }
}