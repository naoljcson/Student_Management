package com.personal.student_management.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.student_management.data.local.dao.StudentDao
import com.personal.student_management.data.local.database.StudentManagementDatabase
import com.personal.student_management.data.local.model.StudentEntity
import com.personal.student_management.utils.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Singleton

/**
 * Module that provides the Database dependencies using Dagger Hilt.
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    /**
     * Provides a singleton instance of the Room Database.
     *
     * @param application The application context.
     * @return Singleton instance of the StudentManagementDatabase.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): StudentManagementDatabase {
        val dbCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                // Insert initial data into the database here
                val studentDao = provideAppDatabase(application).studentDao()
                CoroutineScope(Dispatchers.IO).launch {
                    val seedDataList = listOf(
                        StudentEntity(0, "John", "Doe", Date().time, ""),
                        StudentEntity(0, "Jane", "Smith", Date().time, ""),
                        StudentEntity(0, "Alex", "Johnson", Date().time, "")
                    )
                    seedDataList.forEach { seedData ->
                        studentDao.insertStudent(seedData)
                    }
                }
            }
        }

        return Room.databaseBuilder(
            application, StudentManagementDatabase::class.java, DB_NAME
        )
            .addCallback(dbCallback)
            .build()
    }

    /**
     * Provides the StudentDao instance.
     *
     * @param appDatabase The Room database instance.
     * @return The StudentDao instance to perform database operations.
     */
    @Provides
    fun provideStudentDao(appDatabase: StudentManagementDatabase): StudentDao {
        return appDatabase.studentDao()
    }

}
