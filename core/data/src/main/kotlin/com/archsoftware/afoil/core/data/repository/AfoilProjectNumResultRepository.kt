package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.data.model.asEntity
import com.archsoftware.afoil.core.database.dao.AfoilProjectNumResultDao
import com.archsoftware.afoil.core.database.model.AfoilProjectNumResultEntity
import com.archsoftware.afoil.core.database.model.asExternalModel
import com.archsoftware.afoil.core.model.AfoilProjectNumResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository that provides management methods for [AfoilProjectNumResult] objects.
 */
class AfoilProjectNumResultRepository @Inject constructor(
    private val afoilProjectNumResultDao: AfoilProjectNumResultDao
) : ProjectNumResultRepository {
    override fun getProjectNumResultByProjectId(projectId: Long): Flow<AfoilProjectNumResult> =
        afoilProjectNumResultDao
            .getProjectNumResultsByProjectId(projectId)
            .map(AfoilProjectNumResultEntity::asExternalModel)

    override suspend fun insertProjectNumResult(projectNumResult: AfoilProjectNumResult) {
        afoilProjectNumResultDao.insertProjectNumResult(projectNumResult.asEntity())
    }

    override suspend fun deleteProjectNumResultByProjectId(projectId: Long) {
        afoilProjectNumResultDao.deleteProjectNumResultByProjectId(projectId)
    }
}