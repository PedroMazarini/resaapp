package com.mazarini.resa.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

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
    LIGHT(json = "[]"),
    DARK(json = "[\n" +
            "  {\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#1d2c4d\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#8ec3b9\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#1a3646\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative.country\",\n" +
            "    \"elementType\": \"geometry.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#4b6878\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative.land_parcel\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#64779e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative.province\",\n" +
            "    \"elementType\": \"geometry.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#4b6878\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"landscape.man_made\",\n" +
            "    \"elementType\": \"geometry.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#334e87\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"landscape.natural\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#023e58\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#283d6a\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#6f9ba5\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#1d2c4d\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"geometry.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#023e58\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#3C7680\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#304a7d\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#98a5be\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#1d2c4d\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#2c6675\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"geometry.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#255763\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#b0d5ce\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#023e58\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#98a5be\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit\",\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#1d2c4d\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit.line\",\n" +
            "    \"elementType\": \"geometry.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#283d6a\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit.station\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#3a4762\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#0e1626\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#4e6d70\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]")
}