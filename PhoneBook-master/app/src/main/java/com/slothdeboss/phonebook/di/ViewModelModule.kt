package com.slothdeboss.phonebook.di

import com.slothdeboss.phonebook.ui.AppViewModel
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<AppViewModel>()
}