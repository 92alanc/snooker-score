package com.alancamargo.snookerscore.features.match.domain.usecase

import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.match.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow

class AddMatchUseCase(private val repository: MatchRepository) {

    operator fun invoke(match: Match): Flow<Unit> = repository.addMatch(match)

}
