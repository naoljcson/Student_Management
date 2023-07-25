package com.personal.student_management.data.repository

import com.personal.student_management.data.local.model.StudentEntity
import dagger.Provides

/**
 * Interface for accessing and managing student data from various data sources.
 */

interface StudentRepository {
    /**
     * Retrieves all students from the data source.
     *
     * @return A list of [StudentEntity] objects representing all students.
     */
    suspend fun getAllStudents(): List<StudentEntity>

    /**
     * Inserts a new student into the data source.
     *
     * @param student The [StudentEntity] object representing the student to be inserted.
     */
    suspend fun insertStudent(student: StudentEntity)

    /**
     * Updates an existing student in the data source.
     *
     * @param student The [StudentEntity] object representing the updated student data.
     */
    suspend fun updateStudent(student: StudentEntity)

    /**
     * Deletes a student from the data source.
     *
     * @param student The [StudentEntity] object representing the student to be deleted.
     */
    suspend fun deleteStudent(student: StudentEntity)

    /**
     * Find a student from the data source.
     *
     * @param studentId The [StudentEntity] object representing the student.
     */
    suspend fun findStudentById(studentId: Int): StudentEntity?
}
