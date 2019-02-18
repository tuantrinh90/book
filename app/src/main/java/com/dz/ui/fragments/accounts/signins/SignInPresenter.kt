package com.dz.ui.fragments.accounts.signins

import com.dz.applications.AppContext
import com.dz.commons.presenters.BaseFragmentMvpPresenter
import com.dz.di.AppComponent
import com.dz.listeners.ApiCallback
import com.dz.models.responses.SignInResponse
import com.dz.utilities.RxUtility
import com.dz.utilities.ServiceConfig

class SignInPresenter(appComponent: AppComponent) : BaseFragmentMvpPresenter<ISignInView>(appComponent), ISignInPresenter {
    override fun login(userName: String, password: String) {
        getView {
            RxUtility.async(it, dataModule.apiService.signIn(userName, password, ServiceConfig.GRANT_TYPE, ServiceConfig.CLIENT_ID),
                    object : ApiCallback<SignInResponse>() {
                        override fun onStart() {
                            it.showLoading(true)
                        }

                        override fun onFinish() {
                            it.showLoading(false)
                        }

                        override fun onSuccess(t: SignInResponse?) {
                            AppContext.setData(t)
                            it.onSuccess()
                        }

                        override fun onError(error: String?) {
                            it.showToastError(error)
                        }
                    })
        }
    }
}
