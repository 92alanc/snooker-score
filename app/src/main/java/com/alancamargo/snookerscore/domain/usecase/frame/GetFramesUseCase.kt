package com.alancamargo.snookerscore.domain.usecase.frame

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import kotlinx.coroutines.flow.Flow

class GetFramesUseCase(private val repository: FrameRepository) {

    operator fun invoke(match: Match): Flow<List<Frame>> = repository.getFrames(match)

}
