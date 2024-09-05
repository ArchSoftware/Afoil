package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.data.model.asEntity
import com.archsoftware.afoil.core.database.dao.AfoilProjectPostResultDao
import com.archsoftware.afoil.core.database.model.AfoilProjectPostResultEntity
import com.archsoftware.afoil.core.database.model.asExternalModel
import com.archsoftware.afoil.core.model.AfoilProjectPostResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository that provides management methods for [AfoilProjectPostResult] objects.
 */
class AfoilProjectPostResultRepository @Inject constructor(
    private val projectPostResultDao: AfoilProjectPostResultDao
) : ProjectPostResultRepository {
    override fun getProjectPostResultsByProjectId(projectId: Long): Flow<List<AfoilProjectPostResult>> =
        projectPostResultDao.getProjectPostResultsByProjectId(projectId).map {
            it.map(AfoilProjectPostResultEntity::asExternalModel)
        }

    override suspend fun insertProjectPostResult(projectPostResult: AfoilProjectPostResult) {
        projectPostResultDao.insertProjectPostResult(projectPostResult.asEntity())
    }

    override suspend fun deleteProjectPostResultsByProjectId(projectId: Long) {
        projectPostResultDao.deleteProjectPostResultsByProjectId(projectId)
    }
}