package karma.convertor.api.requestmodel

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequestModel(
    @field:SerializedName("email")
    val email: String
)
