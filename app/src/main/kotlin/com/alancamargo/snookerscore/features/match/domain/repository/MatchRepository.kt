package com.alancamargo.snookerscore.features.match.domain.repository

import com.alancamargo.snookerscore.features.match.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface MatchRepository {

    fun addMatch(match: Match): Flow<Unit>

    fun deleteMatch(match: Match): Flow<Unit>

    fun getMatches(): Flow<List<Match>>

}
