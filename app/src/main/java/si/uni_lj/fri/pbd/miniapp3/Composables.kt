package si.uni_lj.fri.pbd.miniapp3

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeSummaryDTO
import si.uni_lj.fri.pbd.miniapp3.ui.DetailsActivity

@Composable
fun TwoColumnRecyclerView(
    summaries: List<RecipeSummaryDTO>,
    ingredient: String,
    context: Context,
    calledFrom: String,
    isDownloading: Boolean
) {
    if(calledFrom == "FavoritesFragment" || summaries.isNotEmpty() || ingredient == "Choose your ingredient" || isDownloading) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
//            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(summaries.count()) { i ->
                RecipeSummary(
                    recipeSummary = summaries[i],
                    context = context,
                    calledFrom = calledFrom
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No recipes for this ingredient!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RecipeSummary(
    recipeSummary: RecipeSummaryDTO,
    context: Context,
    calledFrom: String
) {
    Box (
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.LightGray)
            .fillMaxSize()
            .clickable {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("idMeal", recipeSummary.idMeal)
                intent.putExtra("calledFrom", calledFrom)
                context?.startActivity(intent)
            }
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .clip(RoundedCornerShape(5.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipeSummary.strMealThumb)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(5.dp)),
                placeholder = painterResource(R.drawable.meal)
            )
            Spacer(modifier = Modifier.height(4.dp))
            recipeSummary.strMeal?.let { Text(text = it) }
        }
    }
}