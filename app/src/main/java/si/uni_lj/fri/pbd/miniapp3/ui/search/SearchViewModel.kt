package si.uni_lj.fri.pbd.miniapp3.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeSummaryDTO
import si.uni_lj.fri.pbd.miniapp3.repository.RecipeRepository

class SearchViewModel(application: Application?) : AndroidViewModel(application!!) {
    var recipeSummariesLive: MutableLiveData<List<RecipeSummaryDTO>?> = MutableLiveData<List<RecipeSummaryDTO>?>()
    var isDownloadingLive: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
    var ingredientsLive: MutableLiveData<ArrayList<String>?> = MutableLiveData<ArrayList<String>?>()
    var errorLive: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    var lastPicked: String = ""
    fun getRecipesByIngredientFromApi(ingredient: String) {
        if (ingredient == "Choose your ingredient" || lastPicked == ingredient)
            return
        lastPicked = ingredient

        RecipeRepository.getRecipesByIngredientFromApi(ingredient,
            callback = { meals, isSuccess ->
                if (isSuccess && meals != null) {
                    recipeSummariesLive.postValue(meals)
                } else {
                    println("Error")
                    errorLive.postValue(true)
                }
                isDownloadingLive.postValue(false)  // Moved inside the callback to ensure it updates after the API call completes
            },
            downloadingState = { isDownloadingLive.postValue(it) }
        )
    }

    fun getIngredientsFromApi() {
        RecipeRepository.getIngredientsFromApi(
            callback = { ingredients ->
                if (ingredients != null) {
                    ingredientsLive.postValue(ingredients)
                } else {
                    println("Error")
                    errorLive.postValue(true)
                }
            },
            downloadingState = { isDownloadingLive.postValue(it) }
        )
    }
}