package com.archsoftware.afoil.core.data.repository

import com.archsoftware.afoil.core.data.model.asEntity
import com.archsoftware.afoil.core.database.dao.AfoilProjectDao
import com.archsoftware.afoil.core.database.model.AfoilProjectEntity
import com.archsoftware.afoil.core.database.model.asExternalModel
import com.archsoftware.afoil.core.model.AfoilProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository that provides management methods for [AfoilProject] objects.
 */
class AfoilProjectRepository @Inject constructor(
    private val afoilProjectDao: AfoilProjectDao
) : ProjectRepository {
    override fun getProjects(): Flow<List<AfoilProject>> =
        afoilProjectDao.getProjects().map {
            it.map(AfoilProjectEntity::asExternalModel)
        }

    override fun getProjectById(id: Long): Flow<AfoilProject> =
        afoilProjectDao.getProjectById(id).map(AfoilProjectEntity::asExternalModel)

    override suspend fun insertProject(project: AfoilProject): Long =
        afoilProjectDao.insertProject(project.asEntity())

    override suspend fun deleteProjects(projects: List<AfoilProject>) {
        afoilProjectDao.deleteProjects(projects.map(AfoilProject::asEntity))
    }
}