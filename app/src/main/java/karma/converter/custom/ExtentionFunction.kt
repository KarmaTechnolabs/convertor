package karma.converter.custom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import karma.converter.R
import karma.converter.utils.Constants
import karma.converter.utils.SerializedExclusionStrategy
import karma.converter.utils.SharedPreferenceHelper
import com.google.gson.GsonBuilder
import timber.log.Timber

fun Activity.showToast(@StringRes id: Int, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, id, length).show()
}

fun Activity.showToast(text: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text, length).show()
}

fun Fragment.showToast(@StringRes id: Int, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireActivity(), id, length).show()
}

fun Fragment.showToast(text: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireActivity(), text, length).show()
}

fun <T> Activity.gotoActivity(
    activity: Class<T>,
    bundle: Bundle? = null,
    needToFinish: Boolean = true,
    clearAllActivity: Boolean = false
) {
    val intent = Intent(this, activity)

    if (bundle != null)
        intent.putExtras(bundle)

    if (clearAllActivity)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

    startActivity(intent)

    if (needToFinish)
        finish()

   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun <T> Activity.savePreferenceValue(key: String, value: T) {
    SharedPreferenceHelper.getInstance(this)
        .setValue(key, value)
}

fun <T> Fragment.savePreferenceValue(key: String, value: T) {
    SharedPreferenceHelper.getInstance(requireContext())
        .setValue(key, value)
}

fun <T> Activity.saveClassAsJson(key: String, classObject: T) {
    val gson =
        GsonBuilder().addSerializationExclusionStrategy(SerializedExclusionStrategy()).create()
    val json = gson.toJson(classObject)
    Timber.d("Saved Json -> $json")
    SharedPreferenceHelper.getInstance(this)
        .setValue(key, json)
}

fun <T> Activity.getPreferenceValue(key: String, defaultValue: T): T {
    return SharedPreferenceHelper.getInstance(this)
        .getValue(key, defaultValue)
}

fun <T> Fragment.getPreferenceValue(key: String, defaultValue: T): T {
    return SharedPreferenceHelper.getInstance(requireContext())
        .getValue(key, defaultValue)
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun String.createClickableSpan(
    clickListener: (view: View) -> Unit,
    typeface: Typeface? = null
): SpannableString {
    val spannableString = SpannableString(this)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            clickListener.invoke(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            typeface?.let {
                ds.typeface = it
            }
        }
    }

    spannableString.setSpan(
        clickableSpan,
        0,
        spannableString.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

fun String.createCustomFontColorSpan(
    typeface: Typeface?,
    @ColorRes colorResourceId: Int = R.color.colorAccent,
    context: Context? = null
): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(
        CustomTypefaceSpan(typeface),
        0,
        spannableString.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    context?.let {
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    it,
                    colorResourceId
                )
            ), 0, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannableString
}

fun Fragment.hideKeyboard(view: View) {
    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Fragment.openApplicationSettings(requestCode: Int = Constants.EXTRA_ACTIVITY_RESULT_REQUEST_CODE) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", requireActivity().packageName, null)
    intent.data = uri
    startActivityForResult(intent, requestCode)
}

fun String.createClickableSpan(
    clickListener: (view: View) -> Unit
): SpannableString {
    val spannableString = SpannableString(this)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            clickListener.invoke(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
        }
    }

    spannableString.setSpan(
        clickableSpan,
        0,
        spannableString.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

fun CharSequence?.checkIsNullOrBlank(): Boolean {
    if (this == null)
        return true
    return this.isEmpty() || this.isBlank()
}

