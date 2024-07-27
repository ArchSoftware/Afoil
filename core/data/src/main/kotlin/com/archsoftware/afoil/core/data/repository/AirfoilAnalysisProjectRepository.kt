package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.data.model.asEntity
import com.archsoftware.afoil.core.database.dao.AirfoilAnalysisProjectDao
import com.archsoftware.afoil.core.database.model.AirfoilAnalysisProjectEntity
import com.archsoftware.afoil.core.database.model.asExternalModel
import com.archsoftware.afoil.core.model.AirfoilAnalysisProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AirfoilAnalysisProjectRepository @Inject constructor(
    private val airfoilAnalysisProjectDao: AirfoilAnalysisProjectDao
) : AnalysisProjectRepository {
    override fun getProjects(): Flow<List<AirfoilAnalysisProject>> =
        airfoilAnalysisProjectDao.getProjects().map {
            it.map(AirfoilAnalysisProjectEntity::asExternalModel)
        }

    override suspend fun insertProject(project: AirfoilAnalysisProject) {
        airfoilAnalysisProjectDao.insertProject(project.asEntity())
    }
}