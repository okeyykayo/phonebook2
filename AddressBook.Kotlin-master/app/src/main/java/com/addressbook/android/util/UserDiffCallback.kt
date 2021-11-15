package com.addressbook.android.util

import androidx.recyclerview.widget.DiffUtil
import com.addressbook.android.roomDatabase.db.AddressBook

/**
 * Author :- Jay Sharma
 */
class UserDiffCallback(private val oldItems: List<AddressBook>, private val newItems: List<AddressBook>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    //Uncomment to notify change payload
    /*override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }*/

}
