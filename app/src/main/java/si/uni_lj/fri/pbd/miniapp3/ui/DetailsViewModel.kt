package si.uni_lj.fri.pbd.miniapp3.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import si.uni_lj.fri.pbd.miniapp3.models.Mapper
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.miniapp3.repository.RecipeRepository

class DetailsViewModel(application: Application?) : AndroidViewModel(application!!) {
    var searchResultsLive = RecipeRepository.searchResults
    var allRecipesLive = RecipeRepository.allRecipes
    var errorLive: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun CheckIfFavourite(recipeId: String) {
        RecipeRepository.findRecipe(recipeId)
    }

    fun getAllRecepies() {
        RecipeRepository.getAllRecepies()
    }

    fun UpdateFavourites(details: RecipeDetailsDTO?, isFavourite: Boolean) {
        if(details == null)
            return

        if(isFavourite) {
            RecipeRepository.deleteRecipeDetails(Mapper.mapRecipeDetailsDtoToRecipeDetails(false, details))
        } else {
            RecipeRepository.insertRecipeDetails(Mapper.mapRecipeDetailsDtoToRecipeDetails(false, details))
        }

        CheckIfFavourite(details.idMeal.toString())
    }

    var isDownloadingLive: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
    var currentDetailsLive: MutableLiveData<RecipeDetailsDTO?> = MutableLiveData<RecipeDetailsDTO?>()
    fun GetDetails(recipeId: String, calledFrom: String) {
        if (calledFrom == "SearchFragment") {
            RecipeRepository.getRecipeDetailsByIdFromApi(recipeId,
                callback = { recipes, isSuccess ->
                    if (isSuccess && recipes != null) {
//                        ExtractMealData(recipes[0])
                        currentDetailsLive.postValue(recipes[0])
                    } else {
                        println("Error")
                        errorLive.postValue(true)
                    }
                    isDownloadingLive.postValue(false)  // Moved inside the callback to ensure it updates after the API call completes
                },
                downloadingState = { isDownloadingLive.postValue(it) }
            )
        }
    }
}