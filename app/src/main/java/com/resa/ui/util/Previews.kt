package com.resa.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "DarkBasic", showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "LightBasic", showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class Previews()
