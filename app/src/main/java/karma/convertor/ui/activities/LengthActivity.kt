package karma.convertor.ui.activities

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
import karma.convertor.BuildConfig
import karma.convertor.R
import karma.convertor.adapter.PickerAdapter
import karma.convertor.adapter.PickerLayoutManager
import karma.convertor.adapter.ScreenUtils
import karma.convertor.adapter.UnitActivityAdpater
import karma.convertor.api.requestmodel.UnitActivityModelResponse
import karma.convertor.api.requestmodel.UnititemModel
import karma.convertor.base.BaseActivity
import karma.convertor.custom.gotoActivity
import karma.convertor.databinding.ActivitySoundBinding
import karma.convertor.listeners.ItemClickListener
import karma.convertor.viewmodel.WeightViewModel
import java.util.*
import kotlin.collections.ArrayList

class LengthActivity: BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitActivityModelResponse> {


    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding:ActivitySoundBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnititemModel>()
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

        binding = ActivitySoundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this


        binding.header.backToHome.setOnClickListener(this)
        binding.header.shareImageView.setOnClickListener(this)

        binding.header.toolbar.text = resources.getString(R.string.length)

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
                unitActivityList.add(UnitActivityModelResponse("mile", (it.conversionValue.trim().toDouble() *0.621371).toString()))
                unitActivityList.add(UnitActivityModelResponse("m", (it.conversionValue.trim().toDouble()*1000 ).toString()))
                unitActivityList.add(UnitActivityModelResponse("dm", (it.conversionValue.trim().toDouble() * 10000).toString()))
                unitActivityList.add(UnitActivityModelResponse("cm", (it.conversionValue.trim().toDouble()*100000 ).toString()))
                unitActivityList.add(UnitActivityModelResponse("mm", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                unitActivityList.add(UnitActivityModelResponse("ft", (it.conversionValue.trim().toDouble()*3280.84).toString()))
                unitActivityList.add(UnitActivityModelResponse("inch", (it.conversionValue.trim().toDouble()*39370.1 ).toString()))


            } else {
                unitActivityList.add(UnitActivityModelResponse("mile", ""))
                unitActivityList.add(UnitActivityModelResponse("m", ""))
                unitActivityList.add(UnitActivityModelResponse("dm", ""))
                unitActivityList.add(UnitActivityModelResponse("cm", ""))
                unitActivityList.add(UnitActivityModelResponse("mm", ""))
                unitActivityList.add(UnitActivityModelResponse("ft", ""))
                unitActivityList.add(UnitActivityModelResponse("inch", ""))

            }

            unitActivityAdapter?.clear()
            unitActivityAdapter?.setClickListener(this)
            binding.unitRecycler.adapter = unitActivityAdapter
            unitActivityAdapter?.setItems(unitActivityList)


        }

        setPicker()

    }


    private fun setPicker() {
        data.add("km")
        data.add("mile")
        data.add("m")
        data.add("dm")
        data.add("cm")
        data.add("mm")
        data.add("ft")
        data.add("inch")






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


                        viewModel.weightResponse.observe(this@LengthActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("mile", (it.conversionValue.trim().toDouble() *0.621371).toString()))
                                unitActivityList.add(UnitActivityModelResponse("m", (it.conversionValue.trim().toDouble()*1000 ).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dm", (it.conversionValue.trim().toDouble() * 10000).toString()))
                                unitActivityList.add(UnitActivityModelResponse("cm", (it.conversionValue.trim().toDouble()*100000 ).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mm", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                                unitActivityList.add(UnitActivityModelResponse("ft", (it.conversionValue.trim().toDouble()*3280.84).toString()))
                                unitActivityList.add(UnitActivityModelResponse("inch", (it.conversionValue.trim().toDouble()*39370.1 ).toString()))



                            } else {
                                unitActivityList.add(UnitActivityModelResponse("mile", ""))
                                unitActivityList.add(UnitActivityModelResponse("m", ""))
                                unitActivityList.add(UnitActivityModelResponse("dm", ""))
                                unitActivityList.add(UnitActivityModelResponse("cm", ""))
                                unitActivityList.add(UnitActivityModelResponse("mm", ""))
                                unitActivityList.add(UnitActivityModelResponse("ft", ""))
                                unitActivityList.add(UnitActivityModelResponse("inch", ""))

                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@LengthActivity)
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


                        viewModel.weightResponse.observe(this@LengthActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("km", (it.conversionValue.trim().toDouble() *1.60934).toString()))
                                unitActivityList.add(UnitActivityModelResponse("m", (it.conversionValue.trim().toDouble()*1609.34 ).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dm", (it.conversionValue.trim().toDouble() *16093.4).toString()))
                                unitActivityList.add(UnitActivityModelResponse("cm", (it.conversionValue.trim().toDouble()*160934).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mm", (it.conversionValue.trim().toDouble() * 1609344).toString()))
                                unitActivityList.add(UnitActivityModelResponse("ft", (it.conversionValue.trim().toDouble()*5280).toString()))
                                unitActivityList.add(UnitActivityModelResponse("inch", (it.conversionValue.trim().toDouble()*63360 ).toString()))

                            } else {
                                unitActivityList.add(UnitActivityModelResponse("km", ""))
                                unitActivityList.add(UnitActivityModelResponse("m", ""))
                                unitActivityList.add(UnitActivityModelResponse("dm", ""))
                                unitActivityList.add(UnitActivityModelResponse("cm", ""))
                                unitActivityList.add(UnitActivityModelResponse("mm", ""))
                                unitActivityList.add(UnitActivityModelResponse("ft", ""))
                                unitActivityList.add(UnitActivityModelResponse("inch", ""))


                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@LengthActivity)
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


                        viewModel.weightResponse.observe(this@LengthActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("km", (it.conversionValue.trim().toDouble() *0.001).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mile", (it.conversionValue.trim().toDouble()*0.00063 ).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dm", (it.conversionValue.trim().toDouble() *10).toString()))
                                unitActivityList.add(UnitActivityModelResponse("cm", (it.conversionValue.trim().toDouble()*100).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mm", (it.conversionValue.trim().toDouble() * 1000).toString()))
                                unitActivityList.add(UnitActivityModelResponse("ft", (it.conversionValue.trim().toDouble()*3.28084).toString()))
                                unitActivityList.add(UnitActivityModelResponse("inch", (it.conversionValue.trim().toDouble()*39.37008 ).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("km", ""))
                                unitActivityList.add(UnitActivityModelResponse("mile", ""))
                                unitActivityList.add(UnitActivityModelResponse("dm", ""))
                                unitActivityList.add(UnitActivityModelResponse("cm", ""))
                                unitActivityList.add(UnitActivityModelResponse("mm", ""))
                                unitActivityList.add(UnitActivityModelResponse("ft", ""))
                                unitActivityList.add(UnitActivityModelResponse("inch", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@LengthActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)


                        }



                    }else if(layoutPosition == 3){
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


                        viewModel.weightResponse.observe(this@LengthActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("km", (it.conversionValue.trim().toDouble() *0.0001).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mile", (it.conversionValue.trim().toDouble()*6.2137e-5 ).toString()))
                                unitActivityList.add(UnitActivityModelResponse("m", (it.conversionValue.trim().toDouble() *0.1).toString()))
                                unitActivityList.add(UnitActivityModelResponse("cm", (it.conversionValue.trim().toDouble()*10).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mm", (it.conversionValue.trim().toDouble() *100).toString()))
                                unitActivityList.add(UnitActivityModelResponse("ft", (it.conversionValue.trim().toDouble()*0.328084).toString()))
                                unitActivityList.add(UnitActivityModelResponse("inch", (it.conversionValue.trim().toDouble()*3.937008).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("km", ""))
                                unitActivityList.add(UnitActivityModelResponse("mile", ""))
                                unitActivityList.add(UnitActivityModelResponse("m", ""))
                                unitActivityList.add(UnitActivityModelResponse("cm", ""))
                                unitActivityList.add(UnitActivityModelResponse("mm", ""))
                                unitActivityList.add(UnitActivityModelResponse("ft", ""))
                                unitActivityList.add(UnitActivityModelResponse("inch", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@LengthActivity)
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


                        viewModel.weightResponse.observe(this@LengthActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("km", (it.conversionValue.trim().toDouble() *0.00001).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mile", (it.conversionValue.trim().toDouble()*6.21371e-6).toString()))
                                unitActivityList.add(UnitActivityModelResponse("m", (it.conversionValue.trim().toDouble() *0.01).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dm", (it.conversionValue.trim().toDouble()*.10).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mm", (it.conversionValue.trim().toDouble() *10).toString()))
                                unitActivityList.add(UnitActivityModelResponse("ft", (it.conversionValue.trim().toDouble()*0.0328084).toString()))
                                unitActivityList.add(UnitActivityModelResponse("inch", (it.conversionValue.trim().toDouble()*0.393701).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("km", ""))
                                unitActivityList.add(UnitActivityModelResponse("mile", ""))
                                unitActivityList.add(UnitActivityModelResponse("m", ""))
                                unitActivityList.add(UnitActivityModelResponse("dm", ""))
                                unitActivityList.add(UnitActivityModelResponse("mm", ""))
                                unitActivityList.add(UnitActivityModelResponse("ft", ""))
                                unitActivityList.add(UnitActivityModelResponse("inch", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@LengthActivity)
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


                        viewModel.weightResponse.observe(this@LengthActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("km", (it.conversionValue.trim().toDouble() *1e-6).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mile", (it.conversionValue.trim().toDouble()*6.2137e-7).toString()))
                                unitActivityList.add(UnitActivityModelResponse("m", (it.conversionValue.trim().toDouble() *0.001).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dm", (it.conversionValue.trim().toDouble()*0.01).toString()))
                                unitActivityList.add(UnitActivityModelResponse("cm", (it.conversionValue.trim().toDouble() *0.1).toString()))
                                unitActivityList.add(UnitActivityModelResponse("ft", (it.conversionValue.trim().toDouble()*0.00328084).toString()))
                                unitActivityList.add(UnitActivityModelResponse("inch", (it.conversionValue.trim().toDouble()*0.0393701).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("km", ""))
                                unitActivityList.add(UnitActivityModelResponse("mile", ""))
                                unitActivityList.add(UnitActivityModelResponse("m", ""))
                                unitActivityList.add(UnitActivityModelResponse("dm", ""))
                                unitActivityList.add(UnitActivityModelResponse("cm", ""))
                                unitActivityList.add(UnitActivityModelResponse("ft", ""))
                                unitActivityList.add(UnitActivityModelResponse("inch", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@LengthActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)


                        }



                    }else if(layoutPosition == 6){
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


                        viewModel.weightResponse.observe(this@LengthActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("km", (it.conversionValue.trim().toDouble() *0.0003048).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mile", (it.conversionValue.trim().toDouble()*0.000189394).toString()))
                                unitActivityList.add(UnitActivityModelResponse("m", (it.conversionValue.trim().toDouble() *0.3048).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dm", (it.conversionValue.trim().toDouble()*3.048).toString()))
                                unitActivityList.add(UnitActivityModelResponse("cm", (it.conversionValue.trim().toDouble() *30.48).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mm", (it.conversionValue.trim().toDouble()*304.8).toString()))
                                unitActivityList.add(UnitActivityModelResponse("inch", (it.conversionValue.trim().toDouble()*12).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("km", ""))
                                unitActivityList.add(UnitActivityModelResponse("mile", ""))
                                unitActivityList.add(UnitActivityModelResponse("m", ""))
                                unitActivityList.add(UnitActivityModelResponse("dm", ""))
                                unitActivityList.add(UnitActivityModelResponse("cm", ""))
                                unitActivityList.add(UnitActivityModelResponse("mm", ""))
                                unitActivityList.add(UnitActivityModelResponse("inch", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@LengthActivity)
                            binding.unitRecycler.adapter = unitActivityAdapter
                            unitActivityAdapter?.setItems(unitActivityList)


                        }



                    }else if(layoutPosition == 7){
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


                        viewModel.weightResponse.observe(this@LengthActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("km", (it.conversionValue.trim().toDouble() *2.54e-5).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mile", (it.conversionValue.trim().toDouble()*1.5783e-5).toString()))
                                unitActivityList.add(UnitActivityModelResponse("m", (it.conversionValue.trim().toDouble() *0.025400276352).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dm", (it.conversionValue.trim().toDouble()*0.254).toString()))
                                unitActivityList.add(UnitActivityModelResponse("cm", (it.conversionValue.trim().toDouble() *2.54).toString()))
                                unitActivityList.add(UnitActivityModelResponse("mm", (it.conversionValue.trim().toDouble()*25.4).toString()))
                                unitActivityList.add(UnitActivityModelResponse("ft", (it.conversionValue.trim().toDouble()*0.0833333).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("km", ""))
                                unitActivityList.add(UnitActivityModelResponse("mile", ""))
                                unitActivityList.add(UnitActivityModelResponse("m", ""))
                                unitActivityList.add(UnitActivityModelResponse("dm", ""))
                                unitActivityList.add(UnitActivityModelResponse("cm", ""))
                                unitActivityList.add(UnitActivityModelResponse("mm", ""))
                                unitActivityList.add(UnitActivityModelResponse("ft", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@LengthActivity)
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