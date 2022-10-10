package com.canerture.firebaseexamples.presentation.todos

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.canerture.firebaseexamples.common.Constants.PRIORITY_HIGH
import com.canerture.firebaseexamples.common.Constants.PRIORITY_LOW
import com.canerture.firebaseexamples.common.Constants.PRIORITY_MEDIUM
import com.canerture.firebaseexamples.common.visible
import com.canerture.firebaseexamples.data.model.Todo
import com.canerture.firebaseexamples.databinding.ItemTodoBinding
import com.canerture.firebaseexamples.databinding.NativeAdLayoutBinding
import com.google.android.gms.ads.nativead.NativeAd

class TodosAdapter : ListAdapter<Todo, RecyclerView.ViewHolder>(DiffCallback()) {

    var onEditClick: (String) -> Unit = {}
    var onDoneClick: (String) -> Unit = {}
    var onDeleteClick: (String) -> Unit = {}

    private var nativeAd: NativeAd? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_NATIVE_ADS -> {
                NativeAdsViewHolder(
                    NativeAdLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                TodoViewHolder(
                    ItemTodoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is TodoViewHolder -> {
                holder.bind(item)
            }
            is NativeAdsViewHolder -> {
                holder.bind()
            }
        }
    }

    inner class TodoViewHolder(private var binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Todo) {

            with(binding) {

                tvTodo.text = item.todo
                tvDate.text = item.date

                item.documentId?.let { documentId ->

                    when (item.priority) {
                        PRIORITY_LOW -> tvLowPriority.visible()
                        PRIORITY_MEDIUM -> tvMediumPriority.visible()
                        PRIORITY_HIGH -> tvHighPriority.visible()
                    }

                    cbDone.isChecked = item.isDone

                    cbDone.setOnCheckedChangeListener { _, _ ->
                        onDoneClick(documentId)
                    }

                    imgEdit.setOnClickListener {
                        onEditClick(documentId)
                    }

                    imgDelete.setOnClickListener {
                        onDeleteClick(documentId)
                    }
                }
            }
        }
    }

    inner class NativeAdsViewHolder(private var binding: NativeAdLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {

            with(binding) {
                nativeAd?.let {
                    nativeAdView.mediaView = adMedia
                    nativeAdView.headlineView = adHeadline
                    nativeAdView.bodyView = adBody
                    nativeAdView.callToActionView = adBtnAction
                    nativeAdView.iconView = adAppIcon
                    nativeAdView.priceView = adPrice
                    nativeAdView.starRatingView = adStars
                    nativeAdView.storeView = adStore
                    nativeAdView.advertiserView = adAdvertiser
                    nativeAdView.mediaView?.setMediaContent(it.mediaContent!!)
                    nativeAdView.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    (nativeAdView.headlineView as TextView).text = it.headline
                    (nativeAdView.bodyView as TextView).text = it.body
                    (nativeAdView.callToActionView as Button).text = it.callToAction
                    (nativeAdView.iconView as ImageView).setImageDrawable(it.icon?.drawable)
                    (nativeAdView.priceView as TextView).text = it.price
                    (nativeAdView.storeView as TextView).text = it.store
                    (nativeAdView.starRatingView as RatingBar).rating = it.starRating!!.toFloat()
                    (nativeAdView.advertiserView as TextView).text = it.advertiser
                    nativeAdView.setNativeAd(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            null -> ITEM_NATIVE_ADS
            else -> ITEM_TODO
        }
    }

    fun setNativeAds(nativeAd: NativeAd) {
        this.nativeAd = nativeAd
    }

    private class DiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem.documentId == newItem.documentId

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
    }

    companion object {
        const val ITEM_TODO = 1
        const val ITEM_NATIVE_ADS = 2
    }
}