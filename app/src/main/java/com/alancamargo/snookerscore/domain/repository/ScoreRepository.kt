package com.alancamargo.snookerscore.domain.repository

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Score
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {

    fun addOrUpdateScore(score: Score): Flow<Unit>

    fun getScore(frame: Frame): Flow<Score>

}
