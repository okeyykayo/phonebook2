package com.slothdeboss.phonebook.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.slothdeboss.domain.entity.Contact
import com.slothdeboss.phonebook.R
import com.slothdeboss.phonebook.ui.OnContactClicked
import com.slothdeboss.phonebook.util.loadImageFromUrl
import kotlinx.android.synthetic.main.contact_card.view.*

class ContactListAdapter(
    private val contactsOwner: OnContactClicked
): RecyclerView.Adapter<ContactListViewHolder>() {

    private val contacts = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_card, parent, false
        )
        return ContactListViewHolder(view = view)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact = contact, contactsOwner = contactsOwner)
    }

    fun refreshContacts(newContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(newContacts)
        notifyDataSetChanged()
    }
}

class ContactListViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bind(contact: Contact, contactsOwner: OnContactClicked) {
        view.cardContactName.text = contact.name
        view.cardContactImage.loadImageFromUrl(imageUrl = contact.imageUrl)
        view.setOnClickListener {
            contactsOwner.onClick(id = contact.id)
        }
    }

}