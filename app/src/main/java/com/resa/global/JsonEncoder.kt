package com.resa.global

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

object JsonEncoder {
    inline fun <reified T> encode(clazz: T): String? {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(T::class.java)
        return jsonAdapter.toJson(clazz)
    }

    inline fun <reified T> decode(json: String): T {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<T> = moshi.adapter(T::class.java)
        val result = jsonAdapter.fromJson(json)
        return result!!
    }
}
