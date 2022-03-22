package com.alancamargo.snookerscore.features.match.data.local

import com.alancamargo.snookerscore.features.match.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface MatchLocalDataSource {

    fun addMatch(match: Match): Flow<Unit>

    fun deleteMatch(match: Match): Flow<Unit>

    fun getMatches(): Flow<List<Match>>

}
