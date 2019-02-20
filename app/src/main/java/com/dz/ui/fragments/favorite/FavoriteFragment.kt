package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import io.reactivex.disposables.CompositeDisposable

class FavoriteFragment : BaseMainFragment<IFavoriteView, IFavoritePresenter>(), IFavoriteView {
    enum class Type {
        OPEN, CLOSE
    }

    override fun createPresenter(): IFavoritePresenter = FavoritePresenter(appComponent)

    override val resourceId: Int get() = R.layout.history_fragment

    override val titleId: Int get() = R.string.home

    var mType = FavoriteFragment.Type.OPEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }

    fun setCompositeDisposable(compositeDisposable: CompositeDisposable): FavoriteFragment {
        return this
    }

    fun setType(type: FavoriteFragment.Type): FavoriteFragment {
        mType = type
        return this
    }

}