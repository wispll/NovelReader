package com.issac.novel.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.issac.novel.extract.HotItem
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView.ScaleType
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import com.issac.novel.R
import com.issac.novel.fragment.MainFragmentDirections
import com.youth.banner.adapter.BannerAdapter

class PopularBannerAdapter(data: List<HotItem>)
    : BannerAdapter<HotItem, PopularBannerAdapter.BannerHolder>(data) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ScaleType.CENTER_INSIDE
        return BannerHolder(imageView)
    }

    override fun onBindView(holder: BannerHolder?, data: HotItem?, position: Int, size: Int) {
        Glide.with(holder?.itemView!!)
            .load(data?.image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(holder.imageView)

        holder.imageView.setOnClickListener{

            val action = MainFragmentDirections.actionMainToDetail(data?.href!!)
            Navigation.findNavController(it).navigate(action)

        }
    }

    inner class BannerHolder(var imageView: ImageView) :
        RecyclerView.ViewHolder(imageView)
}



