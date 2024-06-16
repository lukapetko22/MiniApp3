package si.uni_lj.fri.pbd.miniapp3.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.models.Mapper
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeSummaryDTO

class DetailsActivity : AppCompatActivity() {
    private var dvm: DetailsViewModel? = null

    private var isDownloading: Boolean by mutableStateOf(false)
    private var isError: Boolean by mutableStateOf(false)
    private var currentDetails: RecipeDetailsDTO? = null

    private lateinit var idFromIntent: String
    private var recipeSummaries: List<RecipeSummaryDTO> by mutableStateOf(emptyList())

    private var isFavourite: Boolean by mutableStateOf(false)
    private var mealTitle: String by mutableStateOf("")
    private var mealDrinkAlternate: String by mutableStateOf("")
    private var mealCategory: String by mutableStateOf("")
    private var mealArea: String by mutableStateOf("")
    private var mealInstructions: String by mutableStateOf("")
    private var mealImageUrl: String by mutableStateOf("")
    private var mealTags: String by mutableStateOf("")
    private var mealYoutube: String by mutableStateOf("")
    private var mealSource: String by mutableStateOf("")
    private var mealIngredients: String by mutableStateOf("")
//    private var mealMeasurements: String by mutableStateOf("")

    private lateinit var calledFrom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dvm = DetailsViewModel(application)
        calledFrom = intent.getStringExtra("calledFrom").toString()
        idFromIntent = intent.getStringExtra("idMeal").toString()

        observerSetup()
        GetDetails(idFromIntent)
        CheckIfFavourite(idFromIntent)

        lifecycleScope.launch {
            while (true) {
                CheckIfFavourite(idFromIntent)
                delay(1000L)
            }
        }

        setContent {
            ConnectionErrorToast(application)
            DrawDetails(context = application)
        }
    }

    override fun onStart() {
        super.onStart()
        dvm?.getAllRecepies()
    }

    @Composable
    fun ConnectionErrorToast(
        context: Context
    ) {
        if (isError) {
            LaunchedEffect(isError) {
                Toast.makeText(context, "There has been a connection error!", Toast.LENGTH_SHORT).show()
                isError = false
            }
        }
    }

    @Composable
    fun DrawDetails(
        context: Context
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mealImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp)),
                placeholder = painterResource(R.drawable.meal),
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$mealTitle",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (mealArea != "null" && mealArea.length > 0) {
                    Text(
                        text = "Meal Area:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Magenta,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = mealArea,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                if (mealDrinkAlternate != "null" && mealDrinkAlternate.length > 0) {
                    Text(
                        text = "Meal Drink Alternate:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Magenta,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = mealDrinkAlternate,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                if (mealCategory != "null" && mealCategory.length > 0) {
                    Text(
                        text = "Meal Category:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Magenta,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = mealCategory,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                if (mealInstructions != "null" && mealInstructions.length > 0) {
                    Text(
                        text = "Meal Instructions:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Magenta,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = mealInstructions,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                if (mealTags != "null" && mealTags.length > 0) {
                    Text(
                        text = "Meal Tags:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Magenta,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = mealTags,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

//                if (mealYoutube != "null" && mealYoutube.length > 0) {
//                    Text(
//                        text = "Meal Youtube:",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Magenta,
//                        modifier = Modifier.padding(horizontal = 16.dp)
//                    )
//                    Text(
//                        text = mealYoutube,
//                        fontSize = 16.sp,
//                        modifier = Modifier.padding(horizontal = 16.dp)
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                }

//                if (mealSource != "null" && mealSource.length > 0) {
////                    Text(
////                        text = "Meal Source:",
////                        fontSize = 18.sp,
////                        fontWeight = FontWeight.Bold,
////                        color = Color.Magenta,
////                        modifier = Modifier.padding(horizontal = 16.dp)
////                    )
////                    Text(
////                        text = mealSource,
////                        fontSize = 16.sp,
////                        modifier = Modifier.padding(horizontal = 16.dp)
////                    )
//                    val text = buildAnnotatedString {
//                        withStyle(style = SpanStyle(color = Color.Blue, fontSize = 16.sp)) {
//                            pushStringAnnotation(tag = "URL", annotation = mealSource)
//                            append(mealSource)
//                            pop()
//                        }
//                    }
//
//                    ClickableText(
//                        text = text,
//                        onClick = { offset ->
//                            text.getStringAnnotations(tag = "URL", start = offset, end = offset)
//                                .firstOrNull()?.let { annotation ->
//                                    val uri = Uri.parse(annotation.item)
//                                    val intent = Intent(Intent.ACTION_VIEW, uri)
//                                    context.startActivity(intent)
//                                }
//                        }
//                    )
//
//                    Spacer(modifier = Modifier.height(5.dp))
//                }

                if (mealIngredients != "null" && mealIngredients.length > 0) {
                    Text(
                        text = "Meal Ingredients:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Magenta,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = mealIngredients,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = { UpdateFavourites(currentDetails) }) {
                    if (isFavourite)
                        Text("Remove from favourites")
                    else
                        Text("Add to favourites")
                }
            }
        }
    }

    private fun observerSetup() {
        dvm?.searchResultsLive?.observe(this) { recipe ->
            if(recipe == null)
                isFavourite = false
            else
                isFavourite = true
        }

        dvm?.allRecipesLive?.observe(this) { recepies ->
            if(recepies != null) {
                for (recipe in recepies) {
                    if (recipe.idMeal == idFromIntent) {
                        var recipeIM = Mapper.mapRecipeDetailsToRecipeDetailsIm(true, recipe)
                        currentDetails = Mapper.IMtoDto(recipeIM)
                        ExtractMealData(Mapper.IMtoDto(recipeIM))
                        break
                    }
                }
            }
        }

        dvm?.currentDetailsLive?.observe(this) { detailsLive ->
            if(detailsLive != null) {
                ExtractMealData(detailsLive)
                currentDetails = detailsLive
            }
        }

        dvm?.isDownloadingLive?.observe(this) { isDownloadingLive ->
            if(isDownloadingLive != null) {
                isDownloading = isDownloadingLive
            }
        }

        dvm?.errorLive?.observe(this) { isErrorLive ->
            isError = isErrorLive
        }
    }

    private fun CheckIfFavourite(recipeId: String) {
        dvm?.CheckIfFavourite(recipeId)
    }

    private fun UpdateFavourites(details: RecipeDetailsDTO?) {
        dvm?.UpdateFavourites(details, isFavourite)
    }

    private fun GetDetails(recipeId: String) {
        dvm?.GetDetails(recipeId, calledFrom)
    }

    private fun ExtractMealData(details: RecipeDetailsDTO)
    {
        mealTitle = details.strMeal.toString()
        mealDrinkAlternate = details.strDrinkAlternate.toString()
        mealCategory = details.strCategory.toString()
        mealArea = details.strArea.toString()
        mealInstructions = details.strInstructions.toString()
        mealImageUrl = details.strMealThumb.toString()
        mealTags = details.strTags.toString()
        mealYoutube = details.strYoutube.toString()
        mealSource = details.strSource.toString()

        mealIngredients = ""

        if(!details.strIngredient1.isNullOrBlank() && details.strIngredient1 != "null") {
            mealIngredients += "${details.strIngredient1} - ${details.strMeasure1}\n"
        }

        if(!details.strIngredient2.isNullOrBlank() && details.strIngredient2 != "null") {
            mealIngredients += "${details.strIngredient2} - ${details.strMeasure2}\n"
        }

        if(!details.strIngredient3.isNullOrBlank() && details.strIngredient3 != "null") {
            mealIngredients += "${details.strIngredient3} - ${details.strMeasure3}\n"
        }

        if(!details.strIngredient4.isNullOrBlank() && details.strIngredient4 != "null") {
            mealIngredients += "${details.strIngredient4} - ${details.strMeasure4}\n"
        }

        if(!details.strIngredient5.isNullOrBlank() && details.strIngredient5 != "null") {
            mealIngredients += "${details.strIngredient5} - ${details.strMeasure5}\n"
        }

        if(!details.strIngredient6.isNullOrBlank() && details.strIngredient6 != "null") {
            mealIngredients += "${details.strIngredient6} - ${details.strMeasure6}\n"
        }

        if(!details.strIngredient7.isNullOrBlank() && details.strIngredient7 != "null") {
            mealIngredients += "${details.strIngredient7} - ${details.strMeasure7}\n"
        }

        if(!details.strIngredient8.isNullOrBlank() && details.strIngredient8 != "null") {
            mealIngredients += "${details.strIngredient8} - ${details.strMeasure8}\n"
        }

        if(!details.strIngredient9.isNullOrBlank() && details.strIngredient9 != "null") {
            mealIngredients += "${details.strIngredient9} - ${details.strMeasure9}\n"
        }

        if(!details.strIngredient10.isNullOrBlank() && details.strIngredient10 != "null") {
            mealIngredients += "${details.strIngredient10} - ${details.strMeasure10}\n"
        }

        if(!details.strIngredient11.isNullOrBlank()&& details.strIngredient11 != "null") {
            mealIngredients += "${details.strIngredient11} - ${details.strMeasure11}\n"
        }

        if(!details.strIngredient12.isNullOrBlank() && details.strIngredient12 != "null") {
            mealIngredients += "${details.strIngredient12} - ${details.strMeasure12}\n"
        }

        if(!details.strIngredient13.isNullOrBlank() && details.strIngredient13 != "null") {
            mealIngredients += "${details.strIngredient13} - ${details.strMeasure13}\n"
        }

        if(!details.strIngredient14.isNullOrBlank() && details.strIngredient14 != "null") {
            mealIngredients += "${details.strIngredient14} - ${details.strMeasure14}\n"
        }

        if(!details.strIngredient15.isNullOrBlank() && details.strIngredient15 != "null") {
            mealIngredients += "${details.strIngredient15} - ${details.strMeasure15}\n"
        }

        if(!details.strIngredient16.isNullOrBlank() && details.strIngredient16 != "null") {
            mealIngredients += "${details.strIngredient16} - ${details.strMeasure16}\n"
        }

        if(!details.strIngredient17.isNullOrBlank() && details.strIngredient17 != "null") {
            mealIngredients += "${details.strIngredient17} - ${details.strMeasure17}\n"
        }

        if(!details.strIngredient18.isNullOrBlank() && details.strIngredient18 != "null") {
            mealIngredients += "${details.strIngredient18} - ${details.strMeasure18}\n"
        }

        if(!details.strIngredient19.isNullOrBlank() && details.strIngredient19 != "null") {
            mealIngredients += "${details.strIngredient19} - ${details.strMeasure19}\n"
        }

        if(!details.strIngredient20.isNullOrBlank() && details.strIngredient20 != "null") {
            mealIngredients += "${details.strIngredient20} - ${details.strMeasure20}\n"
        }
    }
}