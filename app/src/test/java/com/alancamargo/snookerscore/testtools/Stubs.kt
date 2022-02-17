package com.alancamargo.snookerscore.testtools

import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats

const val ERROR_MESSAGE = "Something wrong happened. Figure it out"

fun getFrame() = Frame(match = getMatch())

fun getPlayer() = Player(name = "Mark Selby")

fun getMatch() = Match(player1 = getPlayer(), player2 = getPlayer())

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
