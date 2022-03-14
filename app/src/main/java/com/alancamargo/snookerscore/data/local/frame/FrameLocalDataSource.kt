package com.alancamargo.snookerscore.data.local.frame

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.features.match.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface FrameLocalDataSource {

    fun addOrUpdateFrame(frame: Frame): Flow<Unit>

    fun getFrames(match: Match): Flow<List<Frame>>

}
