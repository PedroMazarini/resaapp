package com.mazarini.resa.domain.serializers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        val colorString = "#%08X".format(value.toArgb())
        encoder.encodeString(colorString)
    }

    override fun deserialize(decoder: Decoder): Color {
        val colorString = decoder.decodeString()
        return Color(android.graphics.Color.parseColor(colorString))
    }
}
