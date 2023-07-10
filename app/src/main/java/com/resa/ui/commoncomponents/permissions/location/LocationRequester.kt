package com.resa.ui.commoncomponents.permissions.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.resa.ui.commoncomponents.permissions.PermissionAction
import com.resa.ui.commoncomponents.permissions.PermissionUI

@Composable
fun RequestLocation(
    locationAction: (LocationAction) -> Unit
) {
    val isGPSEnabled = isGPSEnabled()
    val context = LocalContext.current
    PermissionUI(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) { permissionAction ->
        when (permissionAction) {
            is PermissionAction.OnPermissionGranted -> {
                if (isGPSEnabled) {
                    getLocation(context, locationAction)
                } else {
                    locationAction(LocationAction.OnGPSNotAvailable)
                }
            }
            is PermissionAction.OnPermissionDenied -> {
                locationAction(LocationAction.OnPermissionDenied)
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun getLocation(
    context: Context,
    locationAction: (LocationAction) -> Unit
) {
    val locationProvider = LocationServices.getFusedLocationProviderClient(context)
    locationProvider
        .requestLocationUpdates(
            getLocationRequest(),
            object: LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationProvider.removeLocationUpdates(this)
                    if (locationResult.locations.isNotEmpty()) {
                        val lat = locationResult.locations.last().latitude
                        val lon = locationResult.locations.last().longitude
                        locationAction(LocationAction.OnSuccess(lat, lon))
                    } else {
                        locationAction(LocationAction.OnGPSError)
                    }
                }
            },
            Looper.getMainLooper())
}

fun getLocationRequest(): LocationRequest =
    LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, 5000).apply {
        setGranularity(Granularity.GRANULARITY_FINE)
        setWaitForAccurateLocation(true)
    }.build()

@Composable
fun isGPSEnabled(): Boolean {
    val locationManager = LocalContext.current.getSystemService(LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(GPS_PROVIDER)
}
