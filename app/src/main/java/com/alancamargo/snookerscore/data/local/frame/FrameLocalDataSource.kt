package com.alancamargo.snookerscore.data.local.frame

import com.alancamargo.snookerscore.domain.model.Frame
import kotlinx.coroutines.flow.Flow

interface FrameLocalDataSource {

    fun addFrame(frame: Frame): Flow<Unit>

    fun deleteFrame(frame: Frame): Flow<Unit>

}
