package com.mazarini.resa.ui.util.mapsconfiguration

import android.content.Context
import android.content.Intent
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.ui.util.showMessage
import androidx.core.net.toUri

fun openWalkDirectionsOnMaps(
    from: Coordinate,
    to: Coordinate,
    context: Context,
) {
    val uriString = "https://www.google.com/maps/dir/?api=1&origin=${from.lat},${from.lon}&destination=${to.lat},${to.lon}&travelmode=walking"
    val intent = Intent(Intent.ACTION_VIEW, uriString.toUri())
    intent.setPackage("com.google.android.apps.maps")

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        context.showMessage(context.getString(R.string.google_maps_app_not_found))
    }
}