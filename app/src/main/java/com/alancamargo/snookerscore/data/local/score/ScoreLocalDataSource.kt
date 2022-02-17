package com.alancamargo.snookerscore.data.local.score

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Score
import kotlinx.coroutines.flow.Flow

interface ScoreLocalDataSource {

    fun addOrUpdateScore(score: Score): Flow<Unit>

    fun getScore(frame: Frame): Flow<Score>

}
