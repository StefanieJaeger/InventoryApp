package com.ost.mge.inventoryapp.serializer

import android.net.Uri
import androidx.core.net.toUri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UriAsStringSerializer : KSerializer<Uri> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uri) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Uri = decoder.decodeString().toUri()
}