package com.example.pokectcollection.ui.achievements.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokectcollection.R
import kotlinx.coroutines.delay

/**
 *
 * viewModel the [AchievementLabel] function that displays an achievement label with animation.
 * @param achievementImage The URL of the achievement image.
 * @param achievementName The name of the achievement.
 */
@Composable
fun AchievementLabel(
    @DrawableRes achievementImageRes: Int,
    achievementName: String
) {
    var showInitialText by remember { mutableStateOf(true) }
    var showAchievementName by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }
    val displayedText = remember { mutableStateListOf<Char>() }

    LaunchedEffect(Unit) {
        delay(2000)
        showInitialText = false
        showAchievementName = true
        achievementName.forEach { char ->
            displayedText.add(char)
            delay(60)
        }
        delay(3000)
        isVisible = false
    }

    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 120.dp) // Set a maximum height for the component
                .padding(16.dp) // Padding to avoid sticking to the edges
                .clip(RoundedCornerShape(16.dp)) // Rounded corners
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.95f)) // Improved background
                .shadow(4.dp, RoundedCornerShape(16.dp)) // Add shadow
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Padding for inner content
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(id = achievementImageRes),
                    contentDescription = stringResource(R.string.imagen_de_logro),
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterEnd),
                    contentScale = ContentScale.Fit // Adjust the image size to be fully visible
                )

                if (showInitialText) {
                    Text(
                        text = stringResource(R.string.logro_conseguido),
                        color = Color.White, // White text color
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold, // Bold text
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(end = 70.dp) // Space for the image
                    )
                }
                if (showAchievementName) {
                    Text(
                        text = displayedText.joinToString(""),
                        color = Color.White, // White text color
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium, // Medium weight text
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(end = 70.dp) // Space for the image
                    )
                }
            }
        }
    }
}