package com.mazarini.resa.ui.commoncomponents.permissions

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

private const val TAG = "PermissionUI"

@Composable
fun PermissionUI(
    context: Context,
    permission: String,
    permissionAction: (PermissionAction) -> Unit,
) {
    val permissionGranted =
        Common.checkIfPermissionGranted(
            context,
            permission
        )

    if (permissionGranted) {
        Log.d(TAG, "Permission already granted, exiting..")
        permissionAction(PermissionAction.OnPermissionGranted)
        return
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Permission provided by user")
            // Permission Accepted
            permissionAction(PermissionAction.OnPermissionGranted)
        } else {
            Log.d(TAG, "Permission denied by user")
            // Permission Denied
            permissionAction(PermissionAction.OnPermissionDenied)
        }
    }

    val showPermissionRationale = Common.shouldShowPermissionRationale(
        context,
        permission
    )

    if (showPermissionRationale) {
        permissionAction(PermissionAction.OnPermissionDenied)
    } else {
        //Request permissions again
        Log.d(TAG, "Requesting permission for $permission")
        SideEffect {
            launcher.launch(permission)
            permissionAction(PermissionAction.OnPermissionRequested)
        }
    }
}