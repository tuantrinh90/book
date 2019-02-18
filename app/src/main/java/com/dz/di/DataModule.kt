package com.dz.di

import com.dz.interactors.IDataModule
import com.dz.interactors.services.IApiService
import com.dz.interactors.services.IFileApiService
import com.dz.interactors.services.ILongApiService
import com.dz.interactors.databases.IDbModule
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import dagger.Module
import javax.inject.Inject

@Module
class DataModule(component: AppComponent) : IDataModule {
    @Inject
    lateinit var rxBus: RxBus<IEvent>

    @Inject
    override lateinit var apiService: IApiService

    @Inject
    override lateinit var longApiService: ILongApiService

    @Inject
    override lateinit var fileApiService: IFileApiService

    @Inject
    override lateinit var dbModule: IDbModule

    init {
        component.inject(this)
    }
}

