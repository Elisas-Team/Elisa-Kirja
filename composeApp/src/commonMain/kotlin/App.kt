import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.AppTheme
import ui.AllBooksScreen
import ui.HomeScreen
import ui.NavBar

@Composable
@Preview
fun App() {
    val isDarkTheme = rememberSaveable { mutableStateOf(false) }

    AppTheme(isDarkTheme = isDarkTheme.value, toggleTheme = { isDarkTheme.value = !isDarkTheme.value }) {
        Column (modifier = Modifier.background(MaterialTheme.colors.background)){
            NavBar().DisplayNavBar(isDarkTheme = isDarkTheme.value, toggleTheme = { isDarkTheme.value = !isDarkTheme.value })
            Navigator(screen = HomeScreen())
        }
    }
}





