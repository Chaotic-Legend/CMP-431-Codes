package com.example.nycjobs.ui.screens
// Project 2 YouTube Video Link: https://youtu.be/KyRAwI53SOk

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nycjobs.model.JobPost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    jobPost: JobPost,
    favoriteJobIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val isFavorite = favoriteJobIds.contains(jobPost.jobId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(jobPost.businessTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Agency: ${jobPost.agency}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Job Category: ${jobPost.jobCategory}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Career Level: ${jobPost.careerLevel}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Employment Type: ${if (jobPost.fullOrPartTime == 'F') "Full-Time" else "Part-Time"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "Number of Positions: ${jobPost.numOfOpenPositions}", style = MaterialTheme.typography.bodyMedium)

            HorizontalDivider()

            Text(text = "Salary Range:", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "$${jobPost.salaryRangeFrom} - $${jobPost.salaryRangeTo} (${jobPost.salaryFrequency})",
                style = MaterialTheme.typography.bodyMedium
            )

            HorizontalDivider()

            Text(text = "Work Location:", style = MaterialTheme.typography.titleMedium)
            Text(text = jobPost.agencyLocation.ifEmpty { jobPost.workLocation }, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Division / Work Unit: ${jobPost.divisionWorkUnit}", style = MaterialTheme.typography.bodyMedium)

            HorizontalDivider()

            Text(text = "Job Description:", style = MaterialTheme.typography.titleMedium)
            Text(text = jobPost.jobDescription.ifEmpty { "Not Available." }, style = MaterialTheme.typography.bodyMedium)

            Text(text = "Minimum Qualifications:", style = MaterialTheme.typography.titleMedium)
            Text(text = jobPost.minRequirement.ifEmpty { "Not Specified." }, style = MaterialTheme.typography.bodyMedium)

            Text(text = "Preferred Skills:", style = MaterialTheme.typography.titleMedium)
            Text(text = jobPost.preferredSkills.ifEmpty { "Not Specified." }, style = MaterialTheme.typography.bodyMedium)

            Text(text = "Additional Information:", style = MaterialTheme.typography.titleMedium)
            Text(text = jobPost.additionalInfo.ifEmpty { "None." }, style = MaterialTheme.typography.bodyMedium)

            HorizontalDivider()

            Text(text = "How To Apply:", style = MaterialTheme.typography.titleMedium)
            Text(text = jobPost.toApply.ifEmpty { "Not Provided." }, style = MaterialTheme.typography.bodyMedium)

            HorizontalDivider()

            Text(text = "Posting Date: ${jobPost.postingDate}", style = MaterialTheme.typography.bodySmall)
            if (jobPost.postUntil.isNotEmpty()) {
                Text(text = "Post Until: ${jobPost.postUntil}", style = MaterialTheme.typography.bodySmall)
            }
            Text(text = "Last Updated: ${jobPost.postingLastUpdated}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove From Favorites" else "Add To Favorites",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onToggleFavorite(jobPost.jobId) }
            )
        }
    }
}