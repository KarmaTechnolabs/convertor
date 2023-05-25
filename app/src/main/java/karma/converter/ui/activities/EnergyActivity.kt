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
import karma.converter.databinding.ActivityEnergyBinding
import karma.converter.listeners.ItemClickListener
import karma.converter.viewmodel.WeightViewModel

class EnergyActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitActivityModelResponse> {


    private lateinit var binding: ActivityEnergyBinding
    private val viewModel by viewModels<WeightViewModel>()
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnitItemModel>()
    var unitActivityList = ArrayList<UnitActivityModelResponse>()
    private val data = ArrayList<String>()
    private lateinit var rvHorizontalPicker: RecyclerView
    private lateinit var sliderAdapter: PickerAdapter
    private var unitActivityAdapter: UnitActivityAdpater? = UnitActivityAdpater(this)
    var lastNumeric: Boolean = false
    var stateError: Boolean = false
    var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnergyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this


        binding.header.backToHome.setOnClickListener(this)
        binding.header.shareImageView.setOnClickListener(this)

        binding.header.toolbar.text = resources.getString(R.string.energy)

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
                unitActivityList.add(
                    UnitActivityModelResponse(
                        "ton",
                        (it.conversionValue.trim().toDouble() * 2.390057361E-10).toString()
                    )
                )
                unitActivityList.add(
                    UnitActivityModelResponse(
                        "K J",
                        (it.conversionValue.trim().toDouble() * 0.001).toString()
                    )
                )
                unitActivityList.add(
                    UnitActivityModelResponse(
                        "kW*h",
                        (it.conversionValue.trim().toDouble() * 2.777777777E-7).toString()
                    )
                )
                unitActivityList.add(
                    UnitActivityModelResponse(
                        "W*h",
                        (it.conversionValue.trim().toDouble() * 0.0002777778).toString()
                    )
                )
                unitActivityList.add(
                    UnitActivityModelResponse(
                        "Calorie",
                        (it.conversionValue.trim().toDouble() * 0.0002388459).toString()
                    )
                )
                unitActivityList.add(
                    UnitActivityModelResponse(
                        "eV",
                        (it.conversionValue.trim().toDouble() * 6241509074461).toString()
                    )
                )


            } else {
                unitActivityList.add(UnitActivityModelResponse("ton", ""))
                unitActivityList.add(UnitActivityModelResponse("K J", ""))
                unitActivityList.add(UnitActivityModelResponse("kW*h", ""))
                unitActivityList.add(UnitActivityModelResponse("W*h", ""))
                unitActivityList.add(UnitActivityModelResponse("Calorie", ""))
                unitActivityList.add(UnitActivityModelResponse("eV", ""))

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
        //
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


    private fun setPicker() {
        data.add("Joule")
        data.add("K J")
        data.add("kW*h")
        data.add("W*h")
        data.add("Calorie")
        data.add("eV")
        data.add("ton")

        rvHorizontalPicker = binding.rvHorizontalPicker

        val padding: Int = ScreenUtils.getScreenWidth(this) / 2 - ScreenUtils.dpToPx(this, 65)
        rvHorizontalPicker.setPadding(padding, 0, padding, 0)

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

                            viewModel.weightResponse.observe(this@EnergyActivity) {
                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "ton",
                                            (it.conversionValue.trim()
                                                .toDouble() * 2.390057361E-10).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "K J",
                                            (it.conversionValue.trim().toDouble() * 0.001).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "kW*h",
                                            (it.conversionValue.trim()
                                                .toDouble() * 2.777777777E-7).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "W*h",
                                            (it.conversionValue.trim()
                                                .toDouble() * 0.0002777778).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Calorie",
                                            (it.conversionValue.trim()
                                                .toDouble() * 0.0002388459).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "eV",
                                            (it.conversionValue.trim()
                                                .toDouble() * 6241509074461).toString()
                                        )
                                    )


                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("ton", ""))
                                    unitActivityList.add(UnitActivityModelResponse("K J", ""))
                                    unitActivityList.add(UnitActivityModelResponse("kW*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("W*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Calorie", ""))
                                    unitActivityList.add(UnitActivityModelResponse("eV", ""))

                                }
                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@EnergyActivity)
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
                            viewModel.weightResponse.observe(this@EnergyActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "ton",
                                            (it.conversionValue.trim()
                                                .toDouble() * 2.390057361E-7).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Joule",
                                            (it.conversionValue.trim().toDouble() * 1000).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "kW*h",
                                            (it.conversionValue.trim()
                                                .toDouble() * 0.0002778).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "W*h",
                                            (it.conversionValue.trim().toDouble() * 0.2778).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Calorie",
                                            (it.conversionValue.trim().toDouble() * 0.2389).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "eV",
                                            (it.conversionValue.trim()
                                                .toDouble() * 6.241509074E+21).toString()
                                        )
                                    )


                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("ton", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Joule", ""))
                                    unitActivityList.add(UnitActivityModelResponse("kW*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("W*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Calorie", ""))
                                    unitActivityList.add(UnitActivityModelResponse("eV", ""))


                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@EnergyActivity)
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
                            viewModel.weightResponse.observe(this@EnergyActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "ton",
                                            (it.conversionValue.trim()
                                                .toDouble() * 0.0008604207).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Joule",
                                            (it.conversionValue.trim().toDouble() * 3600000).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "K J",
                                            (it.conversionValue.trim()
                                                .toDouble() * 3600).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "W*h",
                                            (it.conversionValue.trim().toDouble() * 1000).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Calorie",
                                            (it.conversionValue.trim().toDouble() * 859.84523).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "eV",
                                            (it.conversionValue.trim()
                                                .toDouble() * 2.246943266E+25).toString()
                                        )
                                    )


                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("ton", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Joule", ""))
                                    unitActivityList.add(UnitActivityModelResponse("K J", ""))
                                    unitActivityList.add(UnitActivityModelResponse("W*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Calorie", ""))
                                    unitActivityList.add(UnitActivityModelResponse("eV", ""))


                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@EnergyActivity)
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
                            viewModel.weightResponse.observe(this@EnergyActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "ton",
                                            (it.conversionValue.trim()
                                                .toDouble() * 8.6042065E-7).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Joule",
                                            (it.conversionValue.trim().toDouble() * 3600).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "K J",
                                            (it.conversionValue.trim()
                                                .toDouble() * 3.6).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "kW*h",
                                            (it.conversionValue.trim().toDouble() * 0.001).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Calorie",
                                            (it.conversionValue.trim().toDouble() * 0.8598452279).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "eV",
                                            (it.conversionValue.trim()
                                                .toDouble() * 2.246943266E+22).toString()
                                        )
                                    )


                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("ton", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Joule", ""))
                                    unitActivityList.add(UnitActivityModelResponse("K J", ""))
                                    unitActivityList.add(UnitActivityModelResponse("kW*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Calorie", ""))
                                    unitActivityList.add(UnitActivityModelResponse("eV", ""))


                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@EnergyActivity)
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
                            viewModel.weightResponse.observe(this@EnergyActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "ton",
                                            (it.conversionValue.trim()
                                                .toDouble() * 0.0000010007).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Joule",
                                            (it.conversionValue.trim().toDouble() * 4186.8).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "K J",
                                            (it.conversionValue.trim()
                                                .toDouble() * 4.1868).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "kW*h",
                                            (it.conversionValue.trim().toDouble() * 0.001163).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "W*h",
                                            (it.conversionValue.trim().toDouble() * 1.163).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "eV",
                                            (it.conversionValue.trim()
                                                .toDouble() * 2.613195019E+22).toString()
                                        )
                                    )


                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("ton", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Joule", ""))
                                    unitActivityList.add(UnitActivityModelResponse("K J", ""))
                                    unitActivityList.add(UnitActivityModelResponse("kW*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("W*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("eV", ""))


                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@EnergyActivity)
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
                            viewModel.weightResponse.observe(this@EnergyActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "ton",
                                            (it.conversionValue.trim()
                                                .toDouble() * 3.829294058E-29).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Joule",
                                            (it.conversionValue.trim().toDouble() * 1.602176633E-19).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "K J",
                                            (it.conversionValue.trim()
                                                .toDouble() * 1.602176633E-22).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "kW*h",
                                            (it.conversionValue.trim().toDouble() * 4.450490649E-26).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "W*h",
                                            (it.conversionValue.trim().toDouble() * 4.450490649E-23).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Calorie",
                                            (it.conversionValue.trim().toDouble() * 3.826733147E-23).toString()
                                        )
                                    )


                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("ton", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Joule", ""))
                                    unitActivityList.add(UnitActivityModelResponse("K J", ""))
                                    unitActivityList.add(UnitActivityModelResponse("kW*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("W*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Calorie", ""))


                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@EnergyActivity)
                                binding.unitRecycler.adapter = unitActivityAdapter
                                unitActivityAdapter?.setItems(unitActivityList)
                            }
                        }
                        6 -> {
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
                            viewModel.weightResponse.observe(this@EnergyActivity) {

                                unitActivityList.clear()
                                if (it.conversionValue != "") {
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Joule",
                                            (it.conversionValue.trim().toDouble() * 4184000000).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "K J",
                                            (it.conversionValue.trim()
                                                .toDouble() * 4184000).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "kW*h",
                                            (it.conversionValue.trim().toDouble() * 1162.22).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "W*h",
                                            (it.conversionValue.trim().toDouble() * 1162222.22).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "Calorie",
                                            (it.conversionValue.trim().toDouble() * 999331.2315).toString()
                                        )
                                    )
                                    unitActivityList.add(
                                        UnitActivityModelResponse(
                                            "eV",
                                            (it.conversionValue.trim()
                                                .toDouble() * 2.611447396E+28).toString()
                                        )
                                    )


                                } else {
                                    unitActivityList.add(UnitActivityModelResponse("Joule", ""))
                                    unitActivityList.add(UnitActivityModelResponse("K J", ""))
                                    unitActivityList.add(UnitActivityModelResponse("kW*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("W*h", ""))
                                    unitActivityList.add(UnitActivityModelResponse("Calorie", ""))
                                    unitActivityList.add(UnitActivityModelResponse("eV", ""))


                                }

                                unitActivityAdapter?.clear()
                                unitActivityAdapter?.setClickListener(this@EnergyActivity)
                                binding.unitRecycler.adapter = unitActivityAdapter
                                unitActivityAdapter?.setItems(unitActivityList)
                            }
                        }
                    }
                }

            }
        }

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