package org.david.notes


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.david.notes.data.cache.DataStoreManager
import org.david.notes.data.db.NoteDatabase
import org.david.notes.feature.auth.SignInScreen
import org.david.notes.feature.auth.SignUpScreen
import org.david.notes.feature.home.HomeScreen
import org.david.notes.feature.profile.UserProfile
import org.david.notes.ui.QuickNotesAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(database: NoteDatabase, dataStoreManager: DataStoreManager) {
    QuickNotesAppTheme {

        val navController = rememberNavController()

        NavHost(navController, startDestination = "home") {
            composable(route = "home") {
                HomeScreen(database,dataStoreManager , navController)
            }

            composable(route = "signup") {
                SignUpScreen(dataStoreManager, navController)
            }

            composable(route = "signin") {
                SignInScreen(dataStoreManager, navController)
            }

            composable(route = "profile") {
                UserProfile(navController, dataStoreManager)
            }
        }

    }
}