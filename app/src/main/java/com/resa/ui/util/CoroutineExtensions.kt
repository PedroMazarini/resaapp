package com.resa.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CoroutineScope.launchIO(function: suspend () -> Unit) {
    launch(Dispatchers.IO) {
        function()
    }
}