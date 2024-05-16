package com.codingambitions.sharedelementtransitionscomposetutorial

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


data class User(val id: Int, val name: String, val profileDesc: String, val resourceId: Int)

val listOfUsers = listOf(
    User(1, "User 1", "", R.drawable.user1),
    User(2, "User 2", "", R.drawable.user2),
    User(3, "User 3", "", R.drawable.user3),
    User(4, "User 4", "", R.drawable.user4),
    User(5, "User 5", "", R.drawable.user5),
    User(6, "User 6", "", R.drawable.user1),
    User(7, "User 7", "", R.drawable.user2),
    User(8, "User 8", "", R.drawable.user3),
    User(9, "User 9", "", R.drawable.user4),
)

//To provide custom animation use boundsTransform
@OptIn(ExperimentalSharedTransitionApi::class)
val boundsTransform  = BoundsTransform { initialBounds, targetBounds ->
    spring(stiffness = StiffnessVeryLow)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun UsersListApp(modifier: Modifier = Modifier) {

    SharedTransitionLayout(modifier) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                ListScreen(
                    navController,
                    this@SharedTransitionLayout,
                    this
                )
            }
            composable(
                "details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                val user = listOfUsers.first { it.id == id }
                DetailScreen(
                    user,
                    this@SharedTransitionLayout,
                    this,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ListScreen(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        items(listOfUsers) { item ->
            Row(
                Modifier.clickable {
                    navController.navigate("details/${item.id}")
                }
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                with(sharedTransitionScope) {
                    Image(
                        painterResource(id = item.resourceId),
                        contentDescription = item.profileDesc,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "image-${item.id}"),
                                animatedVisibilityScope = animatedContentScope,
                                //boundsTransform = boundsTransform
                            )
                            .size(100.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        item.name, fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .sharedElement(
                                state = rememberSharedContentState(key = "title-${item.id}"),
                                animatedVisibilityScope = animatedContentScope,
                                //boundsTransform = boundsTransform
                            )
                    )
                }

            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailScreen(
    user: User,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onBack: () -> Unit,
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable {
                onBack()
            }
    ) {

        with(sharedTransitionScope) {
            Image(
                painterResource(id = user.resourceId),
                contentDescription = user.profileDesc,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image-${user.id}"),
                        animatedVisibilityScope = animatedContentScope,
                        //boundsTransform = boundsTransform
                    )
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                user.name, fontSize = 18.sp,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        state = rememberSharedContentState(key = "title-${user.id}"),
                        animatedVisibilityScope = animatedContentScope,
                        //boundsTransform = boundsTransform
                    )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sit amet lobortis velit. " +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Curabitur sagittis, lectus posuere imperdiet facilisis, nibh massa " +
                        "molestie est, quis dapibus orci ligula non magna. Pellentesque rhoncus " +
                        "hendrerit massa quis ultricies. Curabitur congue ullamcorper leo, at maximus"
            )
        }


    }
}
