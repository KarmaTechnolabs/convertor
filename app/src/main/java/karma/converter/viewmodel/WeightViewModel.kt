package karma.converter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import karma.converter.api.requestmodel.UnitActivityModelResponse

class WeightViewModel : ViewModel() {



    private val weightRequestLiveData = MutableLiveData<String>()

    val weightResponse: LiveData<UnitActivityModelResponse> =
     weightRequestLiveData.map { UnitActivityModelResponse("GRAM" ,it) }

    fun callweightAPI(weightData: String) {
        weightRequestLiveData.value = weightData
    }

}
