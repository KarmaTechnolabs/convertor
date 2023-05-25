package karma.converter.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import karma.converter.BuildConfig
import karma.converter.R
import karma.converter.adapter.PickerAdapter
import karma.converter.adapter.PickerLayoutManager
import karma.converter.adapter.ScreenUtils
import karma.converter.adapter.UnitActivityAdpater
import karma.converter.api.requestmodel.UnitActivityModelResponse
import karma.converter.api.requestmodel.UnitItemModel
import karma.converter.base.BaseActivity
import karma.converter.databinding.ActivityDataBinding
import karma.converter.listeners.ItemClickListener
import karma.converter.viewmodel.WeightViewModel
import java.util.*
import kotlin.collections.ArrayList

class DataActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitActivityModelResponse> {


    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding:ActivityDataBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnitItemModel>()
    private val viewModel by viewModels<WeightViewModel>()
    var unitActivityList = java.util.ArrayList<UnitActivityModelResponse>()
    private val data = ArrayList<String>()
    private lateinit var rvHorizontalPicker: RecyclerView
    private lateinit var sliderAdapter: PickerAdapter
    private var unitActivityAdapter: UnitActivityAdpater? = UnitActivityAdpater(this)

    // TextView used to display the input and output

    // Represent whether the lastly pressed key is numeric or not
    var lastNumeric: Boolean = false

    // Represent that current state is in error or not
    var stateError: Boolean = false

    // If true, do not allow to add another DOT
    var lastDot: Boolean = false

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this

        binding.header.backToHome.setOnClickListener(this)
        binding.header.shareImageView.setOnClickListener(this)

        binding.header.toolbar.text = resources.getString(R.string.data)

        binding.inputValue.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val num1 = binding.inputValue.text.toString()
                if (num1.isNotBlank()) {
                    val num2 = binding.inputValue.text.toString().trim().toDouble()
                    val num3 = (num2).toString()


                    viewModel.callweightAPI(num3)
                } else {
                    viewModel.callweightAPI("")
                }
            }
        })


        viewModel.weightResponse.observe(this) {
            unitActivityList.clear()
            if (it.conversionValue != "") {
                unitActivityList.add(UnitActivityModelResponse("BYTE", (it.conversionValue.trim().toDouble() * 0.125).toString()))
                unitActivityList.add(UnitActivityModelResponse("KB", (it.conversionValue.trim().toDouble() * 0.000125).toString()))
                unitActivityList.add(UnitActivityModelResponse("MB", (it.conversionValue.trim().toDouble() * 1.25e-7).toString()))
                unitActivityList.add(UnitActivityModelResponse("GB", (it.conversionValue.trim().toDouble() * 1.25e-10).toString()))
                unitActivityList.add(UnitActivityModelResponse("TB", (it.conversionValue.trim().toDouble() * 1.25e-13).toString()))

            } else {
                unitActivityList.add(UnitActivityModelResponse("BYTE", ""))
                unitActivityList.add(UnitActivityModelResponse("KB", ""))
                unitActivityList.add(UnitActivityModelResponse("MB", ""))
                unitActivityList.add(UnitActivityModelResponse("GB", ""))
                unitActivityList.add(UnitActivityModelResponse("TB", ""))
            }

            unitActivityAdapter?.clear()
            unitActivityAdapter?.setClickListener(this)
            binding.unitRecycler.adapter = unitActivityAdapter
            unitActivityAdapter?.setItems(unitActivityList)


        }

        setPicker()

    }


    private fun setPicker() {
        data.add("Bit")
        data.add("Byte")
        data.add("KB")
        data.add("MB")
        data.add("GB")
        data.add("TB")




        rvHorizontalPicker = binding.rvHorizontalPicker

        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(this) / 2 - ScreenUtils.dpToPx(this, 65)
        rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        rvHorizontalPicker.layoutManager = PickerLayoutManager(this).apply {
            callback = object : PickerLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    sliderAdapter.setSelectedItem(layoutPosition)

                    if(layoutPosition == 0) {

                        sliderAdapter.setSelectedItem(0)
                        binding.inputValue.addTextChangedListener(object : TextWatcher {

                            override fun afterTextChanged(s: Editable) {}

                            override fun beforeTextChanged(
                                s: CharSequence, start: Int,
                                count: Int, after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence, start: Int,
                                before: Int, count: Int
                            ) {
                                val num1 = binding.inputValue.text.toString()
                                if (num1.isNotBlank()) {
                                    val num2 = binding.inputValue.text.toString().trim().toDouble()
                                    val num3 = (num2).toString()


                                    viewModel.callweightAPI(num3)
                                } else {
                                    viewModel.callweightAPI("")
                                }
                            }
                        })


                        viewModel.weightResponse.observe(this@DataActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("BYTE", (it.conversionValue.trim().toDouble() * 0.125).toString()))
                                unitActivityList.add(UnitActivityModelResponse("KB", (it.conversionValue.trim().toDouble() * 0.000125).toString()))
                                unitActivityList.add(UnitActivityModelResponse("MB", (it.conversionValue.trim().toDouble() * 1.25e-7).toString()))
                                unitActivityList.add(UnitActivityModelResponse("GB", (it.conversionValue.trim().toDouble() * 1.25e-10).toString()))
                                unitActivityList.add(UnitActivityModelResponse("TB", (it.conversionValue.trim().toDouble() * 1.25e-13).toString()))


                            } else {
                                unitActivityList.add(UnitActivityModelResponse("BYTE", ""))
                                unitActivityList.add(UnitActivityModelResponse("KB", ""))
                                unitActivityList.add(UnitActivityModelResponse("MB", ""))
                                unitActivityList.add(UnitActivityModelResponse("GB", ""))
                                unitActivityList.add(UnitActivityModelResponse("TB", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@DataActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)


                        }

                    }else if(layoutPosition == 1){

                        binding.inputValue.addTextChangedListener(object : TextWatcher {

                            override fun afterTextChanged(s: Editable) {}

                            override fun beforeTextChanged(
                                s: CharSequence, start: Int,
                                count: Int, after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence, start: Int,
                                before: Int, count: Int
                            ) {
                                val num1 = binding.inputValue.text.toString()
                                if (num1.isNotBlank()) {
                                    val num2 = binding.inputValue.text.toString().trim().toDouble()
                                    val num3 = (num2).toString()


                                    viewModel.callweightAPI(num3)
                                } else {
                                    viewModel.callweightAPI("")
                                }
                            }
                        })


                        viewModel.weightResponse.observe(this@DataActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("BIT", (it.conversionValue.trim().toDouble() * 8).toString()))
                                unitActivityList.add(UnitActivityModelResponse("KB", (it.conversionValue.trim().toDouble() * 0.0009765625).toString()))
                                unitActivityList.add(UnitActivityModelResponse("MB", (it.conversionValue.trim().toDouble() * 1e-6).toString()))
                                unitActivityList.add(UnitActivityModelResponse("GB", (it.conversionValue.trim().toDouble() *1e-9).toString()))
                                unitActivityList.add(UnitActivityModelResponse("TB", (it.conversionValue.trim().toDouble() * 1e-12).toString()))

                            } else {
                                unitActivityList.add(UnitActivityModelResponse("BIT", ""))
                                unitActivityList.add(UnitActivityModelResponse("KB", ""))
                                unitActivityList.add(UnitActivityModelResponse("MB", ""))
                                unitActivityList.add(UnitActivityModelResponse("GB", ""))
                                unitActivityList.add(UnitActivityModelResponse("TB", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@DataActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)
                        }

                    }else if(layoutPosition == 2){
                        binding.inputValue.addTextChangedListener(object : TextWatcher {

                            override fun afterTextChanged(s: Editable) {}
                            override fun beforeTextChanged(
                                s: CharSequence, start: Int,
                                count: Int, after: Int
                            ) {
                            }
                            override fun onTextChanged(
                                s: CharSequence, start: Int,
                                before: Int, count: Int
                            ) {
                                val num1 = binding.inputValue.text.toString()
                                if (num1.isNotBlank()) {
                                    val num2 = binding.inputValue.text.toString().trim().toDouble()
                                    val num3 = (num2).toString()
                                    viewModel.callweightAPI(num3)
                                } else {
                                    viewModel.callweightAPI("")
                                }
                            }
                        })


                        viewModel.weightResponse.observe(this@DataActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("BIT", (it.conversionValue.trim().toDouble() * 8192).toString()))
                                unitActivityList.add(UnitActivityModelResponse("BYTE", (it.conversionValue.trim().toDouble() * 1024).toString()))
                                unitActivityList.add(UnitActivityModelResponse("MB", (it.conversionValue.trim().toDouble() * 0.0009765625).toString()))
                                unitActivityList.add(UnitActivityModelResponse("GB", (it.conversionValue.trim().toDouble() *1e-6).toString()))
                                unitActivityList.add(UnitActivityModelResponse("TB", (it.conversionValue.trim().toDouble() * 1e-9).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("BIT", ""))
                                unitActivityList.add(UnitActivityModelResponse("BYTE", ""))
                                unitActivityList.add(UnitActivityModelResponse("MB", ""))
                                unitActivityList.add(UnitActivityModelResponse("GB", ""))
                                unitActivityList.add(UnitActivityModelResponse("TB", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@DataActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)


                        }



                    }else if (layoutPosition == 3){

                        binding.inputValue.addTextChangedListener(object : TextWatcher {

                            override fun afterTextChanged(s: Editable) {}
                            override fun beforeTextChanged(
                                s: CharSequence, start: Int,
                                count: Int, after: Int
                            ) {
                            }
                            override fun onTextChanged(
                                s: CharSequence, start: Int,
                                before: Int, count: Int
                            ) {
                                val num1 = binding.inputValue.text.toString()
                                if (num1.isNotBlank()) {
                                    val num2 = binding.inputValue.text.toString().trim().toDouble()
                                    val num3 = (num2).toString()
                                    viewModel.callweightAPI(num3)
                                } else {
                                    viewModel.callweightAPI("")
                                }
                            }
                        })


                        viewModel.weightResponse.observe(this@DataActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("BIT", (it.conversionValue.trim().toDouble() * 1073741824).toString()))
                                unitActivityList.add(UnitActivityModelResponse("BYTE", (it.conversionValue.trim().toDouble() *1048576).toString()))
                                unitActivityList.add(UnitActivityModelResponse("KB", (it.conversionValue.trim().toDouble() * 1024).toString()))
                                unitActivityList.add(UnitActivityModelResponse("GB", (it.conversionValue.trim().toDouble() *0.0009765625).toString()))
                                unitActivityList.add(UnitActivityModelResponse("TB", (it.conversionValue.trim().toDouble() * 1e-6).toString()))
                            } else {
                                unitActivityList.add(UnitActivityModelResponse("BIT", ""))
                                unitActivityList.add(UnitActivityModelResponse("BYTE", ""))
                                unitActivityList.add(UnitActivityModelResponse("KB", ""))
                                unitActivityList.add(UnitActivityModelResponse("GB", ""))
                                unitActivityList.add(UnitActivityModelResponse("TB", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@DataActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)


                        }

                    }else if(layoutPosition == 4){

                        binding.inputValue.addTextChangedListener(object : TextWatcher {

                            override fun afterTextChanged(s: Editable) {}
                            override fun beforeTextChanged(
                                s: CharSequence, start: Int,
                                count: Int, after: Int
                            ) {
                            }
                            override fun onTextChanged(
                                s: CharSequence, start: Int,
                                before: Int, count: Int
                            ) {
                                val num1 = binding.inputValue.text.toString()
                                if (num1.isNotBlank()) {
                                    val num2 = binding.inputValue.text.toString().trim().toDouble()
                                    val num3 = (num2).toString()
                                    viewModel.callweightAPI(num3)
                                } else {
                                    viewModel.callweightAPI("")
                                }
                            }
                        })


                        viewModel.weightResponse.observe(this@DataActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {

                                unitActivityList.add(UnitActivityModelResponse("BIT", (it.conversionValue.trim().toDouble() * 1.0995116e+12).toString()))
                                unitActivityList.add(UnitActivityModelResponse("BYTE", (it.conversionValue.trim().toDouble() * 1073741824).toString()))
                                unitActivityList.add(UnitActivityModelResponse("KB", (it.conversionValue.trim().toDouble() * 1048576).toString()))
                                unitActivityList.add(UnitActivityModelResponse("MB", (it.conversionValue.trim().toDouble() *1024).toString()))
                                unitActivityList.add(UnitActivityModelResponse("TB", (it.conversionValue.trim().toDouble() * 0.0009765625).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("BIT", ""))
                                unitActivityList.add(UnitActivityModelResponse("BYTE", ""))
                                unitActivityList.add(UnitActivityModelResponse("KB", ""))
                                unitActivityList.add(UnitActivityModelResponse("MB", ""))
                                unitActivityList.add(UnitActivityModelResponse("TB", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@DataActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)


                        }

                    }else if(layoutPosition == 5){


                        binding.inputValue.addTextChangedListener(object : TextWatcher {

                            override fun afterTextChanged(s: Editable) {}
                            override fun beforeTextChanged(
                                s: CharSequence, start: Int,
                                count: Int, after: Int
                            ) {
                            }
                            override fun onTextChanged(
                                s: CharSequence, start: Int,
                                before: Int, count: Int
                            ) {
                                val num1 = binding.inputValue.text.toString()
                                if (num1.isNotBlank()) {
                                    val num2 = binding.inputValue.text.toString().trim().toDouble()
                                    val num3 = (num2).toString()
                                    viewModel.callweightAPI(num3)
                                } else {
                                    viewModel.callweightAPI("")
                                }
                            }
                        })


                        viewModel.weightResponse.observe(this@DataActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("BIT", (it.conversionValue.trim().toDouble() * 1.1258999e+15).toString()))
                                unitActivityList.add(UnitActivityModelResponse("BYTE", (it.conversionValue.trim().toDouble() * 1.0995116e+12).toString()))
                                unitActivityList.add(UnitActivityModelResponse("KB", (it.conversionValue.trim().toDouble() * 1073741824).toString()))
                                unitActivityList.add(UnitActivityModelResponse("MB", (it.conversionValue.trim().toDouble() *1048576).toString()))
                                unitActivityList.add(UnitActivityModelResponse("GB", (it.conversionValue.trim().toDouble() * 1024).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("BIT", ""))
                                unitActivityList.add(UnitActivityModelResponse("BYTE", ""))
                                unitActivityList.add(UnitActivityModelResponse("KB", ""))
                                unitActivityList.add(UnitActivityModelResponse("MB", ""))
                                unitActivityList.add(UnitActivityModelResponse("GB", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@DataActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)


                        }








                    }



                }
            }
        }

        // Setting Adapter
        sliderAdapter = PickerAdapter()
        rvHorizontalPicker.adapter = sliderAdapter.apply {
            setData(data)
            callback = object : PickerAdapter.Callback {
                override fun onItemClicked(view: View) {

                    rvHorizontalPicker.smoothScrollToPosition(
                        rvHorizontalPicker.getChildLayoutPosition(
                            view
                        )
                    )
                }
            }
        }
    }


    override fun onClick(view: View?) {
        when (view) {
            binding.header.backToHome -> {
                onBackPressedDispatcher.onBackPressed()
            }

            binding.header.shareImageView -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val app_url = resources.getString(R.string.whatsapp_sharemessages) + BuildConfig.APPLICATION_ID
                shareIntent.putExtra(Intent.EXTRA_TEXT, app_url)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }

        }
    }


    override fun onItemClick(
        viewIdRes: Int,
        model: UnitActivityModelResponse,
        position: Int
    ) {
        when (viewIdRes) {
            /*  R.id.pdfview -> {
                  startDownloadingFile(
                      model.pdf_url,
                      BuildConfig.PDF_BASE_URL + model.pdf_url
                  )

              }*/
        }
    }

    fun onDigit(view: View) {
        if (stateError) {
            // If current state is Error, replace the error message
            binding.inputValue.text = (view as Button).text as Editable?
            stateError = false
        } else {
            // If not, already there is a valid expression so append to it
            binding.inputValue.append((view as Button).text)
        }
        // Set the flag
        lastNumeric = true
    }

    /**
     * Append . to the TextView
     */
    fun onDecimalPoint(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            binding.inputValue.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    /**
     * Append +,-,*,/ operators to the TextView
     */
    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            binding.inputValue.append((view as Button).text)
            lastNumeric = false
            lastDot = false    // Reset the DOT flag
        }
    }


    /**
     * Clear the TextView
     */
    fun onClear(view: View) {
        binding.inputValue.text = " "
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    fun Clear(view: View) {

        var str: String = binding.inputValue.text.toString()
        if (!str.equals("")) {
            if (lastDot == true && str.equals(".")) {
                str = str.substring(0, str.length - 1)
                binding.inputValue.setText(str)
                lastDot = false

            } else {
                str = str.substring(0, str.length - 1)
                binding.inputValue.setText(str)
                lastDot = false


            }

        }
    }


}