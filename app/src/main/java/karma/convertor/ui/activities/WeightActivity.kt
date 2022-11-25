package karma.convertor.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
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
import net.objecthunter.exp4j.ExpressionBuilder

class WeightActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitActivityModelResponse> {
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityWeightBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnititemModel>()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this
        //  binding.appBarDashboard.back_to_home.setOnClickListener(this)

        setPicker()
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))
        unitActivityList.add(UnitActivityModelResponse("KILOGRAM", "0.000"))




        unitActivityAdapter?.clear()
        unitActivityAdapter?.setClickListener(this)
        binding.unitRecycler.adapter = unitActivityAdapter

        unitActivityAdapter?.setItems(unitActivityList)


        // access the items of the list
        val languages = resources.getStringArray(R.array.Languages)

        // access the spinner

        /*    if (binding.spinner != null) {
                val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, languages)
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>,
                                                view: View, position: Int, id: Long) {


    if(id==0L) {

        val num1 = binding.inputValue.text.toString()
        if (num1.isNotBlank()) {
            val num2 = binding.inputValue.text.toString().trim().toDouble()
            binding.tvUnitPound.text = (num2 /1).toString()
            binding.tvUnitKilo.text = (num2 * 0.45359237).toString()
            binding.tvUnitGram.text = (num2 *453.59237).toString()
            binding.tvUnitMiligram.text = (num2 *453592.37).toString()
            binding.tvUnitMicrogram.text = (num2 * 	453592370 ).toString()
            binding.tvUnitNanogram.text = (num2 * 453592370000).toString()
            binding.tvUnitPicogram.text = (num2 *453592370000000).toString()
            binding.tvUnitTola.text = (num2 * 38.89).toString()
        } else {
            binding.tvUnitPound.text = ""
            binding.tvUnitKilo.text = ""
            binding.tvUnitGram.text = ""
            binding.tvUnitMiligram.text = ""
            binding.tvUnitMicrogram.text = ""
            binding.tvUnitNanogram.text = ""
            binding.tvUnitPicogram.text = ""
            binding.tvUnitTola.text = ""

        }
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
                    binding.tvUnitPound.text = (num2 /1).toString()
                    binding.tvUnitKilo.text = (num2 * 0.45359237).toString()
                    binding.tvUnitGram.text = (num2 *453.59237).toString()
                    binding.tvUnitMiligram.text = (num2 *453592.37).toString()
                    binding.tvUnitMicrogram.text = (num2 * 	453592370 ).toString()
                    binding.tvUnitNanogram.text = (num2 * 453592370000).toString()
                    binding.tvUnitPicogram.text = (num2 *453592370000000).toString()
                    binding.tvUnitTola.text = (num2 * 38.89).toString()
                } else {
                    binding.tvUnitPound.text = ""
                    binding.tvUnitKilo.text = ""
                    binding.tvUnitGram.text = ""
                    binding.tvUnitMiligram.text = ""
                    binding.tvUnitMicrogram.text = ""
                    binding.tvUnitNanogram.text = ""
                    binding.tvUnitPicogram.text = ""
                    binding.tvUnitTola.text = ""

                }


            }

        })


    }else if(id==1L){
        val num1 = binding.inputValue.text.toString()
        if (num1.isNotBlank()) {
            val num2 = binding.inputValue.text.toString().trim().toDouble()
            binding.tvUnitPound.text = (num2 / .453).toString()
            binding.tvUnitKilo.text = (num2 * 1).toString()
            binding.tvUnitGram.text = (num2 * 1000).toString()
            binding.tvUnitMiligram.text = (num2 * 10000).toString()
            binding.tvUnitMicrogram.text = (num2 * 100000).toString()
            binding.tvUnitNanogram.text = (num2 * 1000000).toString()
            binding.tvUnitPicogram.text = (num2 * 10000000).toString()
            binding.tvUnitTola.text = (num2 * 85.735).toString()
        } else {
            binding.tvUnitPound.text = ""
            binding.tvUnitKilo.text = ""
            binding.tvUnitGram.text = ""
            binding.tvUnitMiligram.text = ""
            binding.tvUnitMicrogram.text = ""
            binding.tvUnitNanogram.text = ""
            binding.tvUnitPicogram.text = ""
            binding.tvUnitTola.text = ""

        }
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
                binding.tvUnitPound.text = (num2 / .453).toString()
                binding.tvUnitKilo.text = (num2 * 1).toString()
                binding.tvUnitGram.text = (num2 * 1000).toString()
                binding.tvUnitMiligram.text = (num2 * 10000).toString()
                binding.tvUnitMicrogram.text = (num2 * 100000).toString()
                binding.tvUnitNanogram.text = (num2 * 1000000).toString()
                binding.tvUnitPicogram.text = (num2 * 10000000).toString()
                binding.tvUnitTola.text = (num2 * 85.735).toString()
            } else {
                binding.tvUnitPound.text = ""
                binding.tvUnitKilo.text = ""
                binding.tvUnitGram.text = ""
                binding.tvUnitMiligram.text = ""
                binding.tvUnitMicrogram.text = ""
                binding.tvUnitNanogram.text = ""
                binding.tvUnitPicogram.text = ""
                binding.tvUnitTola.text = ""

            }


        }

    })
    }else if(id==2L) {


        val num1 = binding.inputValue.text.toString()
        if (num1.isNotBlank()) {
            val num2 = binding.inputValue.text.toString().trim().toDouble()
            binding.tvUnitPound.text = (num2 * 0.00220462).toString()
            binding.tvUnitKilo.text = (num2 * 0.001).toString()
            binding.tvUnitGram.text = (num2 * 1).toString()
            binding.tvUnitMiligram.text = (num2 * 1000).toString()
            binding.tvUnitMicrogram.text = (num2 * 10000).toString()
            binding.tvUnitNanogram.text = (num2 * 100000).toString()
            binding.tvUnitPicogram.text = (num2 * 1000000).toString()
            binding.tvUnitTola.text = (num2 / 11.6638038).toString()
        } else {
            binding.tvUnitPound.text = ""
            binding.tvUnitKilo.text = ""
            binding.tvUnitGram.text = ""
            binding.tvUnitMiligram.text = ""
            binding.tvUnitMicrogram.text = ""
            binding.tvUnitNanogram.text = ""
            binding.tvUnitPicogram.text = ""
            binding.tvUnitTola.text = ""

        }
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
                    binding.tvUnitPound.text = (num2 * 0.00220462).toString()
                    binding.tvUnitKilo.text = (num2 * 0.001).toString()
                    binding.tvUnitGram.text = (num2 * 1).toString()
                    binding.tvUnitMiligram.text = (num2 * 1000).toString()
                    binding.tvUnitMicrogram.text = (num2 * 10000).toString()
                    binding.tvUnitNanogram.text = (num2 * 100000).toString()
                    binding.tvUnitPicogram.text = (num2 * 1000000).toString()
                    binding.tvUnitTola.text = (num2 / 11.6638038).toString()
                } else {
                    binding.tvUnitPound.text = ""
                    binding.tvUnitKilo.text = ""
                    binding.tvUnitGram.text = ""
                    binding.tvUnitMiligram.text = ""
                    binding.tvUnitMicrogram.text = ""
                    binding.tvUnitNanogram.text = ""
                    binding.tvUnitPicogram.text = ""
                    binding.tvUnitTola.text = ""

                }


            }

        })

    }else if(id==3L){
        val num1 = binding.inputValue.text.toString()
        if (num1.isNotBlank()) {
            val num2 = binding.inputValue.text.toString().trim().toDouble()
            binding.tvUnitPound.text = (num2 /453592).toString()
            binding.tvUnitKilo.text = (num2 /1000000).toString()
            binding.tvUnitGram.text = (num2 / 1000).toString()
            binding.tvUnitMiligram.text = (num2 * 1).toString()
            binding.tvUnitMicrogram.text = (num2 * 10000).toString()
            binding.tvUnitNanogram.text = (num2 * 100000).toString()
            binding.tvUnitPicogram.text = (num2 * 1000000).toString()
            binding.tvUnitTola.text = (num2 / 11.6638038).toString()
        } else {
            binding.tvUnitPound.text = ""
            binding.tvUnitKilo.text = ""
            binding.tvUnitGram.text = ""
            binding.tvUnitMiligram.text = ""
            binding.tvUnitMicrogram.text = ""
            binding.tvUnitNanogram.text = ""
            binding.tvUnitPicogram.text = ""
            binding.tvUnitTola.text = ""

        }
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
                    binding.tvUnitPound.text = (num2 / 453592).toString()
                    binding.tvUnitKilo.text = (num2 * 0.001).toString()
                    binding.tvUnitGram.text = (num2 * 1).toString()
                    binding.tvUnitMiligram.text = (num2 * 1000).toString()
                    binding.tvUnitMicrogram.text = (num2 * 10000).toString()
                    binding.tvUnitNanogram.text = (num2 * 100000).toString()
                    binding.tvUnitPicogram.text = (num2 * 1000000).toString()
                    binding.tvUnitTola.text = (num2 / 11.6638038).toString()
                } else {
                    binding.tvUnitPound.text = ""
                    binding.tvUnitKilo.text = ""
                    binding.tvUnitGram.text = ""
                    binding.tvUnitMiligram.text = ""
                    binding.tvUnitMicrogram.text = ""
                    binding.tvUnitNanogram.text = ""
                    binding.tvUnitPicogram.text = ""
                    binding.tvUnitTola.text = ""

                }


            }

        })








    } else if(id==4L){

        val num1 = binding.inputValue.text.toString()
        if (num1.isNotBlank()) {
            val num2 = binding.inputValue.text.toString().trim().toDouble()
            binding.tvUnitPound.text = (num2 * 0.00220462).toString()
            binding.tvUnitKilo.text = (num2 * 0.001).toString()
            binding.tvUnitGram.text = (num2 * 1).toString()
            binding.tvUnitMiligram.text = (num2 * 1000).toString()
            binding.tvUnitMicrogram.text = (num2 * 10000).toString()
            binding.tvUnitNanogram.text = (num2 * 100000).toString()
            binding.tvUnitPicogram.text = (num2 * 1000000).toString()
            binding.tvUnitTola.text = (num2 / 11.6638038).toString()
        } else {
            binding.tvUnitPound.text = ""
            binding.tvUnitKilo.text = ""
            binding.tvUnitGram.text = ""
            binding.tvUnitMiligram.text = ""
            binding.tvUnitMicrogram.text = ""
            binding.tvUnitNanogram.text = ""
            binding.tvUnitPicogram.text = ""
            binding.tvUnitTola.text = ""

        }
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
                    binding.tvUnitPound.text = (num2 * 0.00220462).toString()
                    binding.tvUnitKilo.text = (num2 * 0.001).toString()
                    binding.tvUnitGram.text = (num2 * 1).toString()
                    binding.tvUnitMiligram.text = (num2 * 1000).toString()
                    binding.tvUnitMicrogram.text = (num2 * 10000).toString()
                    binding.tvUnitNanogram.text = (num2 * 100000).toString()
                    binding.tvUnitPicogram.text = (num2 * 1000000).toString()
                    binding.tvUnitTola.text = (num2 / 11.6638038).toString()
                } else {
                    binding.tvUnitPound.text = ""
                    binding.tvUnitKilo.text = ""
                    binding.tvUnitGram.text = ""
                    binding.tvUnitMiligram.text = ""
                    binding.tvUnitMicrogram.text = ""
                    binding.tvUnitNanogram.text = ""
                    binding.tvUnitPicogram.text = ""
                    binding.tvUnitTola.text = ""

                }


            }

        })

    }else if(id==5L){

        val num1 = binding.inputValue.text.toString()
        if (num1.isNotBlank()) {
            val num2 = binding.inputValue.text.toString().trim().toDouble()
            binding.tvUnitPound.text = (num2 * 0.00220462).toString()
            binding.tvUnitKilo.text = (num2 * 0.001).toString()
            binding.tvUnitGram.text = (num2 * 1).toString()
            binding.tvUnitMiligram.text = (num2 * 1000).toString()
            binding.tvUnitMicrogram.text = (num2 * 10000).toString()
            binding.tvUnitNanogram.text = (num2 * 100000).toString()
            binding.tvUnitPicogram.text = (num2 * 1000000).toString()
            binding.tvUnitTola.text = (num2 / 11.6638038).toString()
        } else {
            binding.tvUnitPound.text = ""
            binding.tvUnitKilo.text = ""
            binding.tvUnitGram.text = ""
            binding.tvUnitMiligram.text = ""
            binding.tvUnitMicrogram.text = ""
            binding.tvUnitNanogram.text = ""
            binding.tvUnitPicogram.text = ""
            binding.tvUnitTola.text = ""

        }
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
                    binding.tvUnitPound.text = (num2 * 0.00220462).toString()
                    binding.tvUnitKilo.text = (num2 * 0.001).toString()
                    binding.tvUnitGram.text = (num2 * 1).toString()
                    binding.tvUnitMiligram.text = (num2 * 1000).toString()
                    binding.tvUnitMicrogram.text = (num2 * 10000).toString()
                    binding.tvUnitNanogram.text = (num2 * 100000).toString()
                    binding.tvUnitPicogram.text = (num2 * 1000000).toString()
                    binding.tvUnitTola.text = (num2 / 11.6638038).toString()
                } else {
                    binding.tvUnitPound.text = ""
                    binding.tvUnitKilo.text = ""
                    binding.tvUnitGram.text = ""
                    binding.tvUnitMiligram.text = ""
                    binding.tvUnitMicrogram.text = ""
                    binding.tvUnitNanogram.text = ""
                    binding.tvUnitPicogram.text = ""
                    binding.tvUnitTola.text = ""

                }


            }

        })
    }else if (id==6L){
        val num1 = binding.inputValue.text.toString()
        if (num1.isNotBlank()) {
            val num2 = binding.inputValue.text.toString().trim().toDouble()
            binding.tvUnitPound.text = (num2 * 0.00220462).toString()
            binding.tvUnitKilo.text = (num2 * 0.001).toString()
            binding.tvUnitGram.text = (num2 * 1).toString()
            binding.tvUnitMiligram.text = (num2 * 1000).toString()
            binding.tvUnitMicrogram.text = (num2 * 10000).toString()
            binding.tvUnitNanogram.text = (num2 * 100000).toString()
            binding.tvUnitPicogram.text = (num2 * 1000000).toString()
            binding.tvUnitTola.text = (num2 / 11.6638038).toString()
        } else {
            binding.tvUnitPound.text = ""
            binding.tvUnitKilo.text = ""
            binding.tvUnitGram.text = ""
            binding.tvUnitMiligram.text = ""
            binding.tvUnitMicrogram.text = ""
            binding.tvUnitNanogram.text = ""
            binding.tvUnitPicogram.text = ""
            binding.tvUnitTola.text = ""

        }
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
                    binding.tvUnitPound.text = (num2 * 0.00220462).toString()
                    binding.tvUnitKilo.text = (num2 * 0.001).toString()
                    binding.tvUnitGram.text = (num2 * 1).toString()
                    binding.tvUnitMiligram.text = (num2 * 1000).toString()
                    binding.tvUnitMicrogram.text = (num2 * 10000).toString()
                    binding.tvUnitNanogram.text = (num2 * 100000).toString()
                    binding.tvUnitPicogram.text = (num2 * 1000000).toString()
                    binding.tvUnitTola.text = (num2 / 11.6638038).toString()
                } else {
                    binding.tvUnitPound.text = ""
                    binding.tvUnitKilo.text = ""
                    binding.tvUnitGram.text = ""
                    binding.tvUnitMiligram.text = ""
                    binding.tvUnitMicrogram.text = ""
                    binding.tvUnitNanogram.text = ""
                    binding.tvUnitPicogram.text = ""
                    binding.tvUnitTola.text = ""

                }
            }

        })
    }else if (id==7L){
        val num1 = binding.inputValue.text.toString()
        if (num1.isNotBlank()) {
            val num2 = binding.inputValue.text.toString().trim().toDouble()
            binding.tvUnitPound.text = (num2 * 0.00220462).toString()
            binding.tvUnitKilo.text = (num2 * 0.001).toString()
            binding.tvUnitGram.text = (num2 * 1).toString()
            binding.tvUnitMiligram.text = (num2 * 1000).toString()
            binding.tvUnitMicrogram.text = (num2 * 10000).toString()
            binding.tvUnitNanogram.text = (num2 * 100000).toString()
            binding.tvUnitPicogram.text = (num2 * 1000000).toString()
            binding.tvUnitTola.text = (num2 / 11.6638038).toString()
        } else {
            binding.tvUnitPound.text = ""
            binding.tvUnitKilo.text = ""
            binding.tvUnitGram.text = ""
            binding.tvUnitMiligram.text = ""
            binding.tvUnitMicrogram.text = ""
            binding.tvUnitNanogram.text = ""
            binding.tvUnitPicogram.text = ""
            binding.tvUnitTola.text = ""

        }
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
                    binding.tvUnitPound.text = (num2 * 0.00220462).toString()
                    binding.tvUnitKilo.text = (num2 * 0.001).toString()
                    binding.tvUnitGram.text = (num2 * 1).toString()
                    binding.tvUnitMiligram.text = (num2 * 1000).toString()
                    binding.tvUnitMicrogram.text = (num2 * 10000).toString()
                    binding.tvUnitNanogram.text = (num2 * 100000).toString()
                    binding.tvUnitPicogram.text = (num2 * 1000000).toString()
                    binding.tvUnitTola.text = (num2 / 11.6638038).toString()
                } else {
                    binding.tvUnitPound.text = ""
                    binding.tvUnitKilo.text = ""
                    binding.tvUnitGram.text = ""
                    binding.tvUnitMiligram.text = ""
                    binding.tvUnitMicrogram.text = ""
                    binding.tvUnitNanogram.text = ""
                    binding.tvUnitPicogram.text = ""
                    binding.tvUnitTola.text = ""

                }


            }

        })







    }
                        Toast.makeText(this@WeightActivity,
                            getString(R.string.selected_item) + id .toString() +
                                    "" + languages[position], Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
            }*/
    }
    /*   private fun setupNumberPickerForStringValues() {
           val numberPicker = binding.numberPicker
           val values = arrayOf("Pound", "KiloGram", "Gram", "MiliGram", "MicroGram", "NanoGram", "Picogram", "Tola")
           numberPicker.minValue = 0
           numberPicker.maxValue = values.size - 1
           numberPicker.displayedValues = values
           numberPicker.wrapSelectorWheel = true
           numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->

               val text = "Changed from " + values[oldVal] + " to " + values[newVal]
               Toast.makeText(this@WeightActivity, text, Toast.LENGTH_SHORT).show()

               if(values[newVal]=="Pound"){

                   val num1 = binding.inputValue.text.toString()
                   if (num1.isNotBlank()) {
                       val num2 = binding.inputValue.text.toString().trim().toDouble()
                       binding.tvUnitPound.text = (num2 /1).toString()
                       binding.tvUnitKilo.text = (num2 * 0.45359237).toString()
                       binding.tvUnitGram.text = (num2 *453.59237).toString()
                       binding.tvUnitMiligram.text = (num2 *453592.37).toString()
                       binding.tvUnitMicrogram.text = (num2 * 	453592370 ).toString()
                       binding.tvUnitNanogram.text = (num2 * 453592370000).toString()
                       binding.tvUnitPicogram.text = (num2 *453592370000000).toString()
                       binding.tvUnitTola.text = (num2 * 38.89).toString()
                   } else {
                       binding.tvUnitPound.text = ""
                       binding.tvUnitKilo.text = ""
                       binding.tvUnitGram.text = ""
                       binding.tvUnitMiligram.text = ""
                       binding.tvUnitMicrogram.text = ""
                       binding.tvUnitNanogram.text = ""
                       binding.tvUnitPicogram.text = ""
                       binding.tvUnitTola.text = ""

                   }
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
                               binding.tvUnitPound.text = (num2 /1).toString()
                               binding.tvUnitKilo.text = (num2 * 0.45359237).toString()
                               binding.tvUnitGram.text = (num2 *453.59237).toString()
                               binding.tvUnitMiligram.text = (num2 *453592.37).toString()
                               binding.tvUnitMicrogram.text = (num2 * 	453592370 ).toString()
                               binding.tvUnitNanogram.text = (num2 * 453592370000).toString()
                               binding.tvUnitPicogram.text = (num2 *453592370000000).toString()
                               binding.tvUnitTola.text = (num2 * 38.89).toString()
                           } else {
                               binding.tvUnitPound.text = ""
                               binding.tvUnitKilo.text = ""
                               binding.tvUnitGram.text = ""
                               binding.tvUnitMiligram.text = ""
                               binding.tvUnitMicrogram.text = ""
                               binding.tvUnitNanogram.text = ""
                               binding.tvUnitPicogram.text = ""
                               binding.tvUnitTola.text = ""

                           }


                       }

                   })


               }
           }
       }*/


    private fun setPicker() {
        data.add("KG")
        data.add("Pound")
        data.add("Gram")
        data.add("MG")
        data.add("µGram")
        data.add("nGram")
        data.add("pGram")
        data.add("Tola")



        rvHorizontalPicker = findViewById(R.id.rv_horizontal_picker)

        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(this) / 2 - ScreenUtils.dpToPx(this, 40)
        rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        rvHorizontalPicker.layoutManager = PickerLayoutManager(this).apply {
            callback = object : PickerLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    sliderAdapter.setSelectedItem(layoutPosition)
                    Log.d("selected text", data[layoutPosition])
                    Toast.makeText(this@WeightActivity, data[layoutPosition], Toast.LENGTH_SHORT)
                        .show()
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
            binding.btnSeven -> {

                if (stateError) {
                    // If current state is Error, replace the error message
                    binding.inputValue.text = (view as Button).text
                    stateError = false
                } else {
                    // If not, already there is a valid expression so append to it
                    binding.inputValue.append((view as Button).text)
                }
                // Set the flag
                lastNumeric = true


            }

            binding.btnEight -> {


                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val app_url = "convertor app" + BuildConfig.APPLICATION_ID
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
            binding.inputValue.text = (view as Button).text
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
        this.binding.inputValue.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    fun Clear(view: View) {

        var str: String = binding.inputValue.text.toString()
        if (!str.equals("") && lastDot == true) {
            str = str.substring(0, str.length - 1)
            binding.inputValue.text = str
            lastDot = false
        }else if(!str.equals("") && lastDot == false){

            str = str.substring(0, str.length - 1)
            binding.inputValue.text = str
            lastDot = false

        }
    }




}