package com.example.pokectcollection.ui.navigation

/**
 * Sealed class [Route] representing the navigation routes in the application.
 *
 * The sealed class 'Route' is used to define the different routes available in the application.
 * Each object within the sealed class represents a specific screen in the application.
 * The 'route' property holds the route string for each screen.
 */
sealed class Route(val route: String) {
    /**
     * Object representing the Splash Screen route.
     */
    data object SplashScreen : Route("SplashScreen")

    /**
     * Object representing the Login Screen route.
     */
    data object Login : Route("Login")

    /**
     * Object representing the Register Screen route.
     */
    data object Register : Route("Register")

    /**
     * Object representing the Set List Screen route.
     */
    data object SetList : Route("SetList")

    /**
     * Object representing the Card Detail Screen route.
     */
    data object CardDetail : Route("CardDetail")

    /**
     * Object representing the Card List Screen route.
     */
    data object CardList : Route("CardList")

    /**
     * Object representing the User Detail Screen route.
     */
    data object UserDetail : Route("UserDetail")

    /**
     * Object representing the Create Card List Screen route.
     */
    data object CreateCardList : Route("CreateCardList")

    /**
     * Object representing the Check List Card Screen route.
     */
    data object CheckListCard : Route("CheckListCard")

    /**
     * Object representing the Achievements Screen route.
     */
    data object Achievements : Route("Achievements")
}
