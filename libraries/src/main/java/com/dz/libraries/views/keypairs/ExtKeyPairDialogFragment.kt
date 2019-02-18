package com.dz.libraries.views.keypairs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dz.libraries.R
import com.dz.libraries.fragments.ExtBaseBottomDialogFragment
import com.dz.libraries.utilities.KeyboardUtility
import com.dz.libraries.utilities.StringUtility
import com.dz.libraries.views.edittexts.ExtEditText
import com.dz.libraries.views.textviews.ExtTextView
import java.util.*

class ExtKeyPairDialogFragment : ExtBaseBottomDialogFragment() {
    companion object {
        private const val TAG = "ExtKeyPairDialogFragment"

        fun newInstance(): ExtKeyPairDialogFragment = ExtKeyPairDialogFragment()
    }

    private var title: String? = null
    private var value: String? = null
    private var isVisibleFilter: Boolean = false
    private var itemConsumer: ((value: ExtKeyPair) -> Unit)? = null
    private var extKeyPairs: ArrayList<ExtKeyPair?>? = null
    private var extKeyPairsOrigins: ArrayList<ExtKeyPair?>? = null
    private var extKeyPairAdapter: ExtKeyPairAdapter? = null

    private lateinit var tvCancel: ExtTextView
    private lateinit var tvTitle: ExtTextView
    private lateinit var edtSearch: ExtEditText
    private lateinit var rvKeyValuePair: RecyclerView

    private var index = 0
    private var positionSelected = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.key_value_pair_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // cancel
        tvCancel = view.findViewById(R.id.tvCancel)
        tvCancel.setOnClickListener { onClickCancel() }

        // title
        tvTitle = view.findViewById(R.id.tvTitle)
        tvTitle.text = if (StringUtility.isNullOrEmpty(title)) getString(R.string.select_value) else title

        // filter
        edtSearch = view.findViewById(R.id.edtSearch)
        edtSearch.visibility = if (isVisibleFilter) View.VISIBLE else View.GONE
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = filterData()
        })

        // recycler view
        rvKeyValuePair = view.findViewById(R.id.rvKeyValuePair)

        // active key is selected
        index = 0
        extKeyPairsOrigins?.forEach {
            it!!.selected = value != null && value!!.equals(it.key, true)
            if (it.selected) positionSelected = index
            index++
        }

        // don't show keyboard
        dialog.setOnShowListener { KeyboardUtility.hideSoftKeyboard(activity!!, edtSearch) }

        // load data
        loadData()
    }

    private fun onClickCancel() {
//        itemConsumer?.invoke(ExtKeyPair("", ""))
        dismiss()
    }

    private fun filterData() {
        extKeyPairsOrigins?.let { extKeyPairs = ArrayList(it) }

        // filter
        val query = edtSearch.text.toString().toLowerCase()


        if (!StringUtility.isNullOrEmpty(query)) {
            extKeyPairs = extKeyPairs?.filter {
                it?.key!!.toLowerCase().contains(query, true) || it.value.toLowerCase().equals(query, true)
            } as? ArrayList<ExtKeyPair?>?
        }

        // notification data
        extKeyPairAdapter?.clear()
        extKeyPairAdapter?.setItems(extKeyPairs)
    }

    private fun loadData() {
        extKeyPairsOrigins?.let { extKeyPairs = ArrayList(it) }
        extKeyPairAdapter = ExtKeyPairAdapter(activity!!, extKeyPairs) {
            itemConsumer?.invoke(it)
            dismiss()
        }

        rvKeyValuePair.layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        rvKeyValuePair.adapter = extKeyPairAdapter
        rvKeyValuePair.scrollToPosition(positionSelected)
    }

    fun setTitle(title: String?): ExtKeyPairDialogFragment {
        this.title = title
        return this
    }

    fun setValue(value: String?): ExtKeyPairDialogFragment {
        this.value = value
        return this
    }

    fun setExtKeyValuePairs(extKeyPairs: ArrayList<ExtKeyPair?>?): ExtKeyPairDialogFragment {
        this.extKeyPairsOrigins = extKeyPairs
        return this
    }

    fun setVisibleFilter(isVisibleFilter: Boolean): ExtKeyPairDialogFragment {
        this.isVisibleFilter = isVisibleFilter
        return this
    }

    fun setOnSelectedConsumer(itemConsumer: ((value: ExtKeyPair) -> Unit)? = null): ExtKeyPairDialogFragment {
        this.itemConsumer = itemConsumer
        return this
    }
}