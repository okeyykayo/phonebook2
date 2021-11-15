package com.slothdeboss.phonebook.base

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.slothdeboss.phonebook.ui.AppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment(layout: Int): Fragment(layout) {

    protected val viewModel: AppViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState()
    }

    protected abstract fun observeState()

    protected fun displayMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}