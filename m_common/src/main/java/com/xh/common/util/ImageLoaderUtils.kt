package com.xh.common.util

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.xh.common.config.BaseConfig
import jp.wasabeef.glide.transformations.CropCircleTransformation


/**
 * Created by BHKJ on 2016/5/11.
 */

fun ImageView.displayImage(url: String) {
    Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(BaseConfig.IMAGE_PLACEHOLDER)
            .error(BaseConfig.IMAGE_ERROR)
            .centerCrop()
            .into(this)
}

fun ImageView.displayImageFitCenter(url: String) {
    Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(BaseConfig.IMAGE_PLACEHOLDER)
            .error(BaseConfig.IMAGE_ERROR)
            .fitCenter()
            .into(this)
}

fun ImageView.displayImageAvatar(url: String, error: Int = BaseConfig.IMAGE_ERROR) {
    //毛玻璃效果  需要时可以添加 BlurTransformation
    Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(error)
            .error(error)
            .centerCrop()
            .bitmapTransform(CropCircleTransformation(context))
            .into(this)
}

fun ImageView.displayImageAvatar(uri: Uri) {
    //毛玻璃效果  需要时可以添加 BlurTransformation
    Glide.with(context)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(BaseConfig.IMAGE_PLACEHOLDER)
            .error(BaseConfig.IMAGE_ERROR)
            .centerCrop()
//            .bitmapTransform(CropCircleTransformation(context))
            .into(this)
}

fun ImageView.getBitmapFromUrl(url: String, listener: (Bitmap) -> Unit) {
    Glide.with(context)
            .load(url)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>) {
                    if (resource != null) {
                        listener(resource)
                    }
                }
            })
}

fun ImageView.displayIntoUseFitWidth(url: String) {
    Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .listener(object : RequestListener<String, GlideDrawable> {
                override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    if (this@displayIntoUseFitWidth == null) {
                        return false
                    }
                    if (this@displayIntoUseFitWidth.getScaleType() != ImageView.ScaleType.FIT_XY) {
                        this@displayIntoUseFitWidth.setScaleType(ImageView.ScaleType.FIT_XY)
                    }
                    val params = this@displayIntoUseFitWidth.getLayoutParams()
                    val vw = this@displayIntoUseFitWidth.getWidth() -
                            this@displayIntoUseFitWidth.getPaddingLeft() -
                            this@displayIntoUseFitWidth.getPaddingRight()
                    val scale = vw.toFloat() / resource!!.getIntrinsicWidth().toFloat()
                    val vh = Math.round(resource.getIntrinsicHeight() * scale)
                    params.height = vh + this@displayIntoUseFitWidth.getPaddingTop() +
                            this@displayIntoUseFitWidth.getPaddingBottom()
                    this@displayIntoUseFitWidth.setLayoutParams(params)
                    return false
                }

            })
            .into(this);
}