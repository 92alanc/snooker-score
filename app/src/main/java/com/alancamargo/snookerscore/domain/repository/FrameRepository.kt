package com.alancamargo.snookerscore.domain.repository

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.features.match.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface FrameRepository {

    fun addOrUpdateFrame(frame: Frame): Flow<Unit>

    fun getFrames(match: Match): Flow<List<Frame>>

}
