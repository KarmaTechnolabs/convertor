package karma.convertor.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Wrapper class for loading images from network
 * Use this class for loading any images across the app
 *
 * @property url full image url
 * @property uri image uri
 * @property gifDrawableResource drawable resource id of GIF file
 */
class ConvertorImageLoader private constructor(
        val url: String? = null,
        val uri: Uri? = null,
        val gifDrawableResource: Int = 0
) {

    private var placeHolder: Int = 0
    private var shouldShowShimmerPlaceHolder = false
    private var shouldAnimate: Boolean = false
    private var shouldBlur: Boolean = false
    private var cacheStrategy: CacheStrategy = CacheStrategy.SESSION_CACHE
    private var disableTransform: Boolean = false
    private var asRounded: Boolean = false
    private var fitCenter: Boolean = false
    private var listener: ImageLoadingListener? = null

    /**
     * @param placeHolder Resource id of placeholder image that will be displayed
     * until image is loaded or any errors
     */
    fun placeHolder(placeHolder: Int) = apply { this.placeHolder = placeHolder }

    /**
     * To load image with shimmer loading effect, turned off by default
     * If placeHolder is set explicitly while building the request, this this method won't have
     * any effects
     */
    fun withShimmerPlaceHolder() = apply { this.shouldShowShimmerPlaceHolder = true }

    /**
     * @param shouldAnimate turn on to load images with fade animation
     * By default this is turned off
     */
    fun shouldAnimate(shouldAnimate: Boolean) = apply { this.shouldAnimate = shouldAnimate }

    /**
     * To load blur image
     * By default this is turned off
     */
    fun blur() = apply { this.shouldBlur = true }

    /**
     * @param listener callback when image is loaded successfully
     */
    fun listener(listener: ImageLoadingListener?) = apply { this.listener = listener }

    /**
     * Load image without any transformation
     */
    fun withNoTransform() = apply { this.disableTransform = true }

    /**
     * Load image without any cache, it will be loaded every time from remote
     */
    fun withNoCache() = apply { this.cacheStrategy = CacheStrategy.NO_CACHE }

    /**
     * Load image by storing image in permanent cache
     */
    fun withPersistentCache() = apply { this.cacheStrategy = CacheStrategy.ALWAYS_CACHE }

    /**
     * Load image as circle/rounded image
     */
    fun asRounded() = apply { this.asRounded = true }

    /**
     * Load image as fit center, default is center crop
     */
    fun fitCenter() = apply { this.fitCenter = true }

    fun into(imageView: ImageView) {

        // load GIF if requested
        if (gifDrawableResource != 0) {
            Glide.with(imageView.context)
                    .asGif()
                    .load(gifDrawableResource)
                    .into(imageView)
        } else {
            val requestManager = Glide.with(imageView.context)
            var request: RequestBuilder<Drawable>?


            val shimmer = Shimmer.AlphaHighlightBuilder() // The attributes for a ShimmerDrawable is set by this builder
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setBaseAlpha(0.87f)
                    .setAutoStart(true)
                    .build()

            // This is the placeholder for the imageView
            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmer)
            }


            request = when {
                !url.isNullOrBlank() -> {
                    val imageUrl = url
                    requestManager.load(imageUrl)
                }
                uri != null -> {
                    requestManager.load(uri)
                }
                placeHolder != 0 -> {
                    requestManager.load(placeHolder)
                }
                shouldShowShimmerPlaceHolder -> {
                    requestManager.load(shimmerDrawable)
                }
                else -> null
            }

            if (request != null) {

                var requestOptions = RequestOptions()

                if (placeHolder != 0) {
                    requestOptions = requestOptions.placeholder(placeHolder)
                    requestOptions = requestOptions.error(placeHolder)
                } else if (shouldShowShimmerPlaceHolder) {
                    requestOptions = requestOptions.placeholder(shimmerDrawable)
                    requestOptions = requestOptions.error(shimmerDrawable)
                }

                requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                when (cacheStrategy) {
                    CacheStrategy.NO_CACHE -> {
                        requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        requestOptions = requestOptions.skipMemoryCache(true)
                    }
                    CacheStrategy.SESSION_CACHE -> {
                        if (diskCacheSignature == null) {
                            invalidateSessionCache()
                        }
                        diskCacheSignature?.let {
                            requestOptions.signature(ObjectKey(it))
                        }
                    }
                    else -> {
                        //Cache always, do nothing
                    }
                }

                if (shouldBlur) {
                    request.apply {
                        transform(BlurTransformation())
                    }
                }
                if (shouldAnimate) {
                    request.apply {
                        transition(withCrossFade())
                    }
                } else {
                    requestOptions = requestOptions.dontAnimate()
                }
                if (disableTransform) {
                    requestOptions = requestOptions.dontTransform()
                }

                if (asRounded) {
                    requestOptions = requestOptions.circleCrop()
                }
                if (fitCenter) {
                    requestOptions = requestOptions.fitCenter()
                }

                listener?.let {

                    request?.listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            it.onImageLoaded()
                            return false
                        }

                    })
                }

                request = request.apply(requestOptions)
                request.into(imageView)
            }
        }
    }

    companion object Request {

        var diskCacheSignature: String? = null

        @JvmStatic
        fun invalidateSessionCache() {
            diskCacheSignature = System.currentTimeMillis().toString()
        }

        @JvmStatic
        fun load(url: String?) = ConvertorImageLoader(url)

        @JvmStatic
        fun load(uri: Uri?) = ConvertorImageLoader(uri = uri)

        @JvmStatic
        fun loadGif(resourceId: Int) = ConvertorImageLoader(gifDrawableResource = resourceId)
    }
}

enum class CacheStrategy {
    NO_CACHE, //load image from internet every time
    ALWAYS_CACHE, //load image only once from the internet, load from cache afterwards
    SESSION_CACHE //load image once per session, session will be cleared when app will be opened again
}

interface ImageLoadingListener {
    fun onImageLoaded()
}