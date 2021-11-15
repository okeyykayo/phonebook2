package com.slothdeboss.phonebook.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.slothdeboss.domain.entity.Contact
import com.slothdeboss.domain.state.*
import com.slothdeboss.phonebook.R
import com.slothdeboss.phonebook.base.BaseFragment
import com.slothdeboss.phonebook.event.DeleteContact
import com.slothdeboss.phonebook.event.LoadContactById
import com.slothdeboss.phonebook.util.buildDialog
import com.slothdeboss.phonebook.util.loadImageFromUrl
import kotlinx.android.synthetic.main.fragment_contact_details.*

class ContactDetailsFragment : BaseFragment(R.layout.fragment_contact_details) {

    private var contactId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let{
            contactId = ContactDetailsFragmentArgs.fromBundle(it).contactId
            viewModel.renderEvent(event = LoadContactById(id = contactId))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contact_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.contact_delete_option -> onDeleteOptionSelected()
            R.id.contact_edit_option -> onEditOptionSelected()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun observeState() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state->
            when (state) {
                OnLoadingState -> onLoadingState()
                OnActionSuccessState -> onActionsSuccessState()
                is OnContactLoadedState -> onContactLoadedState(contact = state.contact)
                is OnErrorState -> onErrorState(message = state.message)
            }
        })
    }

    private fun onActionsSuccessState() {
        Navigation.findNavController(contactContainer).navigate(
            ContactDetailsFragmentDirections.backToContacts()
        )
    }

    private fun onContactLoadedState(contact: Contact) {
        loadContactInFragment(contact = contact)
        detailLoadingBar.visibility = View.GONE
        contactContainer.visibility = View.VISIBLE
    }

    private fun onLoadingState() {
        contactContainer.visibility = View.GONE
        detailLoadingBar.visibility = View.VISIBLE
    }

    private fun onErrorState(message: String) {
        displayMessage(message = message)
    }

    private fun onEditOptionSelected() {
        Navigation.findNavController(contactContainer).navigate(
            ContactDetailsFragmentDirections.toUpdateContact().setContactId(contactId)
        )
    }

    private fun onDeleteOptionSelected() {
        val dialog = buildDialog(
            requireContext(),
            "Delete contact",
            viewModel::renderEvent,
            DeleteContact(contactId = contactId)
        )
        dialog.show()
    }

    private fun loadContactInFragment(contact: Contact) {
        contactImage.loadImageFromUrl(imageUrl = contact.imageUrl)
        contactName.text = contact.name
        contactSurname.text = contact.surname
        contactEmail.text = contact.email ?: "Unknown"
    }
}