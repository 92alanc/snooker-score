package com.alancamargo.snookerscore.data.mapping.json

import com.alancamargo.snookerscore.domain.model.Frame
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class FrameListSerialiser : KSerializer<List<Frame>> {

    override fun deserialize(decoder: Decoder): List<Frame> {
        TODO("Not yet implemented")
    }

    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun serialize(encoder: Encoder, value: List<Frame>) {
        TODO("Not yet implemented")
    }

}
