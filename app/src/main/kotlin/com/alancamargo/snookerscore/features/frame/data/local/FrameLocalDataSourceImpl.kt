package com.alancamargo.snookerscore.features.frame.data.local

import com.alancamargo.snookerscore.features.frame.data.mapping.toData
import com.alancamargo.snookerscore.features.frame.data.mapping.toDomain
import com.alancamargo.snookerscore.features.frame.domain.model.Frame
import com.alancamargo.snookerscore.features.frame.data.db.FrameDao
import com.alancamargo.snookerscore.features.match.data.db.MatchDao
import com.alancamargo.snookerscore.features.match.data.mapping.toData
import com.alancamargo.snookerscore.features.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FrameLocalDataSourceImpl(
    private val frameDao: FrameDao,
    private val matchDao: MatchDao
) : FrameLocalDataSource {

    override fun addOrUpdateFrame(frame: Frame) = flow {
        val frameExists = frameDao.getFrame(frame.id) != null

        val task = if (frameExists) {
            val matchEnded = frame.isFinished && frame.positionInMatch == frame.match.numberOfFrames

            if (matchEnded) {
                val match = frame.match.copy(isFinished = true)
                matchDao.updateMatch(match.toData())
            }

            frameDao.updateFrame(frame.toData())
        } else {
            frameDao.addFrame(frame.toData())
        }

        emit(task)
    }

    override fun getFrames(match: Match): Flow<List<Frame>> = flow {
        val dbFrames = frameDao.getFrames(match.dateTime)
        val frames = dbFrames?.map { it.toDomain(match) } ?: emptyList()
        emit(frames)
    }

}
