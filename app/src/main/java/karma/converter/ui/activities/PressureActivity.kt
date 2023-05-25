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
import karma.converter.databinding.ActivityAreaBinding
import karma.converter.listeners.ItemClickListener
import karma.converter.viewmodel.WeightViewModel
import java.util.*
import kotlin.collections.ArrayList

class PressureActivity: BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitActivityModelResponse> {


    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding:ActivityAreaBinding
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

        binding = ActivityAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this


        binding.header.backToHome.setOnClickListener(this)
        binding.header.shareImageView.setOnClickListener(this)

        binding.header.toolbar.text = resources.getString(R.string.pressure)

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
                unitActivityList.add(UnitActivityModelResponse("bar", (it.conversionValue.trim().toDouble() *1e-5).toString()))
                unitActivityList.add(UnitActivityModelResponse("at", (it.conversionValue.trim().toDouble()*1.01972e-5).toString()))
                unitActivityList.add(UnitActivityModelResponse("atm", (it.conversionValue.trim().toDouble() * 9.8692e-6).toString()))
                unitActivityList.add(UnitActivityModelResponse("torr", (it.conversionValue.trim().toDouble()*0.00750062).toString()))
                unitActivityList.add(UnitActivityModelResponse("lbf/in²", (it.conversionValue.trim().toDouble() *0.000145038).toString()))



            } else {
                unitActivityList.add(UnitActivityModelResponse("bar", ""))
                unitActivityList.add(UnitActivityModelResponse("at", ""))
                unitActivityList.add(UnitActivityModelResponse("atm", ""))
                unitActivityList.add(UnitActivityModelResponse("torr", ""))
                unitActivityList.add(UnitActivityModelResponse("lbf/in²", ""))


            }

            unitActivityAdapter?.clear()
            unitActivityAdapter?.setClickListener(this)
            binding.unitRecycler.adapter = unitActivityAdapter
            unitActivityAdapter?.setItems(unitActivityList)


        }

        setPicker()

    }


    private fun setPicker() {
        data.add("Pa")
        data.add("bar")
        data.add("at")
        data.add("atm")
        data.add("torr")
        data.add("lbf/in²")








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


                        viewModel.weightResponse.observe(this@PressureActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("bar", (it.conversionValue.trim().toDouble() *1e-5).toString()))
                                unitActivityList.add(UnitActivityModelResponse("at", (it.conversionValue.trim().toDouble()*1.01972e-5).toString()))
                                unitActivityList.add(UnitActivityModelResponse("atm", (it.conversionValue.trim().toDouble() * 9.8692e-6).toString()))
                                unitActivityList.add(UnitActivityModelResponse("torr", (it.conversionValue.trim().toDouble()*0.00750062).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", (it.conversionValue.trim().toDouble() *0.000145038).toString()))
                            } else {
                                unitActivityList.add(UnitActivityModelResponse("bar", ""))
                                unitActivityList.add(UnitActivityModelResponse("at", ""))
                                unitActivityList.add(UnitActivityModelResponse("atm", ""))
                                unitActivityList.add(UnitActivityModelResponse("torr", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@PressureActivity)
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


                        viewModel.weightResponse.observe(this@PressureActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("Pa", (it.conversionValue.trim().toDouble() *100000).toString()))
                                unitActivityList.add(UnitActivityModelResponse("at", (it.conversionValue.trim().toDouble()*1.01972).toString()))
                                unitActivityList.add(UnitActivityModelResponse("atm", (it.conversionValue.trim().toDouble() * 0.986923).toString()))
                                unitActivityList.add(UnitActivityModelResponse("torr", (it.conversionValue.trim().toDouble()*750.062).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", (it.conversionValue.trim().toDouble() *14.5038).toString()))


                            } else {
                                unitActivityList.add(UnitActivityModelResponse("Pa", ""))
                                unitActivityList.add(UnitActivityModelResponse("at", ""))
                                unitActivityList.add(UnitActivityModelResponse("atm", ""))
                                unitActivityList.add(UnitActivityModelResponse("torr", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", ""))


                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@PressureActivity)
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


                        viewModel.weightResponse.observe(this@PressureActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("Pa", (it.conversionValue.trim().toDouble() *98066.5).toString()))
                                unitActivityList.add(UnitActivityModelResponse("bar", (it.conversionValue.trim().toDouble()*0.980665).toString()))
                                unitActivityList.add(UnitActivityModelResponse("atm", (it.conversionValue.trim().toDouble() * 0.967841).toString()))
                                unitActivityList.add(UnitActivityModelResponse("torr", (it.conversionValue.trim().toDouble()*735.559).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", (it.conversionValue.trim().toDouble() *14.2233).toString()))

                            } else {
                                unitActivityList.add(UnitActivityModelResponse("Pa", ""))
                                unitActivityList.add(UnitActivityModelResponse("bar", ""))
                                unitActivityList.add(UnitActivityModelResponse("atm", ""))
                                unitActivityList.add(UnitActivityModelResponse("torr", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@PressureActivity)
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


                        viewModel.weightResponse.observe(this@PressureActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("Pa", (it.conversionValue.trim().toDouble() *101325).toString()))
                                unitActivityList.add(UnitActivityModelResponse("bar", (it.conversionValue.trim().toDouble()*1.01325).toString()))
                                unitActivityList.add(UnitActivityModelResponse("at", (it.conversionValue.trim().toDouble() * 1.03323).toString()))
                                unitActivityList.add(UnitActivityModelResponse("torr", (it.conversionValue.trim().toDouble()*760).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", (it.conversionValue.trim().toDouble() *14.6959).toString()))

                            } else {
                                unitActivityList.add(UnitActivityModelResponse("Pa", ""))
                                unitActivityList.add(UnitActivityModelResponse("bar", ""))
                                unitActivityList.add(UnitActivityModelResponse("at", ""))
                                unitActivityList.add(UnitActivityModelResponse("torr", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", ""))

                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@PressureActivity)
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


                        viewModel.weightResponse.observe(this@PressureActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("Pa", (it.conversionValue.trim().toDouble() *133.322).toString()))
                                unitActivityList.add(UnitActivityModelResponse("bar", (it.conversionValue.trim().toDouble()*0.00133322).toString()))
                                unitActivityList.add(UnitActivityModelResponse("at", (it.conversionValue.trim().toDouble() * 0.00135951).toString()))
                                unitActivityList.add(UnitActivityModelResponse("atm", (it.conversionValue.trim().toDouble()*0.00131579).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", (it.conversionValue.trim().toDouble() *0.0193368).toString()))


                            } else {
                                unitActivityList.add(UnitActivityModelResponse("Pa", ""))
                                unitActivityList.add(UnitActivityModelResponse("bar", ""))
                                unitActivityList.add(UnitActivityModelResponse("at", ""))
                                unitActivityList.add(UnitActivityModelResponse("atm", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf/in²", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@PressureActivity)
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


                        viewModel.weightResponse.observe(this@PressureActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("Pa", (it.conversionValue.trim().toDouble() *6894.76).toString()))
                                unitActivityList.add(UnitActivityModelResponse("bar", (it.conversionValue.trim().toDouble()*0.0689476).toString()))
                                unitActivityList.add(UnitActivityModelResponse("at", (it.conversionValue.trim().toDouble() * 0.070307).toString()))
                                unitActivityList.add(UnitActivityModelResponse("atm", (it.conversionValue.trim().toDouble()*0.068046).toString()))
                                unitActivityList.add(UnitActivityModelResponse("torr", (it.conversionValue.trim().toDouble() *51.7149).toString()))

                            } else {
                                unitActivityList.add(UnitActivityModelResponse("Pa", ""))
                                unitActivityList.add(UnitActivityModelResponse("bar", ""))
                                unitActivityList.add(UnitActivityModelResponse("at", ""))
                                unitActivityList.add(UnitActivityModelResponse("atm", ""))
                                unitActivityList.add(UnitActivityModelResponse("torr", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@PressureActivity)
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


                        viewModel.weightResponse.observe(this@PressureActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("Hectare", (it.conversionValue.trim().toDouble() *6.4516e-8).toString()))
                                unitActivityList.add(UnitActivityModelResponse("Acre", (it.conversionValue.trim().toDouble()*1.5942e-7).toString()))
                                unitActivityList.add(UnitActivityModelResponse("sq km", (it.conversionValue.trim().toDouble() *6.4516e-10).toString()))
                                unitActivityList.add(UnitActivityModelResponse("sq m", (it.conversionValue.trim().toDouble()*0.00064516).toString()))
                                unitActivityList.add(UnitActivityModelResponse("sq yds", (it.conversionValue.trim().toDouble() *0.000771605).toString()))
                                unitActivityList.add(UnitActivityModelResponse("sq ft", (it.conversionValue.trim().toDouble()*0.006944445).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("Hectare", ""))
                                unitActivityList.add(UnitActivityModelResponse("Acre", ""))
                                unitActivityList.add(UnitActivityModelResponse("sq km", ""))
                                unitActivityList.add(UnitActivityModelResponse("sq m", ""))
                                unitActivityList.add(UnitActivityModelResponse("sq yds", ""))
                                unitActivityList.add(UnitActivityModelResponse("sq ft", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@PressureActivity)
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


                        viewModel.weightResponse.observe(this@PressureActivity) {

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
                            unitActivityAdapter?.setClickListener(this@PressureActivity)
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