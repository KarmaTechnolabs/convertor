package karma.converter.ui.activities

import android.os.Bundle
import karma.converter.base.FullScreenBaseActivity
import karma.converter.custom.gotoActivity
import karma.converter.utils.RemoteConfigManager
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : FullScreenBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        RemoteConfigManager.checkRemoteConfig()

        Timer("splash", false).schedule(1000) {
            gotoActivity(MainActivity::class.java, needToFinish = true)
            //gotoActivity(CategoryActivity::class.java, needToFinish = true)
            //gotoActivity(TestActivity::class.java, needToFinish = true)
            //gotoActivity(MainActivity2::class.java, needToFinish = true)
        }
    }


}