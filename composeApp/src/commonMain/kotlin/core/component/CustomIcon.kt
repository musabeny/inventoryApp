package core.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomIcon(
    modifier: Modifier,
    imageVector: ImageVector
){
    Icon(
        modifier = modifier,
        imageVector = imageVector,
        contentDescription = "Date icon",
        tint = MaterialTheme.colorScheme.primary
    )
}