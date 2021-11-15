package com.slothdeboss.phonebook.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.slothdeboss.domain.entity.Contact
import com.slothdeboss.domain.state.*
import com.slothdeboss.phonebook.R
import com.slothdeboss.phonebook.base.BaseFragment
import com.slothdeboss.phonebook.event.LoadAllContacts
import com.slothdeboss.phonebook.event.RefreshContactList
import com.slothdeboss.phonebook.ui.OnContactClicked
import com.slothdeboss.phonebook.ui.main.adapter.ContactListAdapter
import com.slothdeboss.phonebook.util.buildDialog
import kotlinx.android.synthetic.main.fragment_contact_list.*

class ContactListFragment : BaseFragment(R.layout.fragment_contact_list), OnContactClicked {

    private lateinit var contactRecyclerAdapter: ContactListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        contactRecyclerAdapter = ContactListAdapter(contactsOwner = this)
        viewModel.renderEvent(event = LoadAllContacts)
        setupRecycler()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contact_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh_contacts_list -> refreshDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun observeState() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                OnLoadingState -> onLoadingState()
                OnActionSuccessState -> onActionSuccessState()
                is OnContactListLoadedState -> onContactsLoadedState(contacts = state.contacts)
                is OnErrorState -> onErrorState(message = state.message)
            }
        })
    }

    override fun onClick(id: Long) {
        Navigation.findNavController(contactsList).navigate(
            ContactListFragmentDirections.toContactDetail().setContactId(id)
        )
    }

    private fun onContactsLoadedState(contacts: List<Contact>) {
        contactRecyclerAdapter.refreshContacts(newContacts = contacts)
        contactLoadingBar.visibility = View.GONE
        contactsList.visibility = View.VISIBLE
    }

    private fun onLoadingState() {
        contactsList.visibility = View.GONE
        contactLoadingBar.visibility = View.VISIBLE
    }

    private fun onErrorState(message: String) {
        contactLoadingBar.visibility = View.GONE
        displayMessage(message = message)
    }

    private fun onActionSuccessState() {
        viewModel.renderEvent(event = LoadAllContacts)
    }

    private fun refreshDatabase() {
        val dialog = buildDialog(
            requireContext(),
            "Refresh contacts",
            viewModel::renderEvent,
            RefreshContactList
        )
        dialog.show()
    }

    private fun setupRecycler() {
        contactsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactRecyclerAdapter
        }
    }
}
