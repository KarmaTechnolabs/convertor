package karma.converter.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import karma.converter.BuildConfig
import karma.converter.R
import karma.converter.adapter.PickerAdapter
import karma.converter.adapter.PickerLayoutManager
import karma.converter.adapter.ScreenUtils
import karma.converter.adapter.UnitActivityAdpater
import karma.converter.api.requestmodel.UnitActivityModelResponse
import karma.converter.api.requestmodel.UnitItemModel
import karma.converter.base.BaseActivity
import karma.converter.databinding.ActivityResistanceBinding
import karma.converter.listeners.ItemClickListener
import karma.converter.viewmodel.WeightViewModel

class ResistanceActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitActivityModelResponse> {

    private lateinit var binding : ActivityResistanceBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnitItemModel>()
    private val viewModel by viewModels<WeightViewModel>()
    var unitActivityList = java.util.ArrayList<UnitActivityModelResponse>()
    private val data = ArrayList<String>()
    private lateinit var rvHorizontalPicker: RecyclerView
    private lateinit var sliderAdapter: PickerAdapter
    private var unitActivityAdapter: UnitActivityAdpater? = UnitActivityAdpater(this)
    var lastNumeric: Boolean = false
    var stateError: Boolean = false
    var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResistanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clickListener = this

        binding.header.backToHome.setOnClickListener(this)
        binding.header.shareImageView.setOnClickListener(this)

        binding.header.toolbar.text = resources.getString(R.string.resistance)

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
                unitActivityList.add(UnitActivityModelResponse("MegOhm", (it.conversionValue.trim().toDouble() * 0.000001).toString()))
                unitActivityList.add(UnitActivityModelResponse("MicOhm", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                unitActivityList.add(UnitActivityModelResponse("V/A", (it.conversionValue.trim().toDouble() * 1).toString()))
                unitActivityList.add(UnitActivityModelResponse("1/S", (it.conversionValue.trim().toDouble() * 1).toString()))
                unitActivityList.add(UnitActivityModelResponse("AbOhm", (it.conversionValue.trim().toDouble() * 1.0E-9).toString()))

            } else {
                unitActivityList.add(UnitActivityModelResponse("MegOhm", ""))
                unitActivityList.add(UnitActivityModelResponse("MicOhm", ""))
                unitActivityList.add(UnitActivityModelResponse("V/A", ""))
                unitActivityList.add(UnitActivityModelResponse("1/S", ""))
                unitActivityList.add(UnitActivityModelResponse("AbOhm", ""))
            }

            unitActivityAdapter?.clear()
            unitActivityAdapter?.setClickListener(this)
            binding.unitRecycler.adapter = unitActivityAdapter
            unitActivityAdapter?.setItems(unitActivityList)


        }

        setPicker()

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.header.backToHome -> {
                onBackPressedDispatcher.onBackPressed()
            }

            binding.header.shareImageView -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val app_url =
                    resources.getString(R.string.whatsapp_sharemessages) + BuildConfig.APPLICATION_ID
                shareIntent.putExtra(Intent.EXTRA_TEXT, app_url)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }

        }
    }

    override fun onItemClick(viewIdRes: Int, model: UnitActivityModelResponse, position: Int) {

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

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            binding.inputValue.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            binding.inputValue.append((view as Button).text)
            lastNumeric = false
            lastDot = false    // Reset the DOT flag
        }
    }

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

    private fun setPicker() {
        data.add("Ohm")
        data.add("MegOhm")
        data.add("MicOhm")
        data.add("V/A")
        data.add("1/S")
        data.add("AbOhm")




        rvHorizontalPicker = binding.rvHorizontalPicker

        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(this) / 2 - ScreenUtils.dpToPx(this, 65)
        rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        rvHorizontalPicker.layoutManager = PickerLayoutManager(this).apply {
            callback = object : PickerLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    sliderAdapter.setSelectedItem(layoutPosition)

                    when (layoutPosition) {
                        0 -> {

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


                            viewModel.weightResponse.observe(this@ResistanceActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", (it.conversionValue.trim().toDouble() * 0.000001).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", (it.conversionValue.trim().toDouble() * 1).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", (it.conversionValue.trim().toDouble() * 1).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", (it.conversionValue.trim().toDouble() * 1.0E-9).toString()))

                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", ""))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", ""))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", ""))
                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@ResistanceActivity)
                                binding.unitRecycler.adapter = unitActivityAdapter
                                unitActivityAdapter?.setItems(unitActivityList)


                            }

                        }
                        1 -> {

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


                            viewModel.weightResponse.observe(this@ResistanceActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", (it.conversionValue.trim().toDouble() * 1000000000000).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", (it.conversionValue.trim().toDouble() * 1000000000000000).toString()))

                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", ""))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", ""))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", ""))
                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@ResistanceActivity)
                                binding.unitRecycler.adapter = unitActivityAdapter
                                unitActivityAdapter?.setItems(unitActivityList)
                            }

                        }
                        2 -> {
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


                            viewModel.weightResponse.observe(this@ResistanceActivity) {


                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", (it.conversionValue.trim().toDouble() * 0.000001).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", (it.conversionValue.trim().toDouble() * 1.0E-12).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", (it.conversionValue.trim().toDouble() * 0.000001).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", (it.conversionValue.trim().toDouble() * 0.000001).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", (it.conversionValue.trim().toDouble() * 1000).toString()))

                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", ""))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", ""))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", ""))
                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@ResistanceActivity)
                                binding.unitRecycler.adapter = unitActivityAdapter
                                unitActivityAdapter?.setItems(unitActivityList)


                            }


                        }
                        3 -> {

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


                            viewModel.weightResponse.observe(this@ResistanceActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", (it.conversionValue.trim().toDouble() * 1).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", (it.conversionValue.trim().toDouble() * 0.000001).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", (it.conversionValue.trim().toDouble() * 1).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", (it.conversionValue.trim().toDouble() * 1000000000).toString()))

                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", ""))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", ""))
                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@ResistanceActivity)
                                binding.unitRecycler.adapter = unitActivityAdapter
                                unitActivityAdapter?.setItems(unitActivityList)


                            }

                        }
                        4 -> {

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


                            viewModel.weightResponse.observe(this@ResistanceActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", (it.conversionValue.trim().toDouble() * 1).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", (it.conversionValue.trim().toDouble() * 0.000001).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", (it.conversionValue.trim().toDouble() * 1000000).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", (it.conversionValue.trim().toDouble() * 1).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", (it.conversionValue.trim().toDouble() * 1000000000).toString()))

                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", ""))
                                    unitActivityList.add(UnitActivityModelResponse("AbOhm", ""))
                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@ResistanceActivity)
                                binding.unitRecycler.adapter = unitActivityAdapter
                                unitActivityAdapter?.setItems(unitActivityList)


                            }

                        }
                        5 -> {


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


                            viewModel.weightResponse.observe(this@ResistanceActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", (it.conversionValue.trim().toDouble() * 1.0E-9).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", (it.conversionValue.trim().toDouble() * 1.0E-15).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", (it.conversionValue.trim().toDouble() * 0.001).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", (it.conversionValue.trim().toDouble() * 1.0E-9).toString()))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", (it.conversionValue.trim().toDouble() * 1.0E-9).toString()))

                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("Ohm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MegOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("MicOhm", ""))
                                    unitActivityList.add(UnitActivityModelResponse("V/A", ""))
                                    unitActivityList.add(UnitActivityModelResponse("1/S", ""))
                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@ResistanceActivity)
                                binding.unitRecycler.adapter = unitActivityAdapter
                                unitActivityAdapter?.setItems(unitActivityList)

                            }

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

}