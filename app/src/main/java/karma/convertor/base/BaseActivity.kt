package karma.convertor.base

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import karma.convertor.custom.showToast
import karma.convertor.listeners.PermissionListener
import karma.convertor.listeners.SnackbarListener
import com.google.android.material.snackbar.Snackbar
import karma.convertor.R


open class BaseActivity : AppCompatActivity() {
    private var progressDialog: Dialog? = null

    internal var cameraPermissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

    fun showProgressDialog() {
        fun showProgressDialog() {
            if (progressDialog == null || !progressDialog?.isShowing!!) {
                progressDialog = Dialog(this)
                progressDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progressDialog?.setContentView(R.layout.custom_progressbar)
                progressDialog?.setCancelable(false)
                progressDialog?.show()
            }
        }
    }

        fun hideProgressDialog() {
            try {
                if (progressDialog != null && progressDialog?.isShowing!!) {
                    progressDialog?.dismiss()
                }
            } catch (throwable: Throwable) {

            } finally {
                progressDialog?.dismiss()
            }
        }

        fun <T> manageAPIResource(resource: APIResource<T>, successListener: (T, String) -> Unit) {
            when (resource.status) {
                Status.LOADING -> {
                    showProgressDialog()
                }
                Status.ERROR -> {
                    hideProgressDialog()
                    resource.message?.let {
                        showToast(it)
                    }
                }
                Status.NO_NETWORK -> {
                    hideProgressDialog()
                    showToast(R.string.no_network_available)
                }
                else -> {
                    hideProgressDialog()
                    successListener.invoke(resource.data!!, resource.message!!)
                }
            }
        }

        fun showPermissionAlert(message: String, listener: PermissionListener) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.need_permission))
            builder.setMessage(message)
            builder.setPositiveButton(
                getString(R.string.ok),
                { dialog, which -> listener.onPermissionClick() })
            builder.setNeutralButton(getString(R.string.cancel), null)
            val dialog = builder.create()
            dialog.show()
        }

        protected fun showSnackbar(
            view: View,
            message: String,
            time: Int,
            snackbarListener: SnackbarListener
        ) {
            val snackbar = Snackbar.make(view, message, time)
                .setAction(getString(R.string.go_to_settings), View.OnClickListener {
                    snackbarListener.onSnackbarClick()
                })
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            snackbar.show()
        }
}