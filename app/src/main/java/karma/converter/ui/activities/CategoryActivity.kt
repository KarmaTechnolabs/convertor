package karma.converter.ui.activities

import android.os.Bundle
import android.view.View
import karma.converter.adapter.CategoryViewAdapter
import karma.converter.api.requestmodel.UnitItemModel
import karma.converter.base.BaseActivity
import karma.converter.custom.gotoActivity
import karma.converter.databinding.ActivityCategoryBinding
import karma.converter.listeners.ItemClickListener

class CategoryActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitItemModel> {
    //    var adRequest: AdRequest? = null
    private lateinit var binding:ActivityCategoryBinding
    var itemList = ArrayList<UnitItemModel>()
    private var categoryViewAdapter:CategoryViewAdapter? = CategoryViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemList.add(UnitItemModel(0, "Engineering Unit"))
        itemList.add(UnitItemModel(0, "Light Unit"))
        itemList.add(UnitItemModel(0, "Magnetic Unit"))
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryViewAdapter?.clear()
        categoryViewAdapter?.setClickListener(this)
        binding.gridview.adapter = categoryViewAdapter
        categoryViewAdapter?.setItems(itemList)
    }

    override fun onClick(v: View?) {
        //
    }

    override fun onItemClick(viewIdRes: Int, model: UnitItemModel, position: Int) {
        when(position){
            0 -> {
                gotoActivity(EngineeringConvertorActivity::class.java, needToFinish = false)
            }
        }
    }
}