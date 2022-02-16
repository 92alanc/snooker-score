package com.alancamargo.snookerscore.domain.usecase.frame

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import kotlinx.coroutines.flow.Flow

class DeleteFrameUseCase(private val repository: FrameRepository) {

    operator fun invoke(frame: Frame): Flow<Unit> = repository.deleteFrame(frame)

}
