package karma.convertor.custom

import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import karma.convertor.R
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, url: String?) {
    imageView.load(url, builder = {
        this.error(R.drawable.ic_splash_logo)
    })
}

@BindingAdapter("backGroundColorForCardView")
fun setBackGroundColor(cardView: MaterialCardView, resource: Int) {
    cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, resource))
}

@BindingAdapter("bind:imageUri")
fun loadImageFromUrl(view: CircleImageView, imageUri: Uri?) {
    if (imageUri == null) {
        view.setImageResource(R.drawable.ic_profile_placeholder)
        return
    }
    Glide.with(view.context)
//        .load(BuildConfig.BASE_IMAGE_URL.plus(imageUrl))
        .load(imageUri)
        .error(R.drawable.ic_profile_placeholder)
        .centerCrop()
        .placeholder(R.drawable.ic_profile_placeholder)
        .into(view)
}
@BindingAdapter("bind:imageUrl")
fun loadImage(view: CircleImageView, imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) {
        view.setImageResource(R.drawable.ic_profile_placeholder)
        return
    }
    Glide.with(view.context)
//        .load(BuildConfig.BASE_IMAGE_URL.plus(imageUrl))
        .load(imageUrl)
        .error(R.drawable.ic_profile_placeholder)
        .centerCrop()
        .placeholder(R.drawable.ic_profile_placeholder)
        .into(view)
}