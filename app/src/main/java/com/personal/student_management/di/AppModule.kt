package com.personal.student_management.di

import android.content.Context
import com.personal.student_management.StudentManagementApp
import com.personal.student_management.utils.FileHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides application-level dependencies.
 *
 * @param application The Application instance provided by Dagger Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the Application instance to be used for dependency injection.
     *
     * @return The Application instance.
     */
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context) = app as StudentManagementApp

    @Singleton
    @Provides
    fun provideImageSaver(@ApplicationContext context: Context): FileHelper {
        return FileHelper(context)
    }
}
