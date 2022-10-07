package karma.convertor.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import karma.convertor.R
import karma.convertor.api.requestmodel.UnititemModel
import karma.convertor.base.BaseActivity
import karma.convertor.databinding.ActivityWeightBinding
import kotlinx.android.synthetic.main.activity_weight.*

class WeightActivity : BaseActivity()
    {
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityWeightBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnititemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // access the items of the list
        val languages = resources.getStringArray(R.array.Languages)

        // access the spinner

        if (binding.spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                if(id == 0L  ){

                  binding.inputValue.addTextChangedListener(object : TextWatcher {

                        override fun afterTextChanged(s: Editable) {}

                        override fun beforeTextChanged(s: CharSequence, start: Int,
                                                       count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence, start: Int,
                                                   before: Int, count: Int) {

                            val num1 = binding.inputValue.text.toString().trim().toInt()

                            binding.tvUnitKilo.text= (num1*1).toString()
                            binding.tvUnitGram.text= (num1*1000).toString()
                            binding.tvUnitMiligram.text=(num1*10000).toString()
                        }
                    })



                }else{

                    binding.tvUnitKilo.text= ""
                    binding.tvUnitGram.text= ""
                    binding.tvUnitMiligram.text=""
                }

                    Toast.makeText(this@WeightActivity,
                        getString(R.string.selected_item) + id .toString() +
                                "" + languages[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }









    }
}