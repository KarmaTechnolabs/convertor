package karma.converter.api.requestmodel

import com.google.gson.annotations.SerializedName

data class RegisterRequestModel(
    @field:SerializedName("first_name")
    val firstName: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)
