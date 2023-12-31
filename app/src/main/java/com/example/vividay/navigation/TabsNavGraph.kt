package com.example.vividay.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vividay.models.Day
import com.example.vividay.presentation.data_screen.DataDetailScreen
import com.example.vividay.presentation.data_screen.DataScreen
import com.example.vividay.presentation.data_screen.DataViewModel
import com.example.vividay.presentation.data_screen.InputScreen
import com.example.vividay.presentation.profile.ProfileScreen
import com.example.vividay.presentation.sign_in.GoogleAuthUiClient
import com.example.vividay.sealed.DataState
import com.example.vividay.sealed.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsNavGraph(
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    applicationContext: Context = LocalContext.current,
    dataViewModel: DataViewModel
) {
    val modifier = Modifier
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp)),
                containerColor = Color(0xFF644FDB)
            ) {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        label = {
                            Text(
                                text = stringResource(screen.resource),
                                color = if (currentDestination?.hierarchy?.any {
                                        it.route == screen.route
                                    } == true) Color.White else Color.Gray, // Set the font color
                                fontSize = if (currentDestination?.hierarchy?.any {
                                        it.route == screen.route
                                    } == true) 17.sp else 16.sp // Increase font size if selected
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = when (screen.route) {
                                    "data" -> Icons.Default.Home
                                    "profile" -> Icons.Default.AccountBox
                                    else -> Icons.Default.Star
                                },
                                contentDescription = null
                            )
                        },
                        onClick = {
                            navController.navigate(screen.route) {

                                // findStartDestination will find the actual start destination of the graph,
                                // handling cases where the graph's starting destination is itself a NavGraph
                                popUpTo(navController.graph.findStartDestination().id) {

                                    // saveState = true will save that state before it clears backstack entries up to popUpTo ID.
                                    saveState = true
                                }

                                launchSingleTop = true

                                // restoreState = true will restore that backstack
                                restoreState = true

                            }
                        },
                    )
                }
            }
        }
    )  { innerPadding ->
        // Create a state to track data saved status
        var isDataSaved by remember { mutableStateOf(false) }

        NavHost(
            navController = navController,
            startDestination = "data",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Define your composable screens here
            composable("data") {
                DataScreen(viewModel = dataViewModel, navHostController = navController)
            }
            composable(
                "dataDetail/{dateTime}",
                arguments = listOf(navArgument("dateTime") { type = NavType.StringType })
            ) { backStackEntry ->
                val dateTime = backStackEntry.arguments?.getString("dateTime")
                val selectedDay = dataViewModel.getSpecificDay(dateTime)
                DataDetailScreen(navController, day = selectedDay)
            }
            composable("input") {
                InputScreen(
                    onSaved = {
                        dataViewModel.fetchDataFromFirebase()
                    },
                    onSavedCallback = {
                        // Handle the data saved event, e.g., set a flag
                        isDataSaved = true
                        // Check if data is saved, and navigate accordingly
                        if (isDataSaved) {
                            navController.navigate("data")
                        }
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = {
                        lifecycleOwner.lifecycleScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                applicationContext,
                                "Sign out",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}

val items = listOf(
    Screen.Home,
    Screen.Create,
    Screen.Profile,
)