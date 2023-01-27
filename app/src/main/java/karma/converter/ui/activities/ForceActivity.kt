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
import karma.converter.api.requestmodel.UnititemModel
import karma.converter.base.BaseActivity
import karma.converter.databinding.ActivityAreaBinding
import karma.converter.listeners.ItemClickListener
import karma.converter.viewmodel.WeightViewModel
import java.util.*
import kotlin.collections.ArrayList

class ForceActivity: BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitActivityModelResponse> {


    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding:ActivityAreaBinding
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

        binding = ActivityAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this
        binding.header.backToHome.setOnClickListener(this)
        binding.header.shareImageView.setOnClickListener(this)
        binding.header.toolbar.text = resources.getString(R.string.force)
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

                unitActivityList.add(UnitActivityModelResponse("dvn", (it.conversionValue.trim().toDouble()*100000).toString()))
                unitActivityList.add(UnitActivityModelResponse("gf", (it.conversionValue.trim().toDouble() * 101.9716212978).toString()))
                unitActivityList.add(UnitActivityModelResponse("pdl", (it.conversionValue.trim().toDouble()*	7.2330138512).toString()))
                unitActivityList.add(UnitActivityModelResponse("lbf", (it.conversionValue.trim().toDouble() *0.224809).toString()))
                unitActivityList.add(UnitActivityModelResponse("Kgf", (it.conversionValue.trim().toDouble()*0.101972).toString()))


            } else {
                unitActivityList.add(UnitActivityModelResponse("dvn", ""))
                unitActivityList.add(UnitActivityModelResponse("gf", ""))
                unitActivityList.add(UnitActivityModelResponse("pdl", ""))
                unitActivityList.add(UnitActivityModelResponse("lbf", ""))
                unitActivityList.add(UnitActivityModelResponse("Kgf", ""))
            }

            unitActivityAdapter?.clear()
            unitActivityAdapter?.setClickListener(this)
            binding.unitRecycler.adapter = unitActivityAdapter
            unitActivityAdapter?.setItems(unitActivityList)


        }

        setPicker()

    }


    private fun setPicker() {
        data.add("N")
        data.add("dvn")
        data.add("gf")
        data.add("pdl")
        data.add("lbf")
        data.add("Kgf")

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


                        viewModel.weightResponse.observe(this@ForceActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("dvn", (it.conversionValue.trim().toDouble()*100000).toString()))
                                unitActivityList.add(UnitActivityModelResponse("gf", (it.conversionValue.trim().toDouble() * 101.9716212978).toString()))
                                unitActivityList.add(UnitActivityModelResponse("pdl", (it.conversionValue.trim().toDouble()*	7.2330138512).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf", (it.conversionValue.trim().toDouble() *0.224809).toString()))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", (it.conversionValue.trim().toDouble()*0.101972).toString()))
                            } else {
                                unitActivityList.add(UnitActivityModelResponse("dvn", ""))
                                unitActivityList.add(UnitActivityModelResponse("gf", ""))
                                unitActivityList.add(UnitActivityModelResponse("pdl", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf", ""))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@ForceActivity)
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


                        viewModel.weightResponse.observe(this@ForceActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("N", (it.conversionValue.trim().toDouble()*1e-5).toString()))
                                unitActivityList.add(UnitActivityModelResponse("gf", (it.conversionValue.trim().toDouble() * 0.0010197162).toString()))
                                unitActivityList.add(UnitActivityModelResponse("pdl", (it.conversionValue.trim().toDouble()*		7.23301E-5).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf", (it.conversionValue.trim().toDouble() *2.24809e-6).toString()))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", (it.conversionValue.trim().toDouble()*1.01972e-6).toString()))
                            } else {
                                unitActivityList.add(UnitActivityModelResponse("N", ""))
                                unitActivityList.add(UnitActivityModelResponse("gf", ""))
                                unitActivityList.add(UnitActivityModelResponse("pdl", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf", ""))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", ""))
                            }
                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@ForceActivity)
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


                        viewModel.weightResponse.observe(this@ForceActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("N", (it.conversionValue.trim().toDouble()*0.0098).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dvn", (it.conversionValue.trim().toDouble() * 980).toString()))
                                unitActivityList.add(UnitActivityModelResponse("pdl", (it.conversionValue.trim().toDouble()*0.0709316352839636).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf", (it.conversionValue.trim().toDouble() *0.002205).toString()))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", (it.conversionValue.trim().toDouble()*0.001).toString()))
                            } else {
                                unitActivityList.add(UnitActivityModelResponse("N", ""))
                                unitActivityList.add(UnitActivityModelResponse("dvn", ""))
                                unitActivityList.add(UnitActivityModelResponse("pdl", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf", ""))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@ForceActivity)
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


                        viewModel.weightResponse.observe(this@ForceActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("N", (it.conversionValue.trim().toDouble()*0.138254954376).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dvn", (it.conversionValue.trim().toDouble() * 13825.5).toString()))
                                unitActivityList.add(UnitActivityModelResponse("gf", (it.conversionValue.trim().toDouble()* 14.098082).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf", (it.conversionValue.trim().toDouble() * 0.031081).toString()))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", (it.conversionValue.trim().toDouble()* 0.0140980819 ).toString()))

                            } else {

                                unitActivityList.add(UnitActivityModelResponse("N", ""))
                                unitActivityList.add(UnitActivityModelResponse("dvn", ""))
                                unitActivityList.add(UnitActivityModelResponse("gf", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf", ""))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@ForceActivity)
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


                        viewModel.weightResponse.observe(this@ForceActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("N", (it.conversionValue.trim().toDouble()*4.44822).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dvn", (it.conversionValue.trim().toDouble() * 444822).toString()))
                                unitActivityList.add(UnitActivityModelResponse("gf", (it.conversionValue.trim().toDouble()* 453.5923699994).toString()))
                                unitActivityList.add(UnitActivityModelResponse("pdl", (it.conversionValue.trim().toDouble() * 32.17).toString()))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", (it.conversionValue.trim().toDouble()* 0.453592 ).toString()))


                            } else {
                                unitActivityList.add(UnitActivityModelResponse("N", ""))
                                unitActivityList.add(UnitActivityModelResponse("dvn", ""))
                                unitActivityList.add(UnitActivityModelResponse("gf", ""))
                                unitActivityList.add(UnitActivityModelResponse("pdl", ""))
                                unitActivityList.add(UnitActivityModelResponse("Kgf", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@ForceActivity)
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


                        viewModel.weightResponse.observe(this@ForceActivity) {

                            unitActivityList.clear()
                            if (it.conversionValue != "") {
                                unitActivityList.add(UnitActivityModelResponse("N", (it.conversionValue.trim().toDouble()*9.80665).toString()))
                                unitActivityList.add(UnitActivityModelResponse("dvn", (it.conversionValue.trim().toDouble() *980665).toString()))
                                unitActivityList.add(UnitActivityModelResponse("gf", (it.conversionValue.trim().toDouble()*1000).toString()))
                                unitActivityList.add(UnitActivityModelResponse("pdl", (it.conversionValue.trim().toDouble() *70.931637).toString()))
                                unitActivityList.add(UnitActivityModelResponse("lbf", (it.conversionValue.trim().toDouble()*2.20462 ).toString()))

                            } else {
                                unitActivityList.add(UnitActivityModelResponse("N", ""))
                                unitActivityList.add(UnitActivityModelResponse("dvn", ""))
                                unitActivityList.add(UnitActivityModelResponse("gf", ""))
                                unitActivityList.add(UnitActivityModelResponse("pdl", ""))
                                unitActivityList.add(UnitActivityModelResponse("lbf", ""))
                            }

                            unitActivityAdapter?.clear()
                            unitActivityAdapter?.setClickListener(this@ForceActivity)
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


                        viewModel.weightResponse.observe(this@ForceActivity) {

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
                            unitActivityAdapter?.setClickListener(this@ForceActivity)
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


                        viewModel.weightResponse.observe(this@ForceActivity) {

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
                            unitActivityAdapter?.setClickListener(this@ForceActivity)
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