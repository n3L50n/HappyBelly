package com.nodecoyote.happybelly.adapter

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nodecoyote.happybelly.R
import com.nodecoyote.happybelly.utility.WebViewHelper
import com.nodecoyote.happybelly.utility.buttonFeedback
import com.nodecoyote.happybelly.viewmodel.HappyBellyCellViewModel
import com.nodecoyote.happybelly.viewmodel.HappyBellyListViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.cell_happy_belly_listing.view.*

class HappyBellyAdapter(
    private val _viewModel: HappyBellyListViewModel,
    private val _owner: LifecycleOwner
) : RecyclerView.Adapter<HappyBellyViewHolder>() {

    init {
        bindViewModels()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HappyBellyViewHolder =
        HappyBellyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_happy_belly_listing, parent, false)
        )

    override fun getItemCount(): Int {
        return _viewModel.searchResultCount.value ?: 0
    }

    override fun onBindViewHolder(holder: HappyBellyViewHolder, position: Int) {
        val viewModel = _viewModel.getViewModelFor(position)
        holder.bindView(viewModel, _owner)
    }

    private fun bindViewModels() {
        _viewModel.searchResultCount.observe(_owner, Observer {
            notifyDataSetChanged()
        })
    }
}

class HappyBellyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        val light = Typeface.createFromAsset(itemView.context.assets, "Montserrat-Light.otf")
        val regular = Typeface.createFromAsset(itemView.context.assets, "Montserrat-Regular.otf")
        val bold = Typeface.createFromAsset(itemView.context.assets, "Montserrat-Bold.otf")
        itemView.happy_belly_listing_title.typeface = bold
        itemView.happy_belly_listing_price_description.typeface = light
        itemView.happy_belly_listing_rating_description.typeface = light
        itemView.happy_belly_listing_rating.typeface = regular
    }

    fun bindView(viewModel: HappyBellyCellViewModel, owner: LifecycleOwner) {
        viewModel.name.observe(owner, Observer { name ->
            name?.let { itemView.happy_belly_listing_title.text = it }
        })
        viewModel.image.observe(owner, Observer { imageUrl ->
            imageUrl?.let {
                Picasso.get()
                    .load(imageUrl)
                    .transform(RoundedCornersTransformation(16, 0, RoundedCornersTransformation.CornerType.ALL))
                    .placeholder(R.drawable.placeholder_plate)
                    .fit()
                    .into(itemView.happy_belly_listing_image)
            }
        })
        viewModel.price.observe(owner, Observer { price ->
            price?.let { itemView.happy_belly_listing_price.text = price }
        })
        viewModel.rating.observe(owner, Observer { rating ->
            rating?.let { itemView.happy_belly_listing_rating.text = "$rating" }
        })
        viewModel.review.observe(owner, Observer { review ->
            review?.let { itemView.happy_belly_listing_review.text = review }
        })
        viewModel.url.observe(owner, Observer { url ->
            url?.let {
                itemView.setOnClickListener {
                    WebViewHelper().open(url, itemView.context)
                    itemView.context.buttonFeedback()
                }
            }
        })
    }

}