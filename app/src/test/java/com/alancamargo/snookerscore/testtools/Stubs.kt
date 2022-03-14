package com.alancamargo.snookerscore.testtools

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.match.domain.model.MatchSummary
import com.alancamargo.snookerscore.features.match.ui.mapping.toUi
import com.alancamargo.snookerscore.features.player.data.model.DbPlayer
import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.playerstats.domain.model.PlayerStats
import com.alancamargo.snookerscore.ui.model.UiFrame
import java.util.UUID

const val ERROR_MESSAGE = "Something wrong happened. Figure it out"

private const val MATCH_DATE_TIME = 12345L

fun getPlayer(id: String = "12345") = Player(id = id, name = "Mark Selby", gender = Gender.MALE)

fun getMatch(
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString(),
    dateTime: Long = MATCH_DATE_TIME
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
    getMatch(player1Id, player2Id),
    getMatch(player1Id, player2Id),
    getMatch(player1Id, player2Id)
)

fun getPlayerList() = listOf(
    Player(name = "Kyren Wilson", gender = Gender.MALE),
    Player(name = "Neil Robertson", gender = Gender.MALE)
)

fun getDbPlayerList() = listOf(
    DbPlayer(id = "12345", name = "Judd Trump", genderId = 0),
    DbPlayer(id = "12345", name = "Ronnie o\' Sullivan", genderId = 0)
)

fun getPlayerStats() = PlayerStats(
    id = "123",
    player = getPlayer(),
    matchesWon = 130,
    highestScore = 147,
    highestBreak = 147
)

fun getFrame(
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString(),
    matchDateTime: Long = MATCH_DATE_TIME
) = Frame(
    id = UUID.randomUUID().toString(),
    positionInMatch = 1,
    match = getMatch(player1Id, player2Id, matchDateTime),
    player1Score = 80,
    player2Score = 48,
    player1HighestBreak = 17,
    player2HighestBreak = 20
)

fun getFrameList(
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString(),
    matchDateTime: Long = MATCH_DATE_TIME
) = listOf(
    getFrame(player1Id, player2Id, matchDateTime),
    getFrame(player1Id, player2Id, matchDateTime),
    getFrame(player1Id, player2Id, matchDateTime)
)

fun getUiMatch(
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString()
) = getMatch(player1Id, player2Id).toUi()

fun getUiFrame(player1Id: String, player2Id: String, positionInMatch: Int) = UiFrame(
    positionInMatch = positionInMatch,
    match = getUiMatch(player1Id, player2Id),
    player1Score = 0,
    player2Score = 0,
    player1HighestBreak = 0,
    player2HighestBreak = 0,
    isFinished = false
)

fun getUiFrameList(
    player1Id: String = UUID.randomUUID().toString(),
    player2Id: String = UUID.randomUUID().toString()
) = listOf(
    getUiFrame(player1Id, player2Id, positionInMatch = 1),
    getUiFrame(player1Id, player2Id, positionInMatch = 2),
    getUiFrame(player1Id, player2Id, positionInMatch = 3)
)

fun getMatchSummary() = MatchSummary(
    match = getMatch(player1Id = "12345", player2Id = "54321"),
    winner = getPlayer(),
    player1Score = 2,
    player2Score = 1,
    player1HighestBreak = 147,
    player2HighestBreak = 100
)
