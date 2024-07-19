
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import core.navigation.ScreenNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope


@Composable
@Preview
fun App() {
    MaterialTheme {
        KoinContext {
            ScreenNavigation()
        }

    }
}

