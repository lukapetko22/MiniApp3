package si.uni_lj.fri.pbd.miniapp3.ui.search

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.amitshekhar.DebugDB
import si.uni_lj.fri.pbd.miniapp3.TwoColumnRecyclerView
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeSummaryDTO

class SearchFragment : Fragment() {
    private var svm: SearchViewModel? = null

    private var ingredients_spinner: List<String> by mutableStateOf(emptyList())
    private var recipeSummaries: List<RecipeSummaryDTO> by mutableStateOf(emptyList())
    private var isDownloading: Boolean by mutableStateOf(false)
    private var selected: String by mutableStateOf("Choose your ingredient")
    private var isError: Boolean by mutableStateOf(false)
    private var errMsg: String by mutableStateOf("")

    private var isRefreshing by mutableStateOf(false)
    private var lastRefreshTime = System.currentTimeMillis()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Database Address: " + DebugDB.getAddressLog());
        svm = SearchViewModel(application = requireActivity().application)
        observerSetup()
        populateSpinner()

        return ComposeView(requireContext()).apply {
            setContent {
                val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { refreshAPI() })

                ErrorToast(context)

                Box (
                    modifier = Modifier
                        .pullRefresh(pullRefreshState)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.pullRefresh(pullRefreshState)
                    ) {
                        Spinner(
                            ingredientList = ingredients_spinner,
                            selectedIngredient = selected,
                            onIngredientSelected = { selected = it },
                            isEnabled = !isDownloading
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState)) {
                            TwoColumnRecyclerView(
                                ingredient = selected,
                                summaries = recipeSummaries,
                                context = context,
                                calledFrom = "SearchFragment",
                                isDownloading = isDownloading
                            )
                            progressCircle(isDisplayed = isDownloading)
                        }

//                    PullRefreshIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), refreshing = isRefreshing, state = pullRefreshState)

                    }

                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter),
                        backgroundColor = if (isRefreshing) Color.Red else Color.Green,
                    )
                }
                onIngredientPick(selected)
            }
        }
    }

    private fun refreshAPI() {
        if(System.currentTimeMillis() - lastRefreshTime < 5000) {
            isError = true
            errMsg = "You can only refresh once in 5s!"
            return
        }

        lastRefreshTime = System.currentTimeMillis()

        isRefreshing = true

        recipeSummaries = emptyList()
        ingredients_spinner = emptyList()

        var prevsel = selected
        if(prevsel != "Choose your ingredient") {
            svm?.lastPicked = ""
            onIngredientPick(prevsel)
        }

        populateSpinner()

        isRefreshing = false
    }

    @Composable
    fun ErrorToast(
        context: Context
    ) {
        if (isError) {
            LaunchedEffect(isError) {
                Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
                isError = false
            }
        }
    }

    // Youtube
    @Composable
    fun Spinner(
        ingredientList: List<String>,
        selectedIngredient: String,
        onIngredientSelected: (selectedItem: String) -> Unit,
        isEnabled: Boolean = true
    ) {
        var expanded by rememberSaveable() { mutableStateOf(false) }

        OutlinedButton(
            onClick = {expanded = true},
            enabled = (!ingredientList.isEmpty() && isEnabled),
            modifier = Modifier.padding(8.dp)
        ) {
            Text (
                text = selectedIngredient,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                ingredientList.forEach {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onIngredientSelected(it)
                    }, text = {
                        Text(text = it)
                    })
                }
            }
        }
    }

    @Composable
    fun progressCircle(
        isDisplayed: Boolean = false
    ){
        if (!isDisplayed)
            return
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator()
        }
    }


    private fun observerSetup() {
        svm?.recipeSummariesLive?.observe(viewLifecycleOwner) { recipes ->
            if(recipes != null) {
                recipeSummaries = recipes
            }
        }

        svm?.ingredientsLive?.observe(viewLifecycleOwner) { ingredients ->
            if(ingredients != null) {
                ingredients_spinner = ingredients
            }
        }

        svm?.isDownloadingLive?.observe(viewLifecycleOwner) { isDownloadingL ->
            if (isDownloadingL != null) {
                isDownloading = isDownloadingL
            }
        }

        svm?.errorLive?.observe(viewLifecycleOwner) { isErrorLive ->
            isError = isErrorLive
            errMsg = "There has been a connection error!"
        }
    }

    private fun populateSpinner() {
        svm?.getIngredientsFromApi()
    }

    private fun onIngredientPick(ingredient: String) {
        if(ingredient != "Choose your ingredient")
            svm?.getRecipesByIngredientFromApi(ingredient)
    }
}