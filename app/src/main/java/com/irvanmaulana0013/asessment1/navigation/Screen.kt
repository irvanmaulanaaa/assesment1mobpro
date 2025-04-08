package com.irvanmaulana0013.asessment1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
}