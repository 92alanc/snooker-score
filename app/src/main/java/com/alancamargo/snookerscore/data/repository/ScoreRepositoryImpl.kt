package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.local.score.ScoreLocalDataSource
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Score
import com.alancamargo.snookerscore.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow

class ScoreRepositoryImpl(
    private val localDataSource: ScoreLocalDataSource
) : ScoreRepository {

    override fun addOrUpdateScore(score: Score): Flow<Unit> {
        return localDataSource.addOrUpdateScore(score)
    }

    override fun getScore(frame: Frame): Flow<Score> = localDataSource.getScore(frame)

}
