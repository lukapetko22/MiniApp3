package si.uni_lj.fri.pbd.miniapp3

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import si.uni_lj.fri.pbd.miniapp3.ui.favorites.FavoritesFragment
import si.uni_lj.fri.pbd.miniapp3.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {
    @Composable
    fun MainScreen(fragmentActivity: FragmentActivity) {
        val fragments = listOf(
            SearchFragment(),
            FavoritesFragment()
        )
        val tabTitles = listOf("Search", "Favorites")

        ViewPagerScreen(fragments = fragments, fragmentActivity = fragmentActivity, tabTitles = tabTitles)
    }

    @Composable
    fun ViewPagerScreen(
        fragments: List<Fragment>,
        fragmentActivity: FragmentActivity,
        tabTitles: List<String>
    ) {
        val currentState = remember { mutableIntStateOf(0) }
        Column {
            TabRow(
                selectedTabIndex = currentState.intValue,
            ) {
                tabTitles.forEachIndexed { i, FragmentName ->
                    Tab(
                        text = { Text(FragmentName) },
                        selected = currentState.intValue == i,
                        onClick = {
                            currentState.intValue = i
                        }
                    )
                }
            }

            AndroidView(
                factory = { context ->
                    ViewPager2(context).apply {
                        adapter = SectionsPagerAdapter(fragmentActivity, fragments)
                        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                currentState.intValue = position
                            }
                        })
                    }
                },
                update = { viewPager ->
                    if (viewPager.currentItem != currentState.intValue) {
                        viewPager.currentItem = currentState.intValue
                    }
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Google
            MainScreen(fragmentActivity = this)
        }

//        enableEdgeToEdge()
//
//        setContentView(R.layout.activity_main)
//
//        val sectionsPagerAdapter = SectionsPagerAdapter(this)
//        var viewPager = findViewById<ViewPager2>(R.id.viewPager)
//        var tabs = findViewById<TabLayout>(R.id.tabs)
//        viewPager.adapter = sectionsPagerAdapter
//
//        TabLayoutMediator(tabs, viewPager) { tab, position ->
//            tab.text = when (position) {
//                SectionsPagerAdapter.TAB_ONE_INDEX -> "Search"
//                SectionsPagerAdapter.TAB_TWO_INDEX -> "Favorites"
//                // Add more tab titles if needed
//                else -> null
//            }
//        }.attach()

//        val searchFragment = SearchFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, searchFragment)
//            .commit()
    }
}