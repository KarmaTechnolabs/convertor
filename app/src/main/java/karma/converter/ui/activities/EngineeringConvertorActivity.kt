package karma.converter.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.multidex.BuildConfig
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import karma.converter.R
import karma.converter.adapter.GridViewAdapter
import karma.converter.api.requestmodel.UnitItemModel
import karma.converter.base.BaseActivity
import karma.converter.custom.gotoActivity
import karma.converter.databinding.ActivityEngineeringConvertorBinding
import karma.converter.listeners.ItemClickListener

class EngineeringConvertorActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitItemModel> {


    private lateinit var binding: ActivityEngineeringConvertorBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnitItemModel>()
    private var gridviewAdapter: GridViewAdapter? = GridViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = FlexboxLayoutManager(this, FlexDirection.ROW)
        manager.justifyContent = JustifyContent.CENTER

        itemList.add(UnitItemModel(R.drawable.noun_density, "Volume"))
        itemList.add(UnitItemModel(R.drawable.area, "Area"))
        itemList.add(UnitItemModel(R.drawable.force_, "Force"))
        itemList.add(UnitItemModel(R.drawable.energy, "Energy"))
        itemList.add(UnitItemModel(R.drawable.speed, "Speed"))
        itemList.add(UnitItemModel(R.drawable.fuel, "Fuel"))
        itemList.add(UnitItemModel(R.drawable.mobile_data, "Data"))
        itemList.add(UnitItemModel(R.drawable.temperature, "Temperature"))
        itemList.add(UnitItemModel(R.drawable.pressure_gauge, "Pressure"))
        itemList.add(UnitItemModel(R.drawable.electric_tower_power, "Power"))
        itemList.add(UnitItemModel(R.drawable.time, "Time"))
        itemList.add(UnitItemModel(R.drawable.angle, "Angle"))
        itemList.add(UnitItemModel(R.drawable.noun_density, "Volume"))

        binding = ActivityEngineeringConvertorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.toolbar.text = resources.getString(R.string.app_title)
        binding.header.backToHome.setImageResource(android.R.color.transparent)
        binding.header.shareImageView.visibility = View.VISIBLE
        binding.clickListener = this
        binding.header.shareImageView.setOnClickListener(this)



        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        gridviewAdapter?.clear()
        gridviewAdapter?.setClickListener(this)
        binding.gridview.adapter = gridviewAdapter
        gridviewAdapter?.setItems(itemList)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.header.shareImageView -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val appUrl =
                    resources.getString(R.string.whatsapp_sharemessages) + BuildConfig.APPLICATION_ID
                shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl)
                startActivity(Intent.createChooser(shareIntent, "Share via"))

            }

        }
    }

    override fun onItemClick(viewIdRes: Int, model: UnitItemModel, position: Int) {
        when (position) {
            0 -> {
                gotoActivity(VolumeActivity::class.java, needToFinish = false)
            }
            1 -> {
                gotoActivity(AreaActivity::class.java, needToFinish = false)
            }
            2 -> {
                gotoActivity(ForceActivity::class.java, needToFinish = false)
            }
            3 -> {
                gotoActivity(EnergyActivity::class.java, needToFinish = false)
            }
            4 -> {
                gotoActivity(SpeedActivity::class.java, needToFinish = false)
            }
            5 -> {
                gotoActivity(FuelActivity::class.java, needToFinish = false)
            }
            6 -> {
                gotoActivity(DataActivity::class.java, needToFinish = false)
            }
            7 -> {
                gotoActivity(TemperatureActivity::class.java, needToFinish = false)
            }
            8 -> {
                gotoActivity(PressureActivity::class.java, needToFinish = false)
            }
            9 -> {
                gotoActivity(PowerActivity::class.java, needToFinish = false)
            }
            10 -> {
                gotoActivity(TimeActivity::class.java, needToFinish = false)
            }
            11 -> {
                gotoActivity(WorkActivity::class.java, needToFinish = false)
            }
            12 -> {
                gotoActivity(VolumeActivity::class.java, needToFinish = false)
            }

        }
    }
}