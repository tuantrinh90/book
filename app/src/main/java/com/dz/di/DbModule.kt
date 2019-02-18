package com.dz.di

import com.dz.interactors.databases.IDbModule
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import dagger.Module
import javax.inject.Inject

@Module
class DbModule(component: AppComponent) : IDbModule {
    @Inject
    lateinit var rxBus: RxBus<IEvent>

    init {
        component.inject(this)
    }
}
