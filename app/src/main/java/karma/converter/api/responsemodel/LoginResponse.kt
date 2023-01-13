package karma.converter.api.responsemodel

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("occupation")
    val occupation: String? = null,

    @field:SerializedName("app_type")
    val appType: String,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("pin_code")
    val pinCode: String? = null,

    @field:SerializedName("created_at")
    val createdAt: CreatedAt? = null,

    @field:SerializedName("device_type")
    var deviceType: String,

    @field:SerializedName("profile_image")
    val profileImage: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_mobile")
    val userMobile: String? = null,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("state")
    val state: String? = null,

    @field:SerializedName("village")
    val village: String? = null,

    @field:SerializedName("first_name")
    val firstName: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("preferred_language")
    val preferredLanguage: String? = null,

    @field:SerializedName("user_status")
    val userStatus: String? = null,

    @field:SerializedName("user_slug")
    val userSlug: String,

    @field:SerializedName("email_verified")
    val emailVerified: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,

    @field:SerializedName("middle_name")
    val middleName: String? = null,

    @field:SerializedName("token")
    var token: String,

    @field:SerializedName("qualification")
    val qualification: String? = null,

    @field:SerializedName("social_provider_id")
    val socialProviderId: String? = null,

    @field:SerializedName("social_provider")
    val socialProvider: String? = null,

    @field:SerializedName("dob")
    val dob: String? = null,

    @field:SerializedName("device_token")
    var deviceToken: String,

    @field:SerializedName("business_email")
    val businessEmail: String? = null,

    @field:SerializedName("how_did_you_hear_about_the_app")
    val howDidYouHearAboutTheApp: String? = null,

    @field:SerializedName("state_data")
    val stateData: StateData? = null,

    @field:SerializedName("city_data")
    val cityData: CityData? = null
){
    data class StateData(@field:SerializedName("state_id")
                         val stateId: Int? = null,
                         @field:SerializedName("name")
                         val name: String? = null)

    data class CityData(@field:SerializedName("city_id")
                         val cityId: Int? = null,
                         @field:SerializedName("name")
                         val name: String? = null)

    fun getFullName(): String {
        var name = firstName
        if (!lastName.isNullOrBlank()) {
            name = "$name $lastName"
        }
        return name
    }
}