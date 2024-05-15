
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(screen = HomeScreen())
    }
}





