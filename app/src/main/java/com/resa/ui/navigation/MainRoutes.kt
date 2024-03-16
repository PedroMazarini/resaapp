package com.resa.ui.navigation
enum class MainRoutes {
    HOME,
    SETTINGS,
}

enum class HomeRoutes {
    HOME_START,
    HOME_LOCATION_SEARCH,
    HOME_JOURNEY_SELECTION,
    HOME_JOURNEY_DETAILS,
}

val MainRoutes.route: String
    get() = this.name

val HomeRoutes.route: String
    get() = this.name