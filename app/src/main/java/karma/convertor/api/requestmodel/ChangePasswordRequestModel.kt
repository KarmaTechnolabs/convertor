package karma.convertor.api.requestmodel

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequestModel(

	@field:SerializedName("old_password")
	val oldPassword: String,

	@field:SerializedName("new_password")
	val newPassword: String,

	@field:SerializedName("confirm_password")
	val confirmPassword: String
)
