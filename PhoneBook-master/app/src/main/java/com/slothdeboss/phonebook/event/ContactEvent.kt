package com.slothdeboss.phonebook.event

import com.slothdeboss.domain.entity.Contact

sealed class ContactEvent

object LoadAllContacts: ContactEvent()
object RefreshContactList: ContactEvent()
data class LoadContactById(val id: Long): ContactEvent()
data class UpdateContact(val contact: Contact): ContactEvent()
data class DeleteContact(val contactId: Long): ContactEvent()