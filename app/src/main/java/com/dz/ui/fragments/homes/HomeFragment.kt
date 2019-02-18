package com.dz.ui.fragments.homes

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.OnClick
import com.dz.libraries.utilities.MediaUtility
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.ui.R
import com.dz.ui.fragments.BaseMainFragment
import com.dz.utilities.AppUtility
import com.dz.utilities.FragmentUtility
import io.reactivex.disposables.CompositeDisposable
import java.io.File

class HomeFragment : BaseMainFragment<IHomeView, IHomePresenter>(), IHomeView {
    override fun createPresenter(): IHomePresenter = HomePresenter(appComponent)

    override val resourceId: Int get() = R.layout.home_fragment

    override val titleId: Int get() = R.string.home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
    }

    fun getMediaDuration(uriOfFile: Uri): Int {
        val mp = MediaPlayer.create(appContext, uriOfFile)
        return mp.getDuration()
    }
}