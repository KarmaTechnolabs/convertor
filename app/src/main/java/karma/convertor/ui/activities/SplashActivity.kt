package karma.convertor.ui.activities

import android.content.Intent
import android.os.Bundle
import karma.convertor.base.FullScreenBaseActivity
import karma.convertor.custom.gotoActivity
import karma.convertor.utils.RemoteConfigManager
import karma.convertor.utils.UserStateManager
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : FullScreenBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        RemoteConfigManager.checkRemoteConfig()

        Timer("splash", false).schedule(3000) {


       gotoActivity(MainActivity::class.java)



        }
    }


}