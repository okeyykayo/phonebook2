package com.slothdeboss.phonebook.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slothdeboss.domain.state.ContactsState
import com.slothdeboss.phonebook.event.ContactEvent

abstract class BaseViewModel: ViewModel() {

    protected val _state: MutableLiveData<ContactsState> = MutableLiveData()
    val state: LiveData<ContactsState>
        get() = _state

    abstract fun renderEvent(event: ContactEvent)

}