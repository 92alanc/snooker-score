package com.alancamargo.snookerscore.data.local.frame

import com.alancamargo.snookerscore.data.db.FrameDao
import com.alancamargo.snookerscore.data.db.MatchDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
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
