package si.uni_lj.fri.pbd.miniapp3.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO

interface RestAPI {
    @get:GET("list.php?i=list")
    val allIngredients: Call<IngredientsDTO?>?

    // Endpoint to get recipes by main ingredient
    @GET("filter.php")
    fun getRecipesByIngredient(@Query("i") ingredient: String): Call<RecipesByIngredientDTO?>

    // Endpoint to get recipe details by recipe ID
    @GET("lookup.php")
    fun getRecipeDetailsById(@Query("i") recipeId: String): Call<RecipesByIdDTO?>
}