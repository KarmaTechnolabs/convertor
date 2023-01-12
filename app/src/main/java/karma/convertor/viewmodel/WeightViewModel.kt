package karma.convertor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import karma.convertor.api.requestmodel.UnitActivityModelResponse

class WeightViewModel : ViewModel() {



    private val weightRequestLiveData = MutableLiveData<String>()

 val  weigthlivedata = MutableLiveData<UnitActivityModelResponse>()

    /*val hisoryResponse: LiveData<Event<APIResource<ArrayList<HistoryResponse>>>> =
        historyRequestLiveData.switchMap {
            repository.callHistoryAPI(it)
        }*/


    val weightResponse: LiveData<UnitActivityModelResponse> =
     weightRequestLiveData.map { UnitActivityModelResponse("GRAM" ,it) }

    fun callweightAPI(weightdata: String) {

      weightRequestLiveData.value = weightdata
    }



   /* override fun onCleared() {
        super.onCleared()
        repository.clearRepo()
    }*/
}
