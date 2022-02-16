package com.alancamargo.snookerscore.domain.repository

import com.alancamargo.snookerscore.domain.model.Frame
import kotlinx.coroutines.flow.Flow

interface FrameRepository {

    fun addFrame(frame: Frame): Flow<Unit>

    fun deleteFrame(frame: Frame): Flow<Unit>

}
