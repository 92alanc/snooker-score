package com.alancamargo.snookerscore.data.mapping.json

import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.Score
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

object ScoreSerialiser : KSerializer<Score> {

    private const val INDEX_ID = 0
    private const val INDEX_PLAYER = 1
    private const val INDEX_SCORE = 2
    private const val INDEX_HIGHEST_BREAK = 3

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(serialName = "Score") {
        element<String>(elementName = "id")
        element<Player>(elementName = "player")
        element<Int>(elementName = "score")
        element<Int>(elementName = "highestBreak")
    }

    override fun serialize(encoder: Encoder, value: Score) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, INDEX_ID, value.id)
            encodeSerializableElement(descriptor, INDEX_PLAYER, PlayerSerialiser, value.player)
            encodeIntElement(descriptor, INDEX_SCORE, value.score)
            encodeIntElement(descriptor, INDEX_HIGHEST_BREAK, value.highestBreak)
        }
    }

    override fun deserialize(decoder: Decoder): Score = decoder.decodeStructure(descriptor) {
        var id: String? = null
        var player: Player? = null
        var score: Int? = null
        var highestBreak: Int? = null

        loop@ while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                DECODE_DONE -> break@loop
                INDEX_ID -> id = decodeStringElement(descriptor, index)
                INDEX_PLAYER -> player = decodeSerializableElement(descriptor, index, PlayerSerialiser)
                INDEX_SCORE -> score = decodeIntElement(descriptor, index)
                INDEX_HIGHEST_BREAK -> highestBreak = decodeIntElement(descriptor, index)
                else -> throw SerializationException("Unknown index. $index")
            }
        }

        if (id == null || player == null || score == null || highestBreak == null) {
            throw IllegalStateException("id=$id, player=$player, score=$score, highestBreak=$highestBreak")
        }

        Score(id, player, score, highestBreak)
    }

}
