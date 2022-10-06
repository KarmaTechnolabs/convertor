package karma.convertor.utils

import android.app.Activity
import android.content.DialogInterface
import android.os.Handler
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

/**
 * Extension method to remove the required boilerplate for running code after a view has been
 * inflated and measured.
 *
 * @author Antonio Leiva
 * @see <a href="https://antonioleiva.com/kotlin-ongloballayoutlistener/>Kotlin recipes: OnGlobalLayoutListener</a>
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun Double.formattedPrice(): String {
    return String.format("%1\$s%2$.2f", Constants.DEFAULT_CURRENCY, this)
}

/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show() : View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Hide the view. (visibility = View.GONE)
 */
fun View.hide() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

/**
 * Invisible the view. (invisibility = View.INVISIBLE)
 */
fun View.invisible() : View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

fun EditText.showForcedKeyboard() {
    Handler().postDelayed({
        try {
            this.dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN,
                    0f,
                    0f,
                    0
                )
            )
            this.dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_UP,
                    0f,
                    0f,
                    0
                )
            )
            this.setSelection(this.getText().length)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }, 200)
}

fun Activity.showInfoAlert(title: String, message: String) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { _, _ -> }
        .create().show()
}

fun Fragment.showInfoAlert(title: String, message: String) {
    AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { _, _ -> }
        .create().show()
}