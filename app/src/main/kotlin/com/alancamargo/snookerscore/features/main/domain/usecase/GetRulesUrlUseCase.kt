package com.alancamargo.snookerscore.features.main.domain.usecase

import com.alancamargo.snookerscore.features.main.domain.repository.RulesUrlRepository

class GetRulesUrlUseCase(private val repository: RulesUrlRepository) {

    operator fun invoke(): String = repository.getRulesUrl()

}
