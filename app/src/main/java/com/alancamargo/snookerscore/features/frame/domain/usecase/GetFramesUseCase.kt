package com.alancamargo.snookerscore.features.frame.domain.usecase

import com.alancamargo.snookerscore.features.frame.domain.model.Frame
import com.alancamargo.snookerscore.features.frame.domain.repository.FrameRepository
import com.alancamargo.snookerscore.features.match.domain.model.Match
import kotlinx.coroutines.flow.Flow

class GetFramesUseCase(private val repository: FrameRepository) {

    operator fun invoke(match: Match): Flow<List<Frame>> = repository.getFrames(match)

}
