package karma.converter.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import karma.converter.R
import karma.converter.base.BaseActivity
import karma.converter.databinding.ActivityWebviewBinding
import karma.converter.utils.Constants

class WebActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityWebviewBinding

    private val title: String by lazy {
        intent.getStringExtra(Constants.EXTRA_TITLE) ?: ""
    }
    private val link: String by lazy {
        intent.getStringExtra(Constants.EXTRA_LINK)
            ?: throw IllegalStateException("Missing URL")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview)
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {

        binding.toolbarGetInTouch.tvTitle.text = title
        binding.toolbarGetInTouch.ivBack.setOnClickListener(this)

        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                hideProgressDialog()
            }
        }

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(link)
        showProgressDialog()


    }

    override fun onClick(p0: View?) {
        onBackPressed()
    }
}