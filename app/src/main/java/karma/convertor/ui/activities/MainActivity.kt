package karma.convertor.ui.activities

import android.content.Intent

import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore.getVersion
import karma.convertor.R
import karma.convertor.adapter.UnitListAdapter
import karma.convertor.api.requestmodel.UnititemModel
import karma.convertor.base.BaseActivity
import karma.convertor.databinding.ActivityMainBinding
import karma.convertor.listeners.ItemClickListener
import karma.convertor.utils.Constants.ADD_MOB_ADD_ID
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : BaseActivity(), View.OnClickListener,
    ItemClickListener<UnititemModel> {
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityMainBinding
    var adRequest: AdRequest? = null
    var itemList = ArrayList<UnititemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = FlexboxLayoutManager(this, FlexDirection.ROW)
        manager.justifyContent = JustifyContent.CENTER


        itemList.add(UnititemModel(R.drawable.weight_svgrepo_com__1_, "Weight"))
        itemList.add(UnititemModel(R.drawable.speed_svgrepo_com, "Speed"))
        itemList.add(UnititemModel(R.drawable.radar_area_svgrepo_com, "Area"))
        itemList.add(UnititemModel(R.drawable.cm_inch_length_svgrepo_com, "Length"))
        itemList.add(UnititemModel(R.drawable.fuel_pump_svgrepo_com, "Fuel"))
        itemList.add(UnititemModel(R.drawable.currency_svgrepo_com, "Currency"))
        itemList.add(UnititemModel(R.drawable.cooking_stew_svgrepo_com, "Cooking"))
        itemList.add(UnititemModel(R.drawable.cube_svgrepo_com, "Volume"))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.clickListener = this

        MobileAds.initialize(this) {}


        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        setupGridView()


    }

    private fun setupGridView() {
        val adapter = UnitListAdapter(this, R.layout.unit_item, itemList)
        adapter.setClickListener(this)

        binding.gridview.adapter = adapter
        binding.gridview.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->


                Toast.makeText(
                    this, " Clicked Position: " + (id + 1),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onClick(view: View?) {
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
        }}

        override fun onItemClick(viewIdRes: Int, model: UnititemModel, position: Int) {
           when (viewIdRes) {
                    R.id.icon -> {
                if (model.img_id == R.drawable.weight_svgrepo_com__1_) {
                    val intent = Intent(this, WeightActivity::class.java)
                    startActivity(intent)
                } /*else if (model.img_id == R.drawable.ic_telegram_logo) {
                    val intent = Intent(this, TelegramActivity::class.java)
                    startActivity(intent)
                } else if (model.img_id == R.drawable.ic_viber_logo) {

                    val intent = Intent(this, ViberActivity::class.java)
                    startActivity(intent)
                } else if ((model.img_id == R.drawable.ic_whatsappbusiness_icon)) {

                    val intent = Intent(this, WhatsappbusinessActivity::class.java)
                    startActivity(intent)
                } else if (model.img_id == R.drawable.ic_comingsoon_logo) {

                    Toast.makeText(
                        this,
                        "Your suggestions are welcomed, contact to karmatechnolabs.com",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }*/
            }
           }
        }


        override fun onPause() {
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
        }
    }


