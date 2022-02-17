package com.alancamargo.snookerscore.domain.usecase.score

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Score
import com.alancamargo.snookerscore.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow

class GetScoreUseCase(private val repository: ScoreRepository) {

    operator fun invoke(frame: Frame): Flow<Score> = repository.getScore(frame)

}
