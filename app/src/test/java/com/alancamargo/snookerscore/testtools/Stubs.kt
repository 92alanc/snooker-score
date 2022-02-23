package com.alancamargo.snookerscore.testtools

import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Gender
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiFrame
import java.util.UUID

const val ERROR_MESSAGE = "Something wrong happened. Figure it out"

private const val MATCH_DATE_TIME = 12345L

fun getPlayer() = Player(name = "Mark Selby", gender = Gender.MALE)

fun getMatch(dateTime: Long = MATCH_DATE_TIME) = Match(
    dateTime = dateTime,
    player1 = getPlayer(),
    player2 = getPlayer(),
    numberOfFrames = 3
)

fun getMatchList() = listOf(
    getMatch(),
    getMatch(),
    getMatch()
)

fun getPlayerList() = listOf(
    Player(name = "Kyren Wilson", gender = Gender.MALE),
    Player(name = "Neil Robertson", gender = Gender.MALE)
)

fun getDbPlayerList() = listOf(
    DbPlayer(name = "Judd Trump", genderId = 0),
    DbPlayer(name = "Ronnie o\' Sullivan", genderId = 0)
)

fun getPlayerStats() = PlayerStats(
    id = "123",
    player = getPlayer(),
    matchesWon = 130,
    highestScore = 147,
    highestBreak = 147
)

fun getFrame(matchDateTime: Long = MATCH_DATE_TIME) = Frame(
    id = UUID.randomUUID().toString(),
    positionInMatch = 1,
    match = getMatch(matchDateTime),
    player1Score = 80,
    player2Score = 48,
    player1HighestBreak = 17,
    player2HighestBreak = 20
)

fun getFrameList(matchDateTime: Long = MATCH_DATE_TIME) = listOf(
    getFrame(matchDateTime),
    getFrame(matchDateTime),
    getFrame(matchDateTime)
)

fun getUiMatch() = getMatch().toUi()

fun getUiFrame(positionInMatch: Int) = UiFrame(
    positionInMatch = positionInMatch,
    match = getUiMatch(),
    player1Score = 0,
    player2Score = 0,
    player1HighestBreak = 0,
    player2HighestBreak = 0
)

fun getUiFrameList() = listOf(
    getUiFrame(positionInMatch = 1),
    getUiFrame(positionInMatch = 2),
    getUiFrame(positionInMatch = 3)
)
