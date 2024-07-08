package com.mazarini.resa.ui.commoncomponents.permissions.location

sealed class LocationAction {

    object OnPermissionGranted : LocationAction()

    object OnPermissionDenied : LocationAction()

    object OnPermissionRequested : LocationAction()

    object OnGPSNotAvailable : LocationAction()

    object OnGPSError : LocationAction()

    data class OnSuccess(val lat: Double, val lon: Double) : LocationAction()
}
