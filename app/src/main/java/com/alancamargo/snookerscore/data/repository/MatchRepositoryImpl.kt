package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.local.match.MatchLocalDataSource
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow

class MatchRepositoryImpl(private val localDataSource: MatchLocalDataSource) : MatchRepository {

    override fun addMatch(match: Match): Flow<Unit> = localDataSource.addMatch(match)

    override fun deleteMatch(match: Match): Flow<Unit> = localDataSource.deleteMatch(match)

    override fun getMatches(): Flow<List<Match>> = localDataSource.getMatches()

}
