package karma.converter.utils

import android.annotation.SuppressLint
import android.content.Context
import karma.converter.base.BaseApplication

@SuppressLint("StaticFieldLeak")
object UserStateManager {

    private val context: Context = BaseApplication.getApplicationContext()
    private val sharedPreferenceHelper: SharedPreferenceHelper =
        SharedPreferenceHelper.getInstance(context)

    fun getFirebaseToken(): String {
        return sharedPreferenceHelper.getValue(Constants.PREF_FIREBASE_TOKEN, "")
    }

    fun getBearerToken(): String {
        return sharedPreferenceHelper.getValue(Constants.PREF_BEARER_TOKEN, "")
    }

    fun saveFirebaseToken(token: String) {
        sharedPreferenceHelper.setValue(Constants.PREF_FIREBASE_TOKEN, token)
    }

    fun isOnBoardingComplete(): Boolean {
        return sharedPreferenceHelper
            .getValue(Constants.PREF_IS_INTRODUCTION_FINISHED_BOOL, false)
    }

    fun markOnBoardingComplete() {
        sharedPreferenceHelper.setValue(Constants.PREF_IS_INTRODUCTION_FINISHED_BOOL, true)
    }
}