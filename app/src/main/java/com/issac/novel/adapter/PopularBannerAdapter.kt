package com.issac.novel.adapter

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.issac.novel.MainActivity
import com.issac.novel.R
import com.issac.novel.extract.HotItem
import com.issac.novel.fragment.DetailFragment
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
            val fragment = DetailFragment()
            val arg = Bundle()
            arg.putString("href", data?.href)
            fragment.arguments = arg

            val activity = holder.imageView.context as MainActivity
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment,"detailFragment")
                .addToBackStack("detail")
                .commit()
        }
    }

    inner class BannerHolder(var imageView: ImageView) :
        RecyclerView.ViewHolder(imageView)
}



