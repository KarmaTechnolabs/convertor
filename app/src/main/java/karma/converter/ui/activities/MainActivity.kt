package karma.converter.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import karma.converter.BuildConfig
import karma.converter.R
import karma.converter.adapter.ChildItem
import karma.converter.adapter.ParentItem
import karma.converter.adapter.ParentViewAdapter
import karma.converter.base.BaseActivity
import karma.converter.base.OnItemClickListener
import karma.converter.custom.gotoActivity
import karma.converter.databinding.ActivityMainBinding


/*
class MainActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnitItemModel> {
    private lateinit var binding: ActivityMainBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnitItemModel>()
    private var gridviewAdapter: GridViewAdapter? = GridViewAdapter(this)

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = FlexboxLayoutManager(this, FlexDirection.ROW)
        manager.justifyContent = JustifyContent.CENTER
        itemList.add(UnitItemModel(R.drawable.area, "Area"))
        itemList.add(UnitItemModel(R.drawable.weight, "Weight"))
        itemList.add(UnitItemModel(R.drawable.temperature, "Temperature"))
        itemList.add(UnitItemModel(R.drawable.sound, "Sound"))
        itemList.add(UnitItemModel(R.drawable.speed, "Speed"))
        itemList.add(UnitItemModel(R.drawable.length, "Length"))
        itemList.add(UnitItemModel(R.drawable.mobile_data, "Data"))
        itemList.add(UnitItemModel(R.drawable.electric_tower_power, "Power"))
        itemList.add(UnitItemModel(R.drawable.noun_density, "Volume"))
        itemList.add(UnitItemModel(R.drawable.pressure_gauge, "Pressure"))
        itemList.add(UnitItemModel(R.drawable.force_, "Force"))
        itemList.add(UnitItemModel(R.drawable.work, "Work"))
        itemList.add(UnitItemModel(R.drawable.angle, "Angle"))
        itemList.add(UnitItemModel(R.drawable.cooking, "Cooking"))
        itemList.add(UnitItemModel(R.drawable.outerspace1, "Space"))
        itemList.add(UnitItemModel(R.drawable.sugar, "BloodSugar"))
        itemList.add(UnitItemModel(R.drawable.fuel, "Fuel"))
        itemList.add(UnitItemModel(R.drawable.energy, "Energy"))
        itemList.add(UnitItemModel(R.drawable.time, "Time"))
        itemList.add(UnitItemModel(R.drawable.luminance, "Luminance"))
        itemList.add(UnitItemModel(R.drawable.frequency, "Frequency"))
        itemList.add(UnitItemModel(R.drawable.resistance, "Resistance"))
        itemList.add(UnitItemModel(R.drawable.magnetic, "Magnetic"))
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


    override fun onItemClick(viewIdRes: Int, model: UnitItemModel, position: Int) {
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
            20 -> {
                gotoActivity(FrequencyActivity::class.java, needToFinish = false)
            }
            21 -> {
                gotoActivity(ResistanceActivity::class.java, needToFinish = false)
            }
            22 -> {
                gotoActivity(MagneticActivity::class.java, needToFinish = false)
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

}*/

class MainActivity : BaseActivity(), OnItemClickListener<ChildItem>, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var parentList: ArrayList<ParentItem> = ArrayList()
    private var parentViewAdapter: ParentViewAdapter? = null

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.parentRecyclerView.setHasFixedSize(true)

        prepareData()

        parentViewAdapter = ParentViewAdapter(parentList)
        binding.parentRecyclerView.adapter = parentViewAdapter
        parentViewAdapter?.setOnItemClickListener(this)


        setContentView(binding.root)

        binding.header.toolbar.text = resources.getString(R.string.app_title)
        binding.header.backToHome.setImageResource(android.R.color.transparent)
        binding.header.shareImageView.visibility = View.VISIBLE
        binding.clickListener = this
        binding.header.shareImageView.setOnClickListener(this)


        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

    }

    private fun prepareData() {
        val items1 = ArrayList<ChildItem>()
        items1.add(ChildItem(10, R.drawable.noun_density, "Volume"))
        items1.add(ChildItem(11, R.drawable.ic_area, "Area"))
        items1.add(ChildItem(12, R.drawable.energy, "Energy"))
        items1.add(ChildItem(13, R.drawable.force_, "Force"))
        items1.add(ChildItem(14, R.drawable.speed, "Speed"))
        items1.add(ChildItem(15, R.drawable.ic_fuel, "Fuel"))
        items1.add(ChildItem(16, R.drawable.ic_data, "Data"))
        items1.add(ChildItem(17, R.drawable.ic_temperature, "Temperature"))
        items1.add(ChildItem(18, R.drawable.ic_pressure, "Pressure"))
        items1.add(ChildItem(19, R.drawable.ic_power, "Power"))
        items1.add(ChildItem(110, R.drawable.ic_time, "Time"))
        items1.add(ChildItem(111, R.drawable.ic_angle, "Angle"))
        parentList.add(ParentItem(R.drawable.ic_eng, "Engineering Converter", items1))

        val items2 = ArrayList<ChildItem>()
        items2.add(ChildItem(20, R.drawable.ic_luminance, "Luminance"))
        items2.add(ChildItem(21, R.drawable.ic_frequency, "Frequency"))
        parentList.add(ParentItem(R.drawable.ic_light, "Light Converter", items2))

        val items3 = ArrayList<ChildItem>()
        items3.add(ChildItem(30, R.drawable.magnetic, "Magnetic"))
        parentList.add(ParentItem(R.drawable.ic_magnetism, "Magnetism Converter", items3))

        val items4 = ArrayList<ChildItem>()
        items4.add(ChildItem(40, R.drawable.weight, "Weight"))
        items4.add(ChildItem(41, R.drawable.sound, "Sound"))
        items4.add(ChildItem(42, R.drawable.length, "Length"))
        items4.add(ChildItem(43, R.drawable.work, "Work"))
        items4.add(ChildItem(44, R.drawable.cooking, "Cooking"))
        items4.add(ChildItem(45, R.drawable.outerspace1, "Space"))
        items4.add(ChildItem(46, R.drawable.sugar, "BloodSugar"))
        items4.add(ChildItem(47, R.drawable.resistance, "Resistance"))
        parentList.add(ParentItem(R.drawable.ic_other, "Other Converter", items4))
    }

    override fun onItemClick(item: ChildItem) {
        when (item.id) {
            10 -> {
                gotoActivity(VolumeActivity::class.java, needToFinish = false)
            }

            11 -> {
                gotoActivity(AreaActivity::class.java, needToFinish = false)
            }

            12 -> {
                gotoActivity(EnergyActivity::class.java, needToFinish = false)
            }

            13 -> {
                gotoActivity(ForceActivity::class.java, needToFinish = false)
            }

            14 -> {
                gotoActivity(SpeedActivity::class.java, needToFinish = false)
            }

            15 -> {
                gotoActivity(FuelActivity::class.java, needToFinish = false)
            }

            16 -> {
                gotoActivity(DataActivity::class.java, needToFinish = false)
            }

            17 -> {
                gotoActivity(TemperatureActivity::class.java, needToFinish = false)
            }

            18 -> {
                gotoActivity(PressureActivity::class.java, needToFinish = false)
            }

            19 -> {
                gotoActivity(PowerActivity::class.java, needToFinish = false)
            }

            110 -> {
                gotoActivity(TimeActivity::class.java, needToFinish = false)
            }

            111 -> {
                gotoActivity(DegreeActivity::class.java, needToFinish = false)
            }

            20 -> {
                gotoActivity(LuminanceActivity::class.java, needToFinish = false)
            }

            21 -> {
                gotoActivity(FrequencyActivity::class.java, needToFinish = false)
            }

            30 -> {
                gotoActivity(MagneticActivity::class.java, needToFinish = false)
            }

            40 -> {
                gotoActivity(WeightActivity::class.java, needToFinish = false)
            }

            41 -> {
                gotoActivity(SoundActivity::class.java, needToFinish = false)
            }

            42 -> {
                gotoActivity(LengthActivity::class.java, needToFinish = false)
            }

            43 -> {
                gotoActivity(WorkActivity::class.java, needToFinish = false)
            }

            44 -> {
                gotoActivity(CookingActivity::class.java, needToFinish = false)
            }

            45 -> {
                gotoActivity(SpaceActivity::class.java, needToFinish = false)
            }

            46 -> {
                gotoActivity(BloodActivity::class.java, needToFinish = false)
            }

            47 -> {
                gotoActivity(ResistanceActivity::class.java, needToFinish = false)
            }
        }
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

}