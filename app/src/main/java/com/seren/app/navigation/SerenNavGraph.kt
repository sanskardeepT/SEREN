package com.seren.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.seren.app.ui.consent.ConsentScreen
import com.seren.app.ui.home.HomeScreen
import com.seren.app.ui.practice.PracticeScreen
import com.seren.app.ui.screening.ScreeningScreen
import com.seren.app.ui.splash.SplashScreen

object Destinations {
    const val SPLASH = "splash"
    const val CONSENT = "consent"
    const val HOME = "home"
    const val SCREENING = "screening"
    const val PRACTICE = "practice"
}

@Composable
fun SerenNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SPLASH
    ) {
        composable(Destinations.SPLASH) {
            SplashScreen(
                onNavigateToConsent = {
                    navController.navigate(Destinations.CONSENT) {
                        popUpTo(Destinations.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.CONSENT) {
            ConsentScreen(
                onNavigateToHome = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.CONSENT) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.HOME) {
            HomeScreen(
                onNavigateToScreening = {
                    navController.navigate(Destinations.SCREENING)
                },
                onNavigateToPractice = {
                    navController.navigate(Destinations.PRACTICE)
                }
            )
        }

        composable(Destinations.SCREENING) {
            ScreeningScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Destinations.PRACTICE) {
            PracticeScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
