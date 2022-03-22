package com.alancamargo.snookerscore.features.frame.data.local

import com.alancamargo.snookerscore.features.frame.domain.model.Frame
import com.alancamargo.snookerscore.features.match.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface FrameLocalDataSource {

    fun addOrUpdateFrame(frame: Frame): Flow<Unit>

    fun getFrames(match: Match): Flow<List<Frame>>

}
