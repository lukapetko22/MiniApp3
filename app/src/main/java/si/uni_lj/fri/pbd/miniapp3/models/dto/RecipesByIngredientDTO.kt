package si.uni_lj.fri.pbd.miniapp3.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipesByIngredientDTO {
    @SerializedName("meals")
    @Expose
    val meals: List<RecipeSummaryDTO>? = null
}