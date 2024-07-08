package com.mazarini.resa.ui.commoncomponents.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val TAG = "Permissions Commons"

class Common {

    companion object {
        fun checkIfPermissionGranted(context: Context, permission: String): Boolean {
            return (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED)
        }
        fun shouldShowPermissionRationale(context: Context, permission: String): Boolean {
            val activity = context as Activity?

            return ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                permission
            )
        }
    }
}
