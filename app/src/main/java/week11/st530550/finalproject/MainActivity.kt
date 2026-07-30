// Purpose: app entry point — hosts the themed root Composable and the nav graph.
// Author: Harrison Dsouza
package week11.st530550.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import week11.st530550.finalproject.navigation.CampusLostFoundNavGraph
import week11.st530550.finalproject.ui.theme.CampusLostFoundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusLostFoundApp()
        }
    }
}

@Composable
fun CampusLostFoundApp() {
    CampusLostFoundTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CampusLostFoundNavGraph()
        }
    }
}
