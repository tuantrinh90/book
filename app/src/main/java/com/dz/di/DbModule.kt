package com.dz.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dz.interactors.databases.IDbModule
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.dz.models.database.Book
import com.dz.models.database.LinkBook
import com.dz.models.database.PageBook
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
