package karma.converter.ui.activities

import android.annotation.SuppressLint
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
import karma.converter.api.requestmodel.UnititemModel
import karma.converter.base.BaseActivity
import karma.converter.custom.gotoActivity
import karma.converter.databinding.ActivityMainBinding
import karma.converter.listeners.ItemClickListener


class MainActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnititemModel> {
    private lateinit var binding: ActivityMainBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnititemModel>()
    private var gridviewAdapter: GridViewAdapter? = GridViewAdapter(this)

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = FlexboxLayoutManager(this, FlexDirection.ROW)
        manager.justifyContent = JustifyContent.CENTER
        itemList.add(UnititemModel(R.drawable.area, "Area"))
        itemList.add(UnititemModel(R.drawable.weight, "Weight"))
        itemList.add(UnititemModel(R.drawable.temperature, "Temperature"))
        itemList.add(UnititemModel(R.drawable.sound, "Sound"))
        itemList.add(UnititemModel(R.drawable.speed, "Speed"))
        itemList.add(UnititemModel(R.drawable.length, "Length"))
        itemList.add(UnititemModel(R.drawable.mobile_data, "Data"))
        itemList.add(UnititemModel(R.drawable.electric_tower_power, "Power"))
        itemList.add(UnititemModel(R.drawable.noun_density, "Volume"))
        itemList.add(UnititemModel(R.drawable.pressure_gauge, "Pressure"))
        itemList.add(UnititemModel(R.drawable.force_, "Force"))
        itemList.add(UnititemModel(R.drawable.work, "Work"))
        itemList.add(UnititemModel(R.drawable.angle, "Angle"))
        itemList.add(UnititemModel(R.drawable.cooking, "Cooking"))
        itemList.add(UnititemModel(R.drawable.outerspace1, "Space"))
        itemList.add(UnititemModel(R.drawable.sugar, "BloodSugar"))
        itemList.add(UnititemModel(R.drawable.fuel, "Fuel"))
        itemList.add(UnititemModel(R.drawable.energy, "Energy"))
        itemList.add(UnititemModel(R.drawable.time, "Time"))
        itemList.add(UnititemModel(R.drawable.luminance, "Luminance"))
        binding = ActivityMainBinding.inflate(layoutInflater)
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


    override fun onItemClick(viewIdRes: Int, model: UnititemModel, position: Int) {
        when (position) {

            0 -> {

                gotoActivity(AreaActivity::class.java, needToFinish = false)

            }
            1 -> {
                gotoActivity(WeightActivity::class.java, needToFinish = false)
            }
            2 -> {
                gotoActivity(TemperatureActivity::class.java, needToFinish = false)
            }
            3 -> {
                gotoActivity(SoundActivity::class.java, needToFinish = false)
            }
            4 -> {
                gotoActivity(SpeedActivity::class.java, needToFinish = false)
            }
            5 -> {
                gotoActivity(LengthActivity::class.java, needToFinish = false)
            }
            6 -> {
                gotoActivity(DataActivity::class.java, needToFinish = false)
            }
            7 -> {
                gotoActivity(PowerActivity::class.java, needToFinish = false)
            }
            8 -> {
                gotoActivity(VolumeActivity::class.java, needToFinish = false)
            }
            9 -> {
                gotoActivity(PressureActivity::class.java, needToFinish = false)
            }
            10 -> {
                gotoActivity(ForceActivity::class.java, needToFinish = false)
            }

            11 -> {
                gotoActivity(WorkActivity::class.java, needToFinish = false)
            }

            12 -> {
                gotoActivity(DegreeActivity::class.java, needToFinish = false)
            }

            13 -> {
                gotoActivity(CookingActivity::class.java, needToFinish = false)
            }

            14 -> {
                gotoActivity(SpaceActivity::class.java, needToFinish = false)
            }

            15 -> {
                gotoActivity(BloodActivity::class.java, needToFinish = false)
            }

            16 -> {
                gotoActivity(FuelActivity::class.java, needToFinish = false)
            }
            17 -> {
                gotoActivity(EnergyActivity::class.java, needToFinish = false)
            }
            18 -> {
                gotoActivity(TimeActivity::class.java, needToFinish = false)
            }
            19 -> {
                gotoActivity(LuminanceActivity::class.java, needToFinish = false)
            }

        }
    }

    override fun onClick(view: View?) {
        when (view) {
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

}