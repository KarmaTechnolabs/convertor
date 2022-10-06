package karma.convertor.api.responsemodel

import com.google.gson.annotations.SerializedName

data class CreatedAt(

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("timezone")
    val timezone: String,

    @field:SerializedName("timezone_type")
    val timezoneType: Int
)
