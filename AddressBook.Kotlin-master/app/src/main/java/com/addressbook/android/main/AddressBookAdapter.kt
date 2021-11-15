package com.addressbook.android.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.addressbook.android.R
import com.addressbook.android.databinding.AdapterAddressBinding
import com.addressbook.android.roomDatabase.db.AddressBook
import com.addressbook.android.util.UserDiffCallback

/**
 * Author :- Jay Sharma
 */
class AddressBookAdapter(private val onAddressViewActionListener: OnAddressViewActionListener? = null) :
    RecyclerView.Adapter<AddressBookAdapter.UserHolder>() {

    private val addresses: MutableList<AddressBook> = ArrayList()

    fun setData(addresses: List<AddressBook>) {
        val result = DiffUtil.calculateDiff(UserDiffCallback(this.addresses, addresses))
        this.addresses.clear()
        this.addresses.addAll(addresses)
        result.dispatchUpdatesTo(this)
        //We don't need this as we are playing with Diff Util which is great thing to play around
        //notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding =
            AdapterAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(addresses[position])
    }

    override fun getItemCount() = addresses.size

    inner class UserHolder(private val binding: AdapterAddressBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            //Set your click listener here
            binding.btDelete.setOnClickListener(this)
            binding.btEdit.setOnClickListener(this)
        }

        fun bind(addressBook: AddressBook) {
            itemView.apply {
                binding.tvName.text = addressBook.name
                binding.tvEmail.text = addressBook.email
                binding.tvContactNumber.text = addressBook.contact_number
            }
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.bt_delete->onAddressViewActionListener?.onAddressDeleted(addresses[adapterPosition])
                R.id.bt_edit->onAddressViewActionListener?.onAddressEdited(addresses[adapterPosition]);

            }

        }

    }

    interface OnAddressViewActionListener {
        fun onAddressDeleted(addressBook: AddressBook)
        fun onAddressEdited(addressBook: AddressBook)
    }

}