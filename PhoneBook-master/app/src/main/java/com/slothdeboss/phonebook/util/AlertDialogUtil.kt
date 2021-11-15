package com.slothdeboss.phonebook.util

import android.app.AlertDialog
import android.content.Context
import com.slothdeboss.phonebook.event.ContactEvent

fun buildDialog(
    context: Context,
    title: String,
    positiveAction: (event: ContactEvent) -> Unit,
    event: ContactEvent
): AlertDialog {
    return AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage("Are you sure?")
        .setPositiveButton("Yes") { _, _ ->
            positiveAction(event)
        }
        .setNegativeButton("No") { _, _ ->
            return@setNegativeButton
        }
        .create()
}