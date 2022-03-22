package com.alancamargo.snookerscore.features.frame.domain.usecase

import com.alancamargo.snookerscore.features.frame.domain.model.Frame
import com.alancamargo.snookerscore.features.frame.domain.repository.FrameRepository
import kotlinx.coroutines.flow.Flow

class AddOrUpdateFrameUseCase(private val repository: FrameRepository) {

    operator fun invoke(frame: Frame): Flow<Unit> = repository.addOrUpdateFrame(frame)

}
