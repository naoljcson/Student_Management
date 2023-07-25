package com.personal.student_management.data.local.dao

import androidx.room.*
import com.personal.student_management.data.local.model.StudentEntity

@Dao
interface StudentDao {
    /**
     * Retrieves all students from the database.
     *
     * @return A list of [StudentEntity] objects representing all students in the database.
     */
    @Query("SELECT * FROM students")
    suspend fun getAllStudents(): List<StudentEntity>

    /**
     * Inserts or replaces a student in the database.
     * If a student with the same primary key exists, it will be replaced, otherwise inserted as a new entry.
     *
     * @param student The [StudentEntity] object to be inserted or replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    /**
     * Updates an existing student in the database.
     * The [StudentEntity] object must already exist in the database with the same primary key.
     *
     * @param student The [StudentEntity] object to be updated.
     */
    @Update
    suspend fun updateStudent(student: StudentEntity)

    /**
     * Deletes a student from the database.
     *
     * @param student The [StudentEntity] object to be deleted.
     */
    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    /**
     * Retrieves a student from the local database by their ID.
     *
     * @param studentId The ID of the student to find.
     * @return The [StudentEntity] representing the found student, or null if not found.
     */
    @Query("SELECT * FROM students WHERE id = :studentId")
    suspend fun findStudentById(studentId: Int): StudentEntity?
}
