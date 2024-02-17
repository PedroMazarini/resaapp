package com.resa.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.resa.ui.theme.colors.ResaColors

val LocalResaMapsStyling = staticCompositionLocalOf<GoogleMapsStyling> {
    error("No GoogleMapsStyling provided")
}

@Composable
fun ProvideMapsStyling(
    googleMapsStyling: GoogleMapsStyling,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalResaMapsStyling provides googleMapsStyling, content = content)
}
enum class GoogleMapsStyling(val json: String) {
    LIGHT(json = "[\n" +
            "  {\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#f5f5f5\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.icon\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#616161\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#f5f5f5\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative.land_parcel\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#bdbdbd\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#eeeeee\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#757575\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#e5e5e5\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#9e9e9e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.arterial\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#757575\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#dadada\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#616161\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.local\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#9e9e9e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit.line\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#e5e5e5\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit.station\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#eeeeee\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#c9c9c9\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#9e9e9e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]"),
    DARK(json = "[\n" +
            "  {\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#242f3e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#746855\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#242f3e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative.locality\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#d59563\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#d59563\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#263c3f\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#6b9a76\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#38414e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"geometry.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#212a37\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#9ca5b3\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#746855\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"geometry.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#1f2835\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#f3d19c\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#2f3948\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit.station\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#d59563\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#17263c\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#515c6d\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#17263c\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]")
}