package com.dz.ui.fragments.upload

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.dz.commons.activities.alonefragment.AloneFragmentActivity
import com.dz.commons.fragments.BaseMvpFragment
import com.dz.libraries.utilities.CollectionUtility
import com.dz.libraries.utilities.MediaUtility
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.edittexts.ExtEditText
import com.dz.libraries.views.textviews.ExtTextView
import com.dz.models.events.AddFriendEvent
import com.dz.models.events.HashTagEvent
import com.dz.models.responses.HashTagResponse
import com.dz.models.responses.MemberResponse
import com.dz.models.responses.SubmisstionResponse
import com.dz.models.responses.UploadResponse
import com.dz.ui.R
import com.dz.ui.fragments.friends.addfriends.AddFriendFragment
import com.dz.ui.fragments.friends.addfriends.adapters.AddFriendAdapter
import com.dz.ui.fragments.hashtags.HashTagFragment
import com.dz.ui.fragments.hashtags.adapters.HashTagAdapter
import com.dz.ui.fragments.upload.adapter.ListImageUploadAdapter
import com.dz.utilities.AppUtility
import com.dz.utilities.Constant
import com.dz.utilities.PicasoUtility
import com.trello.rxlifecycle3.android.FragmentEvent
import java.io.File

class UploadVideoFragment : BaseMvpFragment<IUploadVideoView, IUploadVideoPresenter>(), IUploadVideoView {
    companion object {
        fun newBundle(uploadResponse: UploadResponse?, contestId: Int): Bundle = bundleOf(Constant.VIDEO_PATH to uploadResponse, Constant.CONTEST_ID to contestId)
    }

    @BindView(com.dz.ui.R.id.ivThumbnail)
    lateinit var ivThumbnail: ImageView
    @BindView(R.id.ivPlayVideo)
    lateinit var ivPlayVideo: ImageView
    @BindView(R.id.ivChange)
    lateinit var ivChange: ImageView
    @BindView(com.dz.ui.R.id.rvImage)
    lateinit var rvImage: RecyclerView
    @BindView(com.dz.ui.R.id.rvHashTags)
    lateinit var rvHashTags: RecyclerView
    @BindView(com.dz.ui.R.id.rvFriends)
    lateinit var rvFriends: RecyclerView
    @BindView(R.id.btnSubmit)
    lateinit var btnSubmit: ExtTextView
    @BindView(R.id.etDescription)
    lateinit var etDescription: ExtEditText
    @BindView(R.id.tvTopImage)
    lateinit var tvTopImage: ExtTextView
    @BindView(R.id.tvNumberFriend)
    lateinit var tvNumberFriend: ExtTextView

    var uploadResponse: UploadResponse? = null
    var hastags: ArrayList<Int>? = null
    var friends: ArrayList<Int>? = null
    var filePath: File? = null
    var positionImage: Int = 0
    var contestId: Int? = 0
    var edtDescription: String? = ""

    // images
    val images = getListImage()
    var imageUploadAdapter: ListImageUploadAdapter? = null

    // hashtags
    var hashTagAdapter: HashTagAdapter? = null

    // friends
    var addFriendAdapter: AddFriendAdapter? = null

    override fun createPresenter(): IUploadVideoPresenter = UploadVideoPresenter(appComponent)

    override val resourceId: Int get() = com.dz.ui.R.layout.video_upload_fragment

    override val titleId: Int get() = com.dz.ui.R.string.upload_video

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindButterKnife(view)
        initViews()
        registerBus()
    }

    override fun initToolbar(supportActionBar: ActionBar) {
        super.initToolbar(supportActionBar)
        supportActionBar.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_close_black)
    }

    fun initViews() {
        uploadResponse = arguments?.getSerializable(Constant.VIDEO_PATH) as? UploadResponse?
        contestId = arguments?.getInt(Constant.CONTEST_ID)

        displayVideo()

        // images
        imageUploadAdapter = ListImageUploadAdapter(mActivity, getListImage(), { u ->
            StringUtility.with(u?.file)
                    .doIfEmpty {
                        positionImage = u?.position ?: 0
                        filePath = MediaUtility.getImageUrlPng()
                        AppUtility.actionImageCameraOrGallery(mCompositeDisposable, mActivity, this@UploadVideoFragment, filePath!!)
                    }
                    .doIfPresent { AppUtility.viewFile(this@UploadVideoFragment, it) }
        }, {
            it?.file = null
            rvImage.adapter?.notifyItemChanged(it?.position ?: 0)
        })
        rvImage.layoutManager = GridLayoutManager(mActivity, 2)
        rvImage.isNestedScrollingEnabled = false
        rvImage.adapter = imageUploadAdapter

        // hashtags
        hashTagAdapter = HashTagAdapter(mActivity, null) {
            hashTagAdapter?.removeItem(it)
        }
        rvHashTags.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvHashTags.adapter = hashTagAdapter

        // friends
        addFriendAdapter = AddFriendAdapter(mActivity, null, true, {
            // TODO: View profile
        }, {
            addFriendAdapter?.removeItem(it)
        })
        rvFriends.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFriends.adapter = addFriendAdapter

        // text number
        tvTopImage.text = getString(R.string.label_image).format(Constant.UPLOAD_NUMBER_IMAGE)
        tvNumberFriend.text = getString(R.string.label_friend).format(Constant.UPLOAD_NUMBER_FRIEND)
    }

    fun displayVideo() {
        if (uploadResponse == null || StringUtility.isNullOrEmpty(uploadResponse?.file)) {
            ivThumbnail.setImageDrawable(null)
            ivPlayVideo.visibility = View.GONE
            ivChange.visibility = View.GONE
        } else {
            ivPlayVideo.visibility = View.VISIBLE
            ivChange.visibility = View.VISIBLE
            PicasoUtility.get()
                    .load(PicasoUtility.getLink(uploadResponse?.thumbFile))
                    .placeholder(R.drawable.bg_background_radius_component)
                    .error(R.drawable.bg_background_radius_component)
                    .into(ivThumbnail)
        }
    }

    fun registerBus() {
        // hash tag
        registerRxBus(HashTagEvent::class.java, bindUntilEvent(FragmentEvent.DESTROY_VIEW)) {
            hashTagAdapter?.addItems(it.items)

            // distinct item
            val items = hashTagAdapter?.getItems()
            CollectionUtility.with(items).doIfPresent { its ->
                val results = its.distinctBy { d -> d?.name } as? ArrayList<HashTagResponse?>?
                hashTagAdapter?.setItems(results)
            }
        }

        // add friend
        registerRxBus(AddFriendEvent::class.java, bindUntilEvent(FragmentEvent.DESTROY_VIEW)) {
            addFriendAdapter?.addItems(it.items)

            // distinct item
            val items = addFriendAdapter?.getItems()
            CollectionUtility.with(items).doIfPresent { its ->
                val results = its.distinctBy { d -> d?.id } as? ArrayList<MemberResponse?>?
                addFriendAdapter?.setItems(results)
            }
        }
    }

    override fun onUploadSuccess(response: UploadResponse?) {
        if (MediaUtility.isImage(response?.file)) {
            imageUploadAdapter?.getItem(positionImage)?.file = response?.file
            imageUploadAdapter?.getItem(positionImage)?.path = response?.path
            imageUploadAdapter?.notifyItemChanged(positionImage)
        }

        if (MediaUtility.isVideo(response?.file)) {
            uploadResponse = response
            displayVideo()
        }
    }

    override fun onSubmisstion(response: SubmisstionResponse?) {
        showToastSuccess(getString(R.string.create_contest_success)) {
            mActivity.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppUtility.actionImageCameraOrGalleryOnActivityResult(mActivity, requestCode, resultCode, data, filePath, null) {
            presenter.uploadFile(it)
        }

        AppUtility.actionVideoCameraOrGalleryOnActivityResult(mActivity, requestCode, resultCode, data, filePath) {
            presenter.uploadFile(it)
        }
    }

    @OnClick(R.id.ivChange)
    fun onClickChange() {
        filePath = MediaUtility.getVideoUrlMp4()
        AppUtility.actionVideoCameraOrGallery(mCompositeDisposable, mActivity, this, filePath!!) {
            // TODO: action choose video from library app
        }
    }

    @OnClick(R.id.ivPlayVideo)
    fun onClickPlayVideo() {
        AppUtility.viewFile(this@UploadVideoFragment, uploadResponse?.file)
    }

    fun isValidUpload(): Boolean {
        edtDescription = etDescription.text.toString()
        if (StringUtility.isNullOrEmpty(edtDescription)) {
            showToastError(getString(R.string.error_field_must_not_be_empty))
            return false
        }

        if (imageUploadAdapter!!.getItems()!!.count { !StringUtility.isNullOrEmpty(it?.file) } != Constant.UPLOAD_NUMBER_IMAGE) {
            showToastError(getString(R.string.error_valid_add_image))
            return false
        }

//        if (hashTagAdapter!!.itemCount <= 0) {
//            showToastError(getString(R.string.error_field_must_not_be_empty))
//            return
//        }

        if (addFriendAdapter!!.itemCount < Constant.UPLOAD_NUMBER_FRIEND) {
            showToastError(getString(R.string.error_valid_add_friend))
            return false
        }

        return true
    }

    @OnClick(R.id.btnSubmit)
    fun onClickSubmit() {
        if (!isValidUpload()) return

        presenter.getSubmisstion(uploadResponse?.file, uploadResponse?.thumbPath, getListImageUpload(), edtDescription, contestId, getListHastagById(), getListFriendById())
    }

    @OnClick(com.dz.ui.R.id.tvHashTag)
    fun onClickHashTag() {
        AloneFragmentActivity.with(this)
                .start(HashTagFragment::class.java)
    }

    @OnClick(com.dz.ui.R.id.tvAddFriend)
    fun onClickAddFriend() {
        AloneFragmentActivity.with(this)
                .start(AddFriendFragment::class.java)
    }

    fun getListImage(): ArrayList<UploadResponse?> {
        val listImage = ArrayList<UploadResponse?>()

        for (i in 0 until Constant.UPLOAD_NUMBER_IMAGE) {
            listImage.add(UploadResponse(position = i))
        }

        return listImage
    }

    @Suppress("UNCHECKED_CAST")
    fun getListHastagById(): ArrayList<Int?>? {
        return hashTagAdapter?.getItems()?.map { it?.id } as? ArrayList<Int?>?
    }

    @Suppress("UNCHECKED_CAST")
    fun getListFriendById(): ArrayList<Int?>? {
        return addFriendAdapter?.getItems()?.map { it?.id }   as? ArrayList<Int?>?
    }

    @Suppress("UNCHECKED_CAST")
    fun getListImageUpload(): ArrayList<String?>? {
        return imageUploadAdapter?.getItems()?.map { it?.path } as? ArrayList<String?>?
    }
}