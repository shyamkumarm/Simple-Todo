package com.zuper.todo.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.*

object ImageLoader {

     fun getRandomId():String = "temp=${UUID.randomUUID()}"

    fun loadImage(
        imageUrl: String?,
        imageViewToLoad: ImageView,
        placeHolderId: Int = 0
    ) {
        Glide.with(imageViewToLoad.context).load(imageUrl).apply {
            placeholder(placeHolderId)
            transform(CenterCrop())
            transition(DrawableTransitionOptions.withCrossFade())

        }.into(imageViewToLoad)

    }


}
