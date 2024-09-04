package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.data.model.asEntity
import com.archsoftware.afoil.core.database.dao.AfoilProjectDataDao
import com.archsoftware.afoil.core.database.model.AfoilProjectDataEntity
import com.archsoftware.afoil.core.database.model.asExternalModel
import com.archsoftware.afoil.core.model.AfoilProjectData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository that provides management methods for [AfoilProjectData] objects.
 */
class AfoilProjectDataRepository @Inject constructor(
    private val afoilProjectDataDao: AfoilProjectDataDao
) : ProjectDataRepository {
    override fun getProjectDataByProjectId(projectId: Long): Flow<AfoilProjectData> =
        afoilProjectDataDao
            .getProjectDataByProjectId(projectId)
            .map(AfoilProjectDataEntity::asExternalModel)

    override suspend fun insertProjectData(projectData: AfoilProjectData) {
        afoilProjectDataDao.insertProjectData(projectData.asEntity())
    }

    override suspend fun deleteProjectDataByProjectId(projectId: Long) {
        afoilProjectDataDao.deleteProjectDataByProjectId(projectId)
    }
}