package com.example.nycjobs.ui.screens
// Project 2 YouTube Video Link: https://youtu.be/KyRAwI53SOk

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nycjobs.R
import com.example.nycjobs.model.JobPost
import com.example.nycjobs.util.capitalizeWords
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: JobPostingsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    favoriteJobIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFavorites by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search Job Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                showFavorites = showFavorites,
                onToggleFavorites = { showFavorites = it }
            )
        }
    ) { innerPadding ->
        val modifierWithPadding = modifier.padding(innerPadding)
        when (val uiState = viewModel.jobPostingsUIState) {
            is JobPostingsUIState.Loading -> LoadingSpinner()

            is JobPostingsUIState.Success -> {
                val filteredJobs = uiState.data.filter { job ->
                    val matchesSearch = job.businessTitle.contains(searchQuery, ignoreCase = true)
                    val isFavorite = favoriteJobIds.contains(job.jobId)
                    (!showFavorites || isFavorite) && matchesSearch
                }

                JobPostList(
                    jobPostings = filteredJobs,
                    loadMoreData = { viewModel.getJobPostings() },
                    updateScrollPosition = { pos -> viewModel.setScrollingPosition(pos) },
                    scrollPosition = viewModel.getScrollPosition(),
                    navController = navController,
                    modifier = modifierWithPadding.fillMaxSize(),
                    favoriteJobIds = favoriteJobIds,
                    onToggleFavorite = onToggleFavorite
                )
            }

            is JobPostingsUIState.Error -> ToastMessage(stringResource(R.string.data_failed))
            else -> ToastMessage(stringResource(R.string.loaded))
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun JobPostList(
    jobPostings: List<JobPost>,
    loadMoreData: () -> Unit,
    updateScrollPosition: (Int) -> Unit,
    scrollPosition: Int,
    navController: NavController,
    modifier: Modifier,
    favoriteJobIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit
) {
    val firstVisibleIndex = if (scrollPosition > jobPostings.size) 0 else scrollPosition
    val listState: LazyListState = rememberLazyListState(firstVisibleIndex)

    LazyColumn(modifier = modifier, state = listState) {
        items(jobPostings) { jobPost ->
            val formattedTitle = jobPost.businessTitle.capitalizeWords()
            val isFavorite = favoriteJobIds.contains(jobPost.jobId)

            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("detail/${jobPost.jobId}")
                    },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = formattedTitle,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = jobPost.agency,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Experience: ${jobPost.careerLevel}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.Home,
                        contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                        modifier = Modifier.clickable {
                            onToggleFavorite(jobPost.jobId)
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .debounce(500L)
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                updateScrollPosition(listState.firstVisibleItemIndex)
                if (lastVisibleItemIndex >= jobPostings.size - 1) {
                    loadMoreData()
                }
            }
    }
}

@Composable
fun BottomNavigationBar(
    showFavorites: Boolean,
    onToggleFavorites: (Boolean) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = !showFavorites,
            onClick = { onToggleFavorites(false) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = showFavorites,
            onClick = { onToggleFavorites(true) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") }
        )
    }
}