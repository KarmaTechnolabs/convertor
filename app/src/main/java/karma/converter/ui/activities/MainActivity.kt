package karma.converter.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import karma.converter.BuildConfig
import karma.converter.R
import karma.converter.adapter.GridViewAdapter
import karma.converter.adapter.UnitListAdapter
import karma.converter.api.requestmodel.UnititemModel
import karma.converter.base.BaseActivity
import karma.converter.custom.gotoActivity
import karma.converter.databinding.ActivityMainBinding
import karma.converter.listeners.ItemClickListener


class MainActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnititemModel> {
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityMainBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnititemModel>()
    private var gridviewAdapter: GridViewAdapter? = GridViewAdapter(this)

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

    private fun setupGridView() {
        val adapter = UnitListAdapter(this, R.layout.unit_item, itemList)
        adapter.setClickListener(this)

        //  binding.gridview.adapter = adapter
        /*    binding.gridview.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->


            /*    Toast.makeText(
                    this, " Clicked Position: " + (id + 1),
                    Toast.LENGTH_SHORT
                ).show()*/
            }
    }*/

        /*  override fun onClick(view: View?) {
            when (view?.id) {
                /*  R.id.grid_share -> {

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val app_url =
                    resources.getString(R.string.whatsapp_sharemessages) + BuildConfig.APPLICATION_ID
                shareIntent.putExtra(Intent.EXTRA_TEXT, app_url)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }*/
            }
        }*/

        /* override fun onItemClick(viewIdRes: Int, model: UnititemModel, position: Int) {
            when (viewIdRes) {
                R.id.icon -> {
                    if (model.img_id == R.drawable.weight) {
                        val intent = Intent(this, WeightActivity::class.java)
                        startActivity(intent)
                    } else if (model.img_id == R.drawable.mobile_data) {
                        val intent = Intent(this, DataActivity::class.java)
                        startActivity(intent)
                    } else if (model.img_id == R.drawable.temperature) {

                        val intent = Intent(this, TemperatureActivity::class.java)
                        startActivity(intent)
                    } else if ((model.img_id == R.drawable.sound)) {

                        val intent = Intent(this, SoundActivity::class.java)
                        startActivity(intent)
                    } else if ((model.img_id == R.drawable.length)) {

                        val intent = Intent(this, LengthActivity::class.java)
                        startActivity(intent)
                    } else if ((model.img_id == R.drawable.area)) {

                        val intent = Intent(this, AreaActivity::class.java)
                        startActivity(intent)
                    } else if ((model.img_id == R.drawable.electric_tower_power)) {

                        val intent = Intent(this, PowerActivity::class.java)
                        startActivity(intent)
                    } else if ((model.img_id == R.drawable.speed)) {

                        val intent = Intent(this, SpeedActivity::class.java)
                        startActivity(intent)
                    }/* else if (model.img_id == R.drawable.ic_comingsoon_logo) {

                    Toast.makeText(
                        this,
                        "Your suggestions are welcomed, contact to karmatechnolabs.com",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }*/
                }
            }
        }*/


        /* override fun onPause() {
        if (binding.adView != null) {
            binding.adView.pause()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (binding.adView != null) {
            binding.adView.resume()
        }
    }

    override fun onDestroy() {
        if (binding.adView != null) {
            binding.adView.destroy()
        }
        super.onDestroy()
    }*/
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
            8-> {
                gotoActivity(VolumeActivity::class.java, needToFinish = false)
            }
            9-> {
                gotoActivity(PressureActivity::class.java, needToFinish = false)
            }
            10-> {
                gotoActivity(ForceActivity::class.java, needToFinish = false)
            }

        }
    }

    override fun onClick(view: View?) {
        when (view) {
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

}