package org.david.notes.feature.home

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.david.notes.HomeViewModel
import org.david.notes.db.NoteDatabase
import org.david.notes.models.Note
import org.david.notes.notes.ListNotesScreen
import org.jetbrains.compose.resources.painterResource
import quickernotes.composeapp.generated.resources.Res
import quickernotes.composeapp.generated.resources.ic_phone
import quickernotes.composeapp.generated.resources.rafiki

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(database: NoteDatabase, navController: NavController) {
    val viewModel = viewModel { HomeViewModel(database) }
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                shape = CircleShape
            ) {
                Text("+", fontSize = 18.sp)
            }
        }
    ) {
        val notes by viewModel.notes.collectAsStateWithLifecycle(emptyList())
        Column(modifier = Modifier.fillMaxWidth().padding(it)) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Notes",
                    modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.Center),
                    fontSize = 30.sp
                )
                Image(
                    painterResource(Res.drawable.ic_phone),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp).size(30.dp).clickable {
                        navController.navigate("signup")
                    }.align(Alignment.CenterEnd)
                )
            }
            if (notes.isNotEmpty()) {
                ListNotesScreen(notes)
            } else {
                EmptyView()
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                showBottomSheet = false
            }, sheetState = bottomSheetState) {
                AddItemDialog(onCancel = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    showBottomSheet = false
                }, onSave = { note ->
                    viewModel.addNote(note)
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    showBottomSheet = false
                })
            }
        }
    }
}

@Composable
fun AddItemDialog(onCancel: () -> Unit, onSave: (Note) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        val colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        )
        TextField(
            value = title,
            onValueChange = { title = it },
            colors = colors,
            placeholder = {
                Text(text = "Title", fontSize = 22.sp)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 22.sp)
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            colors = colors,
            placeholder = {
                Text(text = "Description")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5
        )
        Row(modifier = Modifier.align(Alignment.End)) {
            Text(
                text = "Cancel",
                modifier = Modifier.padding(8.dp).clickable {
                    onCancel()
                }
            )
            Text(
                text = "Save",
                modifier = Modifier.padding(8.dp).clickable {
                    onSave(Note(0, title, description))
                }
            )
        }

    }

}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painterResource(Res.drawable.rafiki),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Text(
                text = "Create your first note !",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}