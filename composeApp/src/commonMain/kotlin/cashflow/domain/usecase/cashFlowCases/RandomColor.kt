package cashflow.domain.usecase.cashFlowCases

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import kotlin.random.Random


class RandomColor {
    operator  fun invoke(): Color {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        val alpha = Random.nextInt(256) // Optional: Adjust alpha for transparency
        if(Color(red, green, blue) == Color.White){
            return Color.Cyan
        }
        return Color(red, green, blue)
    }

}