package com.slothdeboss.phonebook.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.slothdeboss.domain.entity.Contact
import com.slothdeboss.domain.state.*
import com.slothdeboss.phonebook.R
import com.slothdeboss.phonebook.base.BaseFragment
import com.slothdeboss.phonebook.event.LoadContactById
import com.slothdeboss.phonebook.event.UpdateContact
import com.slothdeboss.phonebook.util.buildDialog
import com.slothdeboss.phonebook.util.loadImageFromUrl
import kotlinx.android.synthetic.main.fragment_update_contact.*

class UpdateContactFragment : BaseFragment(R.layout.fragment_update_contact) {

    private lateinit var changeableContact: Contact

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val id = UpdateContactFragmentArgs.fromBundle(it).contactId
            viewModel.renderEvent(event = LoadContactById(id = id))
        }
        setupUpdateButton()
    }

    override fun observeState() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                OnActionSuccessState -> onActionSuccessState()
                is OnContactLoadedState -> onContactLoadedState(contact = state.contact)
                is OnErrorState -> onErrorState(message = state.message)
            }
        })
    }

    private fun onErrorState(message: String) {
        displayMessage(message = message)
    }

    private fun onActionSuccessState() {
        Navigation.findNavController(btnUpdateContact).navigate(
            UpdateContactFragmentDirections.backToContacts()
        )
    }

    private fun onContactLoadedState(contact: Contact) {
        changeableContact = contact
        fillContactFields()
    }

    private fun setupUpdateButton() {
        btnUpdateContact.setOnClickListener {
            val contact = getUpdatedContact()
            val dialog = buildDialog(
                requireContext(),
                "Contact update",
                viewModel::renderEvent,
                UpdateContact(contact = contact)
            )
            dialog.show()
        }
    }

    private fun fillContactFields() {
        contactImage.loadImageFromUrl(imageUrl = changeableContact.imageUrl)
        editContactName.setText(changeableContact.name, TextView.BufferType.EDITABLE)
        editContactSurname.setText(changeableContact.surname, TextView.BufferType.EDITABLE)
        editContactEmail.setText(changeableContact.email, TextView.BufferType.EDITABLE)
    }

    private fun getUpdatedContact(): Contact {
        val name = editContactName.text.toString()
        val surname = editContactSurname.text.toString()
        val email = editContactEmail.text.toString()
        return Contact(
            id = changeableContact.id,
            imageUrl = changeableContact.imageUrl,
            name = name,
            surname = surname,
            email = email
        )
    }
}