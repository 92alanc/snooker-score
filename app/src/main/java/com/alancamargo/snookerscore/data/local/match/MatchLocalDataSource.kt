package com.alancamargo.snookerscore.data.local.match

import com.alancamargo.snookerscore.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface MatchLocalDataSource {

    fun addMatch(match: Match): Flow<Unit>

    fun deleteMatch(match: Match): Flow<Unit>

    fun getMatches(): Flow<List<Match>>

}
