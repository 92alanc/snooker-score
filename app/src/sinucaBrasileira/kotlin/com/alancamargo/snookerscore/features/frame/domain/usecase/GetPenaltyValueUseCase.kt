package com.alancamargo.snookerscore.features.frame.domain.usecase

import com.alancamargo.snookerscore.features.frame.domain.model.Foul

private const val PENALTY_VALUE = 7

class GetPenaltyValueUseCase {

    @Suppress("unused_parameter")
    operator fun invoke(foul: Foul): Int = PENALTY_VALUE

}
