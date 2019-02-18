package com.dz.di

import com.dz.applications.AppContext
import com.dz.commons.activities.BaseAppCompatActivity
import com.dz.commons.fragments.BaseMvpDialogFragment
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.commons.presenters.BaseMvpPresenter
import com.dz.commons.presenters.IBaseActivityMvpPresenter
import com.dz.commons.presenters.IBaseFragmentMvpPresenter
import com.dz.commons.views.IBaseActivityMvpView
import com.dz.commons.views.IBaseFragmentMvpView
import com.dz.commons.views.IBaseMvpView
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, DbModule::class, DataModule::class, AppModule::class])
interface AppComponent {
    fun inject(dataModule: DataModule)

    fun inject(dbModule: DbModule)

    fun inject(appContext: AppContext)

    fun inject(activity: BaseAppCompatActivity<IBaseActivityMvpView, IBaseActivityMvpPresenter<IBaseActivityMvpView>>)

    fun inject(presenter: BaseMvpPresenter<IBaseMvpView>)

    fun inject(fragment: BaseMvpFragment<IBaseFragmentMvpView, IBaseFragmentMvpPresenter<IBaseFragmentMvpView>>)

    fun inject(fragment: BaseMvpDialogFragment<IBaseFragmentMvpView, IBaseFragmentMvpPresenter<IBaseFragmentMvpView>>)
}
