package si.uni_lj.fri.pbd.miniapp3.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import si.uni_lj.fri.pbd.miniapp3.database.Database
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeSummaryDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator


class RecipeRepository(context: Context) {
//    val allRecipes: LiveData<List<RecipeDetails>>?

    companion object {
        fun getIngredientsFromApi(callback: (ArrayList<String>?) -> Unit, downloadingState: (Boolean) -> Unit) {
            val restApiClient = ServiceGenerator.createService(RestAPI::class.java)
            downloadingState(true)
            restApiClient.allIngredients?.enqueue(object : Callback<IngredientsDTO?> {
                override fun onResponse(call: Call<IngredientsDTO?>, response: Response<IngredientsDTO?>) {
                    downloadingState(false)
                    if (response.isSuccessful) {
                        val ingredientsDTO = response.body()
                        val ingredients = ingredientsDTO?.ingredients.orEmpty()

                        val newIngredients = ArrayList<String>()
                        for (ingredient in ingredients) {
                            newIngredients.add(ingredient.strIngredient.toString())
                        }

                        callback(newIngredients)
                    } else {
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<IngredientsDTO?>, t: Throwable) {
                    downloadingState(false)
                    callback(null)
                }
            })
        }

        fun getRecipesByIngredientFromApi(
            ingredient: String,
            callback: (List<RecipeSummaryDTO>?, Boolean) -> Unit,
            downloadingState: (Boolean) -> Unit
        ) {
            val restApiClient = ServiceGenerator.createService(RestAPI::class.java)
            downloadingState(true)
            restApiClient.getRecipesByIngredient(ingredient).enqueue(object : Callback<RecipesByIngredientDTO?> {
                override fun onResponse(call: Call<RecipesByIngredientDTO?>, response: Response<RecipesByIngredientDTO?>) {
                    downloadingState(false)
                    if (response.isSuccessful) {
                        val recipesByIngredientDTO = response.body()
                        val meals = recipesByIngredientDTO?.meals.orEmpty()
                        callback(meals, true)
                    } else {
                        callback(null, false)
                    }
                }

                override fun onFailure(call: Call<RecipesByIngredientDTO?>, t: Throwable) {
                    downloadingState(false)
                    callback(null, false)
                }
            })
        }

        fun getRecipeDetailsByIdFromApi(
            recipeId: String,
            callback: (List<RecipeDetailsDTO>?, Boolean) -> Unit,
            downloadingState: (Boolean) -> Unit
        ) {
            val restApiClient = ServiceGenerator.createService(RestAPI::class.java)
            downloadingState(true)
            restApiClient.getRecipeDetailsById(recipeId).enqueue(object : Callback<RecipesByIdDTO?> {
                override fun onResponse(call: Call<RecipesByIdDTO?>, response: Response<RecipesByIdDTO?>) {
                    downloadingState(false)
                    if (response.isSuccessful) {
                        val recipesByIdDTO = response.body()
                        val details = recipesByIdDTO?.meals.orEmpty()
                        callback(details, true)
                    } else {
                        callback(null, false)
                    }
                }

                override fun onFailure(call: Call<RecipesByIdDTO?>, t: Throwable) {
                    downloadingState(false)
                    callback(null, false)
                }
            })
        }

        private var recipeDao: RecipeDao? = null

        fun insertRecipeDetails(recipe: RecipeDetails) {
            Database.databaseWriteExecutor.execute(Runnable {
                recipeDao?.insertRecipe(recipe)
            })
        }

        fun deleteRecipeDetails(recipe: RecipeDetails) {
            Database.databaseWriteExecutor.execute(Runnable {
                recipeDao?.deleteRecipe(recipe.idMeal.toString())
            })
        }

        val searchResults = MutableLiveData<RecipeDetails>()
        fun findRecipe(recipeId: String) {
            Database.databaseWriteExecutor.execute(Runnable {
                searchResults.postValue(recipeDao?.getRecipeById(recipeId))
            })
        }

        val allRecipes = MutableLiveData<List<RecipeDetails>>()
        fun getAllRecepies() {
            Database.databaseWriteExecutor.execute(Runnable {
              allRecipes.postValue(recipeDao?.getAllRecipes())
            })
        }

    }

    init {
//        val db: Database? = application?.let {
//            Database.getDatabase(it.applicationContext) }
        recipeDao = Database.getDatabase(context)?.recipeDao()
//        allRecipes = recipeDao?.allRecipes
//        recipeDao = db?.recipeDao()
    }
}