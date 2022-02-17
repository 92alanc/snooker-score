package com.alancamargo.snookerscore.data.local.frame

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface FrameLocalDataSource {

    fun addFrame(frame: Frame): Flow<Unit>

    fun deleteFrame(frame: Frame): Flow<Unit>

    fun getFrames(match: Match): Flow<List<Frame>>

}
