package karma.convertor.api.requestmodel

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("device_token")
    val deviceToken: String,

    @field:SerializedName("app_type")
    val appType: String = "1",

    @field:SerializedName("device_type")
    val deviceType: String = "1"
)
