package org.david.notes


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.david.notes.db.NoteDatabase
import org.david.notes.feature.auth.SignInScreen
import org.david.notes.feature.auth.SignUpScreen
import org.david.notes.feature.home.HomeScreen
import org.david.notes.models.Note
import org.david.notes.notes.ListNotesScreen
import org.david.notes.ui.QuickNotesAppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import quickernotes.composeapp.generated.resources.Res
import quickernotes.composeapp.generated.resources.rafiki
import kotlin.collections.emptyList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(database: NoteDatabase) {
    QuickNotesAppTheme {

        val navController = rememberNavController()

        NavHost(navController, startDestination = "home") {
            composable(route = "home") {
                HomeScreen(database, navController)
            }

            composable(route = "signup") {
                SignUpScreen(navController)
            }

            composable(route = "signin") {
                SignInScreen(navController)
            }
        }

    }
}