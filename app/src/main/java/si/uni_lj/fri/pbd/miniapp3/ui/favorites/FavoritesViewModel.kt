package si.uni_lj.fri.pbd.miniapp3.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.miniapp3.repository.RecipeRepository

class FavoritesViewModel(application: Application?) : AndroidViewModel(application!!) {
    var allRecipesLive: MutableLiveData<List<RecipeDetails>>

    fun getAllRecipes() {
        RecipeRepository.getAllRecepies()
    }

    init {
        allRecipesLive = RecipeRepository.allRecipes
    }
}