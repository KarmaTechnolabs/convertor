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
import karma.convertor.databinding.ActivityWeightBinding
import karma.convertor.listeners.ItemClickListener
import karma.convertor.viewmodel.WeightViewModel
import kotlinx.android.synthetic.main.appbar.view.*
import java.util.*
import kotlin.collections.ArrayList

class WeightActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitActivityModelResponse> {


    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityWeightBinding
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

        binding = ActivityWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this


        binding.header.back_to_home.setOnClickListener(this)
        binding.header.share_imageView.setOnClickListener(this)

        binding.header.toolbar.setText(resources.getString(R.string.weight))

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
                unitActivityList.add(UnitActivityModelResponse("GRAM", (it.conversionValue.trim().toDouble() * 1000).toString()))
                unitActivityList.add(UnitActivityModelResponse("MILIGRAM", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                unitActivityList.add(UnitActivityModelResponse("POUND", (it.conversionValue.trim().toDouble() * 2.20462).toString()))
                unitActivityList.add(UnitActivityModelResponse("OUNCE", (it.conversionValue.trim().toDouble() * 35.274).toString()))
                unitActivityList.add(UnitActivityModelResponse("TON", (it.conversionValue.trim().toDouble() * 0.00110231).toString()))

            } else {
                unitActivityList.add(UnitActivityModelResponse("GRAM", ""))
                unitActivityList.add(UnitActivityModelResponse("MILIGRAM", ""))
                unitActivityList.add(UnitActivityModelResponse("POUND", ""))
                unitActivityList.add(UnitActivityModelResponse("OUNCE", ""))
                unitActivityList.add(UnitActivityModelResponse("TON", ""))
            }

            unitActivityAdapter?.clear()
            unitActivityAdapter?.setClickListener(this)
            binding.unitRecycler.adapter = unitActivityAdapter
            unitActivityAdapter?.setItems(unitActivityList)


        }

        setPicker()

    }


    private fun setPicker() {
        data.add("KG")
        data.add("Gram")
        data.add("MG")
        data.add("Pound")
        data.add("Ounce")
        data.add("Ton")




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


                     viewModel.weightResponse.observe(this@WeightActivity) {

                         unitActivityList.clear()
                         if (it.conversionValue != "") {
                             unitActivityList.add(UnitActivityModelResponse("GRAM", (it.conversionValue.trim().toDouble() * 1000).toString()))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                             unitActivityList.add(UnitActivityModelResponse("POUND", (it.conversionValue.trim().toDouble() * 2.20462).toString()))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", (it.conversionValue.trim().toDouble() * 35.274).toString()))
                             unitActivityList.add(UnitActivityModelResponse("TON", (it.conversionValue.trim().toDouble() * 0.00110231).toString()))


                         } else {
                             unitActivityList.add(UnitActivityModelResponse("GRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("POUND", ""))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", ""))
                             unitActivityList.add(UnitActivityModelResponse("TON", ""))
                         }

                         unitActivityAdapter?.clear()
                         unitActivityAdapter?.setClickListener(this@WeightActivity)
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


                     viewModel.weightResponse.observe(this@WeightActivity) {

                         unitActivityList.clear()
                         if (it.conversionValue != "") {
                             unitActivityList.add(UnitActivityModelResponse("KG", (it.conversionValue.trim().toDouble() * 0.001).toString()))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", (it.conversionValue.trim().toDouble() * 1000).toString()))
                             unitActivityList.add(UnitActivityModelResponse("POUND", (it.conversionValue.trim().toDouble() * 0.00220462).toString()))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", (it.conversionValue.trim().toDouble() * 0.035274).toString()))
                             unitActivityList.add(UnitActivityModelResponse("TON", (it.conversionValue.trim().toDouble() * 0.000001).toString()))

                         } else {
                             unitActivityList.add(UnitActivityModelResponse("KG", ""))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("POUND", ""))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", ""))
                             unitActivityList.add(UnitActivityModelResponse("TON", ""))

                         }

                         unitActivityAdapter?.clear()
                         unitActivityAdapter?.setClickListener(this@WeightActivity)
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


                     viewModel.weightResponse.observe(this@WeightActivity) {

                         unitActivityList.clear()
                         if (it.conversionValue != "") {
                             unitActivityList.add(UnitActivityModelResponse("KG", (it.conversionValue.trim().toDouble() * 0.000001 ).toString()))
                             unitActivityList.add(UnitActivityModelResponse("GRAM", (it.conversionValue.trim().toDouble() * 0.001).toString()))
                             unitActivityList.add(UnitActivityModelResponse("POUND", (it.conversionValue.trim().toDouble() *  0.0000022046).toString()))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", (it.conversionValue.trim().toDouble() * 0.000035274).toString()))
                             unitActivityList.add(UnitActivityModelResponse("TON", (it.conversionValue.trim().toDouble() *  0.000000001).toString()))

                         } else {

                             unitActivityList.add(UnitActivityModelResponse("KG", ""))
                             unitActivityList.add(UnitActivityModelResponse("GRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("POUND", ""))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", ""))
                             unitActivityList.add(UnitActivityModelResponse("TON", ""))
                         }

                         unitActivityAdapter?.clear()
                         unitActivityAdapter?.setClickListener(this@WeightActivity)
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


                     viewModel.weightResponse.observe(this@WeightActivity) {

                         unitActivityList.clear()
                         if (it.conversionValue != "") {
                             unitActivityList.add(UnitActivityModelResponse("KG", (it.conversionValue.trim().toDouble() * 0.453592 ).toString()))
                             unitActivityList.add(UnitActivityModelResponse("GRAM", (it.conversionValue.trim().toDouble() * 453.592).toString()))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", (it.conversionValue.trim().toDouble() *  453592).toString()))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", (it.conversionValue.trim().toDouble() * 16).toString()))
                             unitActivityList.add(UnitActivityModelResponse("TON", (it.conversionValue.trim().toDouble() *  0.0005).toString()))

                         } else {

                             unitActivityList.add(UnitActivityModelResponse("KG", ""))
                             unitActivityList.add(UnitActivityModelResponse("GRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", ""))
                             unitActivityList.add(UnitActivityModelResponse("TON", ""))
                         }

                         unitActivityAdapter?.clear()
                         unitActivityAdapter?.setClickListener(this@WeightActivity)
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


                     viewModel.weightResponse.observe(this@WeightActivity) {

                         unitActivityList.clear()
                         if (it.conversionValue != "") {
                             unitActivityList.add(UnitActivityModelResponse("KG", (it.conversionValue.trim().toDouble() * 0.0283495 ).toString()))
                             unitActivityList.add(UnitActivityModelResponse("GRAM", (it.conversionValue.trim().toDouble() * 28.3495).toString()))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", (it.conversionValue.trim().toDouble() *  28349.5).toString()))
                             unitActivityList.add(UnitActivityModelResponse("POUND", (it.conversionValue.trim().toDouble() * 0.0625).toString()))
                             unitActivityList.add(UnitActivityModelResponse("TON", (it.conversionValue.trim().toDouble() *  0.000028349523).toString()))

                         } else {

                             unitActivityList.add(UnitActivityModelResponse("KG", ""))
                             unitActivityList.add(UnitActivityModelResponse("GRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("POUND", ""))
                             unitActivityList.add(UnitActivityModelResponse("TON", ""))
                         }

                         unitActivityAdapter?.clear()
                         unitActivityAdapter?.setClickListener(this@WeightActivity)
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


                     viewModel.weightResponse.observe(this@WeightActivity) {

                         unitActivityList.clear()
                         if (it.conversionValue != "") {
                             unitActivityList.add(UnitActivityModelResponse("KG", (it.conversionValue.trim().toDouble() * 1000 ).toString()))
                             unitActivityList.add(UnitActivityModelResponse("GRAM", (it.conversionValue.trim().toDouble() *1000000).toString()))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", (it.conversionValue.trim().toDouble() *  1000000000).toString()))
                             unitActivityList.add(UnitActivityModelResponse("POUND", (it.conversionValue.trim().toDouble() * 2000).toString()))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", (it.conversionValue.trim().toDouble() * 32000).toString()))

                         } else {

                             unitActivityList.add(UnitActivityModelResponse("KG", ""))
                             unitActivityList.add(UnitActivityModelResponse("GRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("MILIGRAM", ""))
                             unitActivityList.add(UnitActivityModelResponse("POUND", ""))
                             unitActivityList.add(UnitActivityModelResponse("OUNCE", ""))
                         }

                         unitActivityAdapter?.clear()
                         unitActivityAdapter?.setClickListener(this@WeightActivity)
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
            binding.header.back_to_home -> {

                gotoActivity(MainActivity::class.java)

            }

            binding.header.share_imageView -> {


                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val app_url = resources.getString(R.string.whatsapp_sharemessages) + BuildConfig.APPLICATION_ID
                shareIntent.putExtra(Intent.EXTRA_TEXT, app_url)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }

            binding.btnClear -> {

                gotoActivity(MainActivity::class.java)

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