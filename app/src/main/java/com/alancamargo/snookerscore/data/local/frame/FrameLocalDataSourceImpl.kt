package com.alancamargo.snookerscore.data.local.frame

import com.alancamargo.snookerscore.data.db.FrameDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.domain.model.Frame
import kotlinx.coroutines.flow.flow

class FrameLocalDataSourceImpl(private val frameDao: FrameDao) : FrameLocalDataSource {

    override fun addFrame(frame: Frame) = flow {
        val task = frameDao.addFrame(frame.toData())
        emit(task)
    }

    override fun deleteFrame(frame: Frame) = flow {
        val task = frameDao.deleteFrame(frame.id)
        emit(task)
    }

}
