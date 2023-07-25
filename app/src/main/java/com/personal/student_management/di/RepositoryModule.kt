package com.personal.student_management.di

import com.personal.student_management.data.local.dao.StudentDao
import com.personal.student_management.data.local.database.StudentManagementDatabase
import com.personal.student_management.data.repository.StudentRepository
import com.personal.student_management.data.repositoryImpl.StudentRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideStudentRepository(studentDao: StudentDao): StudentRepository {
        return StudentRepositoryImpl(studentDao)
    }
}