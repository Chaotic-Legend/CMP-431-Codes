package com.example.nycjobs

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nycjobs.ui.screens.DetailScreen
import com.example.nycjobs.ui.screens.HomeScreen
import com.example.nycjobs.ui.screens.JobPostingsUIState
import com.example.nycjobs.ui.screens.JobPostingsViewModel
import com.example.nycjobs.ui.theme.NYCJobsTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NYCJobsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: JobPostingsViewModel by viewModels { JobPostingsViewModel.Factory }
                    val navController = rememberNavController()
                    var favoriteJobIds by remember { mutableStateOf(setOf<Int>()) }
                    fun onToggleFavorite(jobId: Int) {
                        favoriteJobIds = if (favoriteJobIds.contains(jobId)) {
                            favoriteJobIds - jobId
                        } else {
                            favoriteJobIds + jobId
                        }
                    }
                    NavHost(
                        navController = navController,
                        startDestination = "Home"
                    ) {
                        composable("Home") {
                            HomeScreen(
                                viewModel = viewModel,
                                navController = navController,
                                favoriteJobIds = favoriteJobIds,
                                onToggleFavorite = ::onToggleFavorite
                            )
                        }
                        composable(
                            route = "detail/{jobId}",
                            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getInt("jobId")
                            val jobPost = (viewModel.jobPostingsUIState as? JobPostingsUIState.Success)
                                ?.data
                                ?.find { it.jobId == jobId }

                            if (jobPost != null && jobId != null) {
                                DetailScreen(
                                    jobPost = jobPost,
                                    favoriteJobIds = favoriteJobIds,
                                    onToggleFavorite = ::onToggleFavorite,
                                    navController = navController
                                )
                            } else {
                                Text("Job Not Found")
                            }
                        }
                    }
                }
            }
        }
    }
}