package com.personal.student_management.data.repositoryImpl

import com.personal.student_management.data.local.dao.StudentDao
import com.personal.student_management.data.local.model.StudentEntity
import com.personal.student_management.data.repository.StudentRepository
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [StudentRepository] that interacts with the local database using [StudentDao].
 *
 * @param studentDao The [StudentDao] interface to perform database operations.
 */
@Singleton
class StudentRepositoryImpl constructor(private val studentDao: StudentDao) : StudentRepository {
    /**
     * Retrieves all students from the local database.
     *
     * @return A list of [StudentEntity] objects representing all students in the database.
     */
    override suspend fun getAllStudents(): List<StudentEntity> {
        return studentDao.getAllStudents()
    }

    /**
     * Inserts a new student into the local database.
     *
     * @param student The [StudentEntity] object representing the student to be inserted.
     */
    override suspend fun insertStudent(student: StudentEntity) {
        studentDao.insertStudent(student)
    }

    /**
     * Updates an existing student in the local database.
     *
     * @param student The [StudentEntity] object representing the updated student data.
     */
    override suspend fun updateStudent(student: StudentEntity) {
        studentDao.updateStudent(student)
    }

    /**
     * Deletes a student from the local database.
     *
     * @param student The [StudentEntity] object representing the student to be deleted.
     */
    override suspend fun deleteStudent(student: StudentEntity) {
        studentDao.deleteStudent(student)
    }

    /**
     * Retrieves a student from the local database by their ID.
     *
     * @param studentId The ID of the student to find.
     * @return The [StudentEntity] representing the found student, or null if not found.
     */
    override suspend fun findStudentById(studentId: Int): StudentEntity? {
        return studentDao.findStudentById(studentId)
    }

}
