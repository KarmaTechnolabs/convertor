package karma.convertor.base

import karma.convertor.utils.Utils
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

class BaseResponseModel<T> {

    var ResponseJson: T? = null

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("message")
    @Expose
    var message: String? = null

    @Expose
    @SerializedName("data")
    var data: Any? = null

    fun getResponseModel(aModel: Class<T>?): T? {
        setResponseJson(aModel)
        return ResponseJson
    }

    fun getResponseModel(typeOfObjectsList: Type?): T {
        return Gson().fromJson(Utils.getGSONWithExpose()?.toJson(data), typeOfObjectsList)
    }

    fun setResponse(data: String?) {
        this.data = data
    }

    private fun setResponseJson(aModel: Class<T>?) {
        ResponseJson = prepareModel(data!!, aModel)
    }

    private fun <T> prepareModel(aString: Any, aClass: Class<T>?): T {
        return Gson().fromJson(Gson().toJson(aString), aClass)
    }

}