package com.alancamargo.snookerscore.data.local.match

import com.alancamargo.snookerscore.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface MatchLocalDataSource {

    fun addOrUpdateMatch(match: Match): Flow<Unit>

}
