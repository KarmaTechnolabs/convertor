package karma.converter.custom

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}



@BindingAdapter("backGroundColorForCardView")
fun setBackGroundColor(cardView: MaterialCardView, resource: Int) {
    cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, resource))
}
