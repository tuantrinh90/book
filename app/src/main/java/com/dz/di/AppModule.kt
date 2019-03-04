package com.dz.di

import android.content.Context
import androidx.room.Room
import com.dz.applications.AppContext
import com.dz.interactors.IDataModule
import com.dz.interactors.databases.IDbModule
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val appContext: AppContext) {
    @Singleton
    @Provides
    fun provideEvenBus(): RxBus<IEvent> = RxBus()

    @Singleton
    @Provides
    fun provideAppContext(): AppContext = appContext

    @Singleton
    @Provides
    fun provideDataModule(): IDataModule = DataModule(appContext.appComponent)

    @Singleton
    @Provides
    fun provideDbModule(): IDbModule = DbModule(appContext.appComponent)

    @Singleton
    @Provides
    fun provideApiModule(): ApiModule = ApiModule()

}
