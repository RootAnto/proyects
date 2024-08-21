package com.example.pokectcollection.ui.navigation


import CustomCardList
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokectcollection.ui.carddetail.view.CardDetailScreen
import com.example.pokectcollection.ui.cardlist.view.CardListScreen
import com.example.pokectcollection.ui.createlistcard.view.CreateListCard


import com.example.pokectcollection.ui.login.view.LoginScreen
import com.example.pokectcollection.ui.register.view.RegisterScreen
import com.example.pokectcollection.ui.setlist.view.SetListScreen
import com.example.pokectcollection.ui.splash.view.SplashScreen
import com.example.pokectcollection.ui.userdetail.view.UserDetailScreen

/**
 * Composable function [PokectCollectionNavigation] for setting up navigation in the PokectCollection app.
 */
@Composable
fun PokectCollectionNavigation() {
    val navController: NavHostController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = Route.SplashScreen.route
    ) {
        // Splash Screen navigation
        composable(route = Route.SplashScreen.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Route.Login.route)
                    navController.backQueue.removeFirst()
                },
            )
        }

        // Login screen navigation
        composable(route = Route.Login.route) {
            LoginScreen(
                onLoginSuccessful = { navController.navigate(Route.SetList.route) }
            ) {
                navController.navigate(Route.Register.route)
            }
        }

        // Register screen navigation
        composable(route = Route.Register.route) {
            RegisterScreen(onClickBack = { navController.navigateUp() }) {
                navController.navigateUp()
            }
        }

        // Set list screen navigation
        composable(route = Route.SetList.route) {
            SetListScreen(
                onProfileClick = { navController.navigate(Route.UserDetail.route) },
                onSetListClick = { },
                onCreateListClick = { navController.navigate(Route.CreateCardList.route) },
                onCheckListCardClick = { navController.navigate(Route.CheckListCard.route) },
                onClickBack = { navController.navigateUp() },
                onLogOut = {
                    navController.navigate(Route.Login.route)
                    while (navController.backQueue.size > 1) {
                        navController.backQueue.removeFirst()
                    }
                },
                onSetClicked = { setId ->
                    navController.navigate(Route.CardList.route.plus("/$setId"))
                }
            )
        }

        // Card list screen navigation
        composable(
            route = Route.CardList.route.plus("/{setId}"),
            arguments = listOf(navArgument("setId") { type = NavType.StringType })
        ) {
            it.arguments?.getString("setId") ?: ""

            CardListScreen(
                onProfileClick = { navController.navigate(Route.UserDetail.route) },
                onSetListClick = { navController.navigate(Route.SetList.route) },
                onCreateListClick = { navController.navigate(Route.CreateCardList.route) },
                onCheckListCardClick = { navController.navigate(Route.CheckListCard.route) },
                onClickBack = { navController.navigateUp() },
                onLogOut = {
                    navController.navigate(Route.Login.route)
                    while (navController.backQueue.size > 1) {
                        navController.backQueue.removeFirst()
                    }
                },
            ) { cardId ->
                navController.navigate(Route.CardDetail.route.plus("/$cardId"))
            }
        }

        // Card detail screen navigation
        composable(
            route = Route.CardDetail.route.plus("/{cardId}"),
            arguments = listOf(navArgument("cardId") { type = NavType.StringType })
        ) {
            it.arguments?.getString("cardId") ?: ""

            CardDetailScreen(
                onClickBack = { navController.navigateUp() }
            )
        }

        // User detail screen navigation
        composable(route = Route.UserDetail.route) {
            UserDetailScreen(
                onProfileClick = { },
                onSetListClick = { navController.navigate(Route.SetList.route) },
                onCreateListClick = { navController.navigate(Route.CreateCardList.route) },
                onCheckListCardClick = { navController.navigate(Route.CheckListCard.route) },
                onClickBack = { navController.navigateUp() },
                onLogOut = {
                    navController.navigate(Route.Login.route)
                    while (navController.backQueue.size > 1) {
                        navController.backQueue.removeFirst()
                    }
                }

            )
        }

        // Create card list screen navigation
        composable(route = Route.CreateCardList.route) {
            CreateListCard(
                onProfileClick = { navController.navigate(Route.UserDetail.route) },
                onSetListClick = { navController.navigate(Route.SetList.route) },
                onCreateListClick = { },
                onCheckListCardClick = { navController.navigate(Route.CheckListCard.route) },
                onClickBack = { navController.navigateUp() },
                onLogOut = {
                    navController.navigate(Route.Login.route)
                    while (navController.backQueue.size > 1) {
                        navController.backQueue.removeFirst()
                    }
                }
            )
        }

        // Check list card screen navigation
        composable(route = Route.CheckListCard.route) {
            CustomCardList(
                onProfileClick = { navController.navigate(Route.UserDetail.route) },
                onSetListClick = { navController.navigate(Route.SetList.route) },
                onCreateListClick = { navController.navigate(Route.CreateCardList.route) },
                onCheckListCardClick = {  },
                onSelectedCardClicked = { cardId ->
                    navController.navigate(Route.CardDetail.route.plus("/$cardId")) },
                onLogOut = {
                    navController.navigate(Route.Login.route)
                    while (navController.backQueue.size > 1) {
                        navController.backQueue.removeFirst()
                    }
                },
                onClickBack = { navController.navigateUp() },

            )
        }
    }
}
