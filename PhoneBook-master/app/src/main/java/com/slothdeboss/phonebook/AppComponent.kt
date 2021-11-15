package com.slothdeboss.phonebook

import android.app.Application
import com.slothdeboss.data.di.dataModule
import com.slothdeboss.domain.di.useCasesModule
import com.slothdeboss.local.di.localModule
import com.slothdeboss.phonebook.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppComponent: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppComponent)
            modules(
                useCasesModule,
                localModule,
                dataModule,
                viewModelModule
            )
        }
    }

}