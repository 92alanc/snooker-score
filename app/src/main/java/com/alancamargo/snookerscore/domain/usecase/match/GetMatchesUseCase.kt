package com.alancamargo.snookerscore.domain.usecase.match

import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow

class GetMatchesUseCase(private val repository: MatchRepository) {

    operator fun invoke(): Flow<List<Match>> = repository.getMatches()

}
