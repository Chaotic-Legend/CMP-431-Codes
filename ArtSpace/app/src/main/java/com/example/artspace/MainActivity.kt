package com.example.artspace

// Project 1 YouTube Video Link: https://youtu.be/5Y3auixVY_A

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.artspace.data.ArtworkRepository
import com.example.artspace.model.Art
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ArtSpaceTheme {
                NavHost(
                    navController = navController, startDestination = Screen.Home.route + "/{id}") {
                    composable(
                        route = Screen.Home.route + "/{id}", arguments = listOf(navArgument("id") {
                            type = NavType.IntType
                            defaultValue = 0
                        } )
                    ) {
                        HomePage(navController)
                    }
                    composable(
                        route = Screen.Artist.route + "/{id}", arguments = listOf(navArgument("id") {
                            type = NavType.IntType
                        } )
                    ) {
                        ArtistPage(navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController) {
    var current by remember {
        mutableIntStateOf(navController.currentBackStackEntry?.arguments?.getInt("id") ?: 0)
    }
    val art = ArtworkRepository.artworks[current]
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Art Space",
                        fontSize = 24.sp,
                        color = Color(0xFF495D92),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFDAE2FF),
                    titleContentColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color(0xFFFEFBFF))
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            ArtworkWall(navController, art, current)
            Spacer(modifier = Modifier.weight(1f))
            ArtDescription(art, navController, current)
            Spacer(modifier = Modifier.height(4.dp))
            ArtNavigation(current) {
                current = if (it !in 0 until ArtworkRepository.artworks.size) 0 else it
            }
        }
    }
}

@Composable
fun ArtistPage(navController: NavHostController) {
    val id = navController.currentBackStackEntry?.arguments?.getInt("id") ?: 0
    val art = ArtworkRepository.artworks[id]
    val scrollState = rememberScrollState()
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color(0xFFFEFBFF))
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            ArtistProfile(art)
            Spacer(modifier = Modifier.height(32.dp))
            ArtistBio(art.artistBioId)
            Spacer(modifier = Modifier.height(32.dp))
            ArtistBackNavigation(navController, id)
        }
    }
}

@Composable
fun ArtworkWall(navController: NavHostController, art: Art, current: Int) {
    Image(
        painter = painterResource(id = art.artworkImageId),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .border(8.dp, color = Color(0xFFDFE1EC))
            .clickable {
                navController.navigate(Screen.Artist.route + "/$current")
            }
    )
}

@Composable
fun ArtDescription(art: Art, navController: NavHostController, current: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(art.titleId),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable {
                    navController.navigate(Screen.Artist.route + "/$current")
                }
        )
        Text(text = "${stringResource(art.artistId)} ${stringResource(art.yearId)}")
    }
}

@Composable
fun ArtNavigation(current: Int, move: (Int) -> Unit) {
    val lastIndex = ArtworkRepository.artworks.size - 1
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { move(current - 1) },
            modifier = Modifier
                .height(48.dp)
                .width(142.dp),
            enabled = current > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (current > 0) Color(0xFF495D92) else Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text(text = "Previous")
        }
        Spacer(modifier = Modifier.width(32.dp))

        Button(
            onClick = { move(current + 1) },
            modifier = Modifier
                .height(48.dp)
                .width(142.dp),
            enabled = current < lastIndex,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (current < lastIndex) Color(0xFF495D92) else Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text(text = "Next")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ArtistProfile(art: Art) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(3.dp, color = Color(0xFF495D92), CircleShape)
                .border(6.dp, color = Color(0xFFFEFBFF), CircleShape)
        ) {
            Image(
                painter = painterResource(art.artistImageId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(art.artistId),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(art.artistInfoId),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ArtistBio(bioId: Int) {
    Text(text = stringResource(bioId))
    Spacer(modifier = Modifier.width(32.dp))
}

@Composable
fun ArtistBackNavigation(navController: NavHostController, id: Int) {
    Button(
        onClick = { navController.navigate(Screen.Home.route + "/$id") },
        modifier = Modifier
            .height(50.dp)
            .width(100.dp)
    ) {
        Text(text = "Back")
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ArtSpaceTheme {
        val navController = rememberNavController()
        HomePage(navController = navController)
        // ArtistPage(navController = navController)
    }
}