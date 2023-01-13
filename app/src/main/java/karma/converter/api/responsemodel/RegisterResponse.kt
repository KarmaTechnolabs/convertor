package karma.converter.api.responsemodel

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("user_slug")
    val userSlug: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("created_at")
    val createdAt: CreatedAt,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("first_name")
    val firstName: String,

    @field:SerializedName("email")
    val email: String
)