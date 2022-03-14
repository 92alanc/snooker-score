package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.local.frame.FrameLocalDataSource
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import kotlinx.coroutines.flow.Flow

class FrameRepositoryImpl(
    private val localDataSource: FrameLocalDataSource
) : FrameRepository {

    override fun addOrUpdateFrame(frame: Frame): Flow<Unit> {
        return localDataSource.addOrUpdateFrame(frame)
    }

    override fun getFrames(match: Match): Flow<List<Frame>> = localDataSource.getFrames(match)

}
