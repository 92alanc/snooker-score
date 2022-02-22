package com.alancamargo.snookerscore.domain.usecase.rules

import com.alancamargo.snookerscore.domain.repository.RulesUrlRepository

class GetRulesUrlUseCase(private val repository: RulesUrlRepository) {

    operator fun invoke(): String = repository.getRulesUrl()

}
