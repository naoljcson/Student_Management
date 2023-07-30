package com.personal.student_management.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.student_management.data.local.dao.StudentDao
import com.personal.student_management.data.local.model.StudentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

/**
 * The Room Database for managing student data.
 */
@Database(entities = [StudentEntity::class], version = 1)
abstract class StudentManagementDatabase : RoomDatabase() {
    /**
     * Provides access to the StudentDao for performing database operations related to students.
     *
     * @return An instance of [StudentDao].
     */
    abstract fun studentDao(): StudentDao
}
