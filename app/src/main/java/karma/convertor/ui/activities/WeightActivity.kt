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
import karma.convertor.custom.gotoActivity
import karma.convertor.databinding.ActivityWeightBinding
import kotlinx.android.synthetic.main.activity_weight.*
import kotlinx.android.synthetic.main.appbar.view.*

class WeightActivity : BaseActivity(),View.OnClickListener
    {
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityWeightBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnititemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clickListener = this
        binding.appBarDashboard.back_to_home.setOnClickListener(this)
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


}else if(id==5L){
    val num1 = binding.inputValue.text.toString()
    if (num1.isNotBlank()) {
        val num2 = binding.inputValue.text.toString().trim().toDouble()
        binding.tvUnitPound.text = (num2 / .45).toString()
        binding.tvUnitKilo.text = (num2 * 1).toString()
        binding.tvUnitGram.text = (num2 * 1000).toString()
        binding.tvUnitMiligram.text = (num2 * 10000).toString()
        binding.tvUnitMicrogram.text = (num2 * 1).toString()
        binding.tvUnitNanogram.text = (num2 * 1000).toString()
        binding.tvUnitPicogram.text = (num2 * 10000).toString()
        binding.tvUnitTola.text = (num2 * 10000).toString()
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
            binding.tvUnitPound.text = (num2 / 10).toString()
            binding.tvUnitKilo.text = (num2 * 1).toString()
            binding.tvUnitGram.text = (num2 * 1000).toString()
            binding.tvUnitMiligram.text = (num2 * 10000).toString()
            binding.tvUnitMicrogram.text = (num2 * 1).toString()
            binding.tvUnitNanogram.text = (num2 * 1000).toString()
            binding.tvUnitPicogram.text = (num2 * 10000).toString()
            binding.tvUnitTola.text = (num2 * 10000).toString()
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
        }
    }

        override fun onClick(view: View?) {
            when (view) {
                binding.appBarDashboard.back_to_home ->{

                    gotoActivity(MainActivity::class.java)


                }
            }
        }
      }