package com.alancamargo.snookerscore.data.mapping.json

import com.alancamargo.snookerscore.domain.model.Player
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object PlayerSerialiser : KSerializer<Player> {

    private const val INDEX_ID = 0
    private const val INDEX_NAME = 1

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(serialName = "Player") {
        element<String>(elementName = "id")
        element<String>(elementName = "name")
    }

    override fun serialize(encoder: Encoder, value: Player) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, INDEX_ID, value.id)
            encodeStringElement(descriptor, INDEX_NAME, value.name)
        }
    }

    override fun deserialize(decoder: Decoder): Player = decoder.decodeStructure(descriptor) {
        var id: String? = null
        var name: String? = null

        loop@ while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                DECODE_DONE -> break@loop
                INDEX_ID -> id = decodeStringElement(descriptor, index)
                INDEX_NAME -> name = decodeStringElement(descriptor, index)
                else -> throw SerializationException("Unknown index. $index")
            }
        }

        if (id == null || name == null) {
            throw IllegalStateException("id = $id, name = $name")
        }

        Player(id, name)
    }
}
