package com.seren.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.seren.app.navigation.SerenNavGraph

@Composable
fun SerenApp() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SerenNavGraph(navController = navController)
    }
}
