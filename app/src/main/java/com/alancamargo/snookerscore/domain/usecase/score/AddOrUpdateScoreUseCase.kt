package com.alancamargo.snookerscore.domain.usecase.score

import com.alancamargo.snookerscore.domain.model.Score
import com.alancamargo.snookerscore.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow

class AddOrUpdateScoreUseCase(private val repository: ScoreRepository) {

    operator fun invoke(score: Score): Flow<Unit> = repository.addOrUpdateScore(score)

}
