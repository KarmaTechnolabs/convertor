package karma.converter.custom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import karma.converter.R
import karma.converter.databinding.DialogLogOutBinding

class LogOutAlertDialog : DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogLogOutBinding
    private lateinit var desc: String
    private var listener: LogoutClickListener? = null

    companion object {
        const val DESC = "DESCRIPTION"

        fun newInstance(desc: String): LogOutAlertDialog {
            val f = LogOutAlertDialog()
            val args = Bundle()
            args.putString(DESC, desc)
            f.arguments = args
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        desc = arguments?.getString(DESC) ?: ""
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog

        if (dialog != null) {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()?.window!!.setWindowAnimations(R.style.dialog_animation_fade)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_log_out, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDescription.text = desc
        binding.btnNo.setOnClickListener(this)
        binding.btnYes.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        dismiss()
        if (view?.id == binding.btnNo.id) {

        } else if (view?.id == binding.btnYes.id) {
            listener?.onLogOutClick()
        }
    }

    interface LogoutClickListener {
        fun onLogOutClick()
    }

    fun setListener(listener: LogoutClickListener) {
        this.listener = listener
    }
}