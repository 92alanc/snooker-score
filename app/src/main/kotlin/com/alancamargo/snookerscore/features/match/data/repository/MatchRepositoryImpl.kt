package com.alancamargo.snookerscore.features.match.data.repository

import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.match.domain.repository.MatchRepository
import com.alancamargo.snookerscore.features.match.data.local.MatchLocalDataSource
import kotlinx.coroutines.flow.Flow

class MatchRepositoryImpl(private val localDataSource: MatchLocalDataSource) : MatchRepository {

    override fun addMatch(match: Match): Flow<Unit> = localDataSource.addMatch(match)

    override fun deleteMatch(match: Match): Flow<Unit> = localDataSource.deleteMatch(match)

    override fun getMatches(): Flow<List<Match>> = localDataSource.getMatches()

}
