package si.uni_lj.fri.pbd.miniapp3.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import si.uni_lj.fri.pbd.miniapp3.TwoColumnRecyclerView
import si.uni_lj.fri.pbd.miniapp3.models.Mapper
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeSummaryDTO

class FavoritesFragment : Fragment() {
    private var recipeSummaries: List<RecipeSummaryDTO> by mutableStateOf(emptyList())
    private var fvm: FavoritesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fvm = FavoritesViewModel(application = requireActivity().application)
        observerSetup()
    }

    override fun onStart() {
        super.onStart()
        fvm?.getAllRecipes()
    }

    private fun observerSetup() {
        fvm?.allRecipesLive?.observe(this) { recepies ->
            var tmplist: ArrayList<RecipeSummaryDTO> = ArrayList<RecipeSummaryDTO>()
            if(recepies != null) {
                for (recipe in recepies) {
                    tmplist.add(Mapper.mapRecipeDetailsToRecipeDetailsDTO(recipe))
                }
                recipeSummaries = tmplist
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                Column() {
                    Box(modifier = Modifier.fillMaxSize()) {
                        TwoColumnRecyclerView(ingredient =  "", summaries = recipeSummaries, context = context, calledFrom = "FavoritesFragment", isDownloading = false)
                    }
                }
            }
        }
    }
}