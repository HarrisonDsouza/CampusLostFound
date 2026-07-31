// Purpose: shared bottom navigation bar (Browse / My Posts) matching the Figma BottomNav component.
// Author: Harrison Dsouza
package week11.st530550.finalproject.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class BottomNavTab { BROWSE, MY_POSTS }

@Composable
fun BottomNavBar(
    selected: BottomNavTab,
    onBrowseClick: () -> Unit,
    onMyPostsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            NavTabItem(
                label = "Browse",
                icon = Icons.Filled.Search,
                active = selected == BottomNavTab.BROWSE,
                onClick = onBrowseClick,
            )
            NavTabItem(
                label = "My Posts",
                icon = Icons.AutoMirrored.Filled.List,
                active = selected == BottomNavTab.MY_POSTS,
                onClick = onMyPostsClick,
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (active) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)
    }
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = tint)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = tint)
    }
}
