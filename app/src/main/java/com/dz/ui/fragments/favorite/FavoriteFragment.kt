package com.dz.ui.fragments.history

import android.os.Bundle
import android.view.View
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import io.reactivex.disposables.CompositeDisposable

class FavoriteFragment : BaseMainFragment<IDetailView, IDetailPresenter>(), IDetailView {
    enum class Type {
        OPEN, CLOSE
    }

    override fun createPresenter(): IDetailPresenter = DetailPresenter(appComponent)

    override val resourceId: Int get() = R.layout.favorite_fragment

    override val titleId: Int get() = R.string.home


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }

}