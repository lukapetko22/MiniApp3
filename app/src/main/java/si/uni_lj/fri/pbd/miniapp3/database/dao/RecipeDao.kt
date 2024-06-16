package si.uni_lj.fri.pbd.miniapp3.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    fun getRecipeById(idMeal: String?): RecipeDetails?

    @Query("SELECT * FROM RecipeDetails")
    fun getAllRecipes(): List<RecipeDetails>

    @Insert
    fun insertRecipe(recipe: RecipeDetails)

    @Query("DELETE FROM RecipeDetails WHERE idMeal = :idMeal")
    fun deleteRecipe(idMeal: String)
}