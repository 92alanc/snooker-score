package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.local.frame.FrameLocalDataSource
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import kotlinx.coroutines.flow.Flow

class FrameRepositoryImpl(private val localDataSource: FrameLocalDataSource) : FrameRepository {

    override fun addFrame(frame: Frame): Flow<Unit> = localDataSource.addFrame(frame)

    override fun deleteFrame(frame: Frame): Flow<Unit> = localDataSource.deleteFrame(frame)

    override fun getFrames(match: Match): Flow<List<Frame>> = localDataSource.getFrames(match)

}
