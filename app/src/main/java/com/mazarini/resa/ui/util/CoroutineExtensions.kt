package com.mazarini.resa.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun CoroutineScope.launchIO(function: suspend () -> Unit) {
    launch(Dispatchers.IO) {
        function()
    }
}

inline fun <T> Flow<T>.distinctCollectLatest(
    crossinline action: suspend (T) -> Unit
): Job = CoroutineScope(Dispatchers.Default).launch {
    this@distinctCollectLatest
        .distinctUntilChanged()
        .collectLatest { action(it) }
}

inline fun <T> MutableStateFlow<T>.copyUpdate(crossinline block: T.() -> T) {
    update { it.block() }
}