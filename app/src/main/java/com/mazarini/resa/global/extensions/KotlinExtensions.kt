package com.mazarini.resa.global.extensions

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.ui.theme.colors.FrenchBlue
import java.lang.Exception

inline fun <T: Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}

fun <T> List<T>.ifEmpty(default: () -> List<T>): List<T> {
    return if (this.isEmpty()) default() else this
}

infix fun <T> List<T>.isBounds(index: Int): Boolean {
    return index == 0 || index == lastIndex
}

infix fun Int?.or(value: Int): Int {
    return this ?: value
}

fun MutableState<Boolean>.toggle() {
    this.value = !this.value
}

infix fun MutableState<Boolean>.set(setValue: Boolean) {
    this.value = setValue
}


fun String.capitalizeFirst() = replaceFirstChar(Char::titlecase)

fun <T> List<T>.toggle(element: T): List<T> {
    return if (this.contains(element)) {
        this - element
    } else {
        this + element
    }
}

inline fun <T: Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}

inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

infix fun <T> T?.orThrow(exception: Exception): T {
    if (this == null) throw exception
    return this
}


data class NTuple2<T1, T2>(val t1: T1, val t2: T2)
data class NTuple3<T1, T2, T3>(val t1: T1, val t2: T2, val t3: T3)
data class NTuple4<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4)

infix fun <T1, T2> T1.then(t2: T2): NTuple2<T1, T2>
{
    return NTuple2(this, t2)
}

infix fun <T1, T2, T3> NTuple2<T1, T2>.then(t3: T3): NTuple3<T1, T2, T3>
{
    return NTuple3(this.t1, this.t2, t3)
}

infix fun <T1, T2, T3, T4> NTuple3<T1, T2, T3>.then(t4: T4): NTuple4<T1, T2, T3, T4>
{
    return NTuple4(this.t1, this.t2, this.t3, t4)
}

fun (() -> LegColors).orDefaultColors() : LegColors =
    try {
        this()
    } catch (e: Exception) {
        e.printStackTrace()
        LegColors(
            foreground = Color.White,
            background = FrenchBlue,
            border = FrenchBlue,
        )
    }

fun <T> tryCatch(block: () -> T): T? =
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

