package com.alancamargo.snookerscore.domain.repository

import com.alancamargo.snookerscore.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface MatchRepository {

    fun addOrUpdateMatch(match: Match): Flow<Unit>

    fun deleteMatch(match: Match): Flow<Unit>

}
