package com.mazarini.resa.ui.util

data class MappingException(
    val obj: Any? = null,
    val m: String = "Mapping failed for object: $obj",
) : Exception(m)
