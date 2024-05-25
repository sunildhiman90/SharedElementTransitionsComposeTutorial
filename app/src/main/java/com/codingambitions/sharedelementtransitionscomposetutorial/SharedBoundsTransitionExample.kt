package com.codingambitions.sharedelementtransitionscomposetutorial

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingambitions.sharedelementtransitionscomposetutorial.ui.theme.LavenderLight


@OptIn(ExperimentalSharedTransitionApi::class)
val boundsTransform = BoundsTransform { initialBounds, targetBounds ->
    spring(stiffness = Spring.StiffnessVeryLow)
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun SharedBoundsTransitionExample(modifier: Modifier = Modifier) {
    var showDetails by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable {
            showDetails = false
        }) {

        Text(
            modifier = Modifier.padding(16.dp),
            text = "Shared Bounds Transitions",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        SharedTransitionLayout {
            AnimatedContent(
                showDetails,
                label = "basic_transition"
            ) { targetState ->
                if (!targetState) {
                    RecipeMenu(
                        onShowDetails = {
                            showDetails = true
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this
                    )
                } else {
                    MenuContent(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this
                    )
                }
            }
        }


    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun RecipeMenu(
    onShowDetails: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {


    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .padding(vertical = 66.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = "bounds"),
                            animatedVisibilityScope = animatedContentScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            boundsTransform = boundsTransform
                        )
                        .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .background(LavenderLight, RoundedCornerShape(8.dp))
                        .clickable {
                            onShowDetails()
                        }
                        .padding(8.dp)
                ) {

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Recipe Menu",
                        fontSize = 16.sp,
                    )

                }

            }
        }
    }


}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MenuContent(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .padding(top = 66.dp, start = 16.dp, end = 16.dp)
                .sharedBounds(
                    rememberSharedContentState(key = "bounds"),
                    animatedVisibilityScope = animatedContentScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    boundsTransform = boundsTransform
                )
                .width(250.dp)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .background(LavenderLight, RoundedCornerShape(8.dp))
        ) {
            repeat(4) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Item $it: 5 Qty",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)

                    )
                }
            }

        }

    }


}

