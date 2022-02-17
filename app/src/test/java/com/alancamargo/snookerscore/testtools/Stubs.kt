package com.alancamargo.snookerscore.testtools

import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.model.Score
import java.util.UUID

const val ERROR_MESSAGE = "Something wrong happened. Figure it out"

private const val MATCH_DATE_TIME = 12345L

fun getFrame(
    matchDateTime: Long = MATCH_DATE_TIME,
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString()
) = Frame(match = getMatch(matchDateTime, player1Id, player2Id))

fun getPlayer(id: String = UUID.randomUUID().toString()) = Player(id = id, name = "Mark Selby")

fun getMatch(
    dateTime: Long = MATCH_DATE_TIME,
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString()
) = Match(
    dateTime = dateTime,
    player1 = getPlayer(player1Id),
    player2 = getPlayer(player2Id),
    numberOfFrames = 3
)

fun getMatchList(
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString()
) = listOf(
    getMatch(player1Id = player1Id, player2Id = player2Id),
    getMatch(player1Id = player1Id, player2Id = player2Id),
    getMatch(player1Id = player1Id, player2Id = player2Id)
)

fun getPlayerList() = listOf(
    Player(name = "Kyren Wilson"),
    Player(name = "Neil Robertson")
)

fun getDbPlayerList() = listOf(
    DbPlayer(id = "12345", name = "Judd Trump"),
    DbPlayer(id = "54321", name = "Ronnie o\' Sullivan")
)

fun getPlayerStats() = PlayerStats(
    id = "123",
    player = getPlayer(),
    matchesWon = 130,
    highestScore = 147,
    highestBreak = 147
)

fun getScore() = Score(
    frame = getFrame(),
    player1Score = 80,
    player2Score = 48,
    player1HighestBreak = 17,
    player2HighestBreak = 20
)

fun getFrameList(
    matchDateTime: Long = MATCH_DATE_TIME,
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString()
) = listOf(
    getFrame(matchDateTime, player1Id, player2Id),
    getFrame(matchDateTime, player1Id, player2Id),
    getFrame(matchDateTime, player1Id, player2Id)
)
