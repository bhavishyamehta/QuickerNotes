package org.david.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.david.notes.data.cache.DataStoreManager
import org.david.notes.data.db.getNoteDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                database = getNoteDatabase(getDatabaseBuilder(this@MainActivity)),
                dataStoreManager = DataStoreManager(createDataStorePath(this@MainActivity))
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val database = getNoteDatabase(getDatabaseBuilder(LocalContext.current))
    val dataStoreManager = DataStoreManager(createDataStorePath(LocalContext.current))
    App(database, dataStoreManager)
}