package com.alancamargo.snookerscore.domain.usecase.match

import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow

class AddOrUpdateMatchUseCase(private val repository: MatchRepository) {

    operator fun invoke(match: Match): Flow<Unit> = repository.addOrUpdateMatch(match)

}
