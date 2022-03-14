package com.alancamargo.snookerscore.features.frame.data.repository

import com.alancamargo.snookerscore.features.frame.domain.model.Frame
import com.alancamargo.snookerscore.features.frame.domain.repository.FrameRepository
import com.alancamargo.snookerscore.features.frame.data.local.FrameLocalDataSource
import com.alancamargo.snookerscore.features.match.domain.model.Match
import kotlinx.coroutines.flow.Flow

class FrameRepositoryImpl(
    private val localDataSource: FrameLocalDataSource
) : FrameRepository {

    override fun addOrUpdateFrame(frame: Frame): Flow<Unit> {
        return localDataSource.addOrUpdateFrame(frame)
    }

    override fun getFrames(match: Match): Flow<List<Frame>> = localDataSource.getFrames(match)

}
