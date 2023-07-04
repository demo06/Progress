package funny.buildapp.progress.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Click without wave
 *
 * @param onClick
 * @receiver
 */
fun Modifier.clickWithoutWave(onClick: () -> Unit) = composed {
    Modifier.clickable(
        onClick = { onClick.invoke() },
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        })
}
