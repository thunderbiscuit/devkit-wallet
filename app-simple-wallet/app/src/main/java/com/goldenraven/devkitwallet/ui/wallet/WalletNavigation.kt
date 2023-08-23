/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.devkitwallet.ui.wallet

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.goldenraven.devkitwallet.ui.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WalletNavigation(innerPadding: PaddingValues) {
    val navController: NavHostController = rememberAnimatedNavController()
    val animationDuration = 400

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
        modifier = Modifier.padding(innerPadding)
    ) {

        composable(
            route = Screen.HomeScreen.route,
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
        ) { HomeScreen(navController) }

        composable(
            route = Screen.ReceiveScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            }
        ) { ReceiveScreen(navController) }

        composable(
            route = Screen.SendScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            }
        ) { SendScreen(navController) }

        composable(
            route = Screen.TransactionsScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            }
        ) { TransactionsScreen(navController) }
    }
}
