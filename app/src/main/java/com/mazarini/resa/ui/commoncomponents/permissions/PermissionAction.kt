package com.mazarini.resa.ui.commoncomponents.permissions

sealed class PermissionAction {

    object OnPermissionGranted : PermissionAction()

    object OnPermissionDenied : PermissionAction()

    object OnPermissionRequested : PermissionAction()
}
