package com.addressbook.android.main.respository

import androidx.lifecycle.LiveData
import com.addressbook.android.roomDatabase.db.AddressBook
import com.addressbook.android.roomDatabase.db.AddressBookDatabase

class AddressBookRepository(private val addressBookDatabase: AddressBookDatabase) {

    private var list :LiveData<List<AddressBook>> = addressBookDatabase
            .getAddressBookDao().getAllAddressBook()

    suspend fun insertAddressBook(addressBook: AddressBook) = addressBookDatabase.getAddressBookDao().insertAddressBook(addressBook)

    suspend fun updateAddressBook(addressBook: AddressBook) = addressBookDatabase.getAddressBookDao().updateAddressBook(addressBook)

    suspend fun deleteAddressBook(addressBook: AddressBook) = addressBookDatabase.getAddressBookDao().deleteAddressBook(addressBook)

    suspend fun clearAddress() = addressBookDatabase.getAddressBookDao().clearAddress()

    suspend fun deleteAddressBookbyId(id:Int) = addressBookDatabase.getAddressBookDao().deleteAddressBook(id.toLong())

    fun getAllAddress() : LiveData<List<AddressBook>> = list
}