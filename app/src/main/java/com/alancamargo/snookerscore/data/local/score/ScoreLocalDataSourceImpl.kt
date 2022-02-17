package com.alancamargo.snookerscore.data.local.score

import com.alancamargo.snookerscore.data.db.ScoreDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Score
import kotlinx.coroutines.flow.flow

class ScoreLocalDataSourceImpl(private val scoreDao: ScoreDao) : ScoreLocalDataSource {

    override fun addOrUpdateScore(score: Score) = flow {
        val task = scoreDao.addOrUpdateScore(score.toData())
        emit(task)
    }

    override fun getScore(frame: Frame) = flow {
        val dbScore = scoreDao.getScore(frame.id)
        emit(dbScore.toDomain(frame))
    }

}
