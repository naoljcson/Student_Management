package com.personal.student_management.domain.usecase

import com.personal.student_management.data.local.model.StudentEntity
import com.personal.student_management.data.repository.StudentRepository
import com.personal.student_management.domain.model.Student
import com.personal.student_management.utils.UseCaseResult
import javax.inject.Inject

/**
 * Use case responsible for updating a student's data and managing related file operations.
 *
 * @param studentRepository The repository for accessing and updating student data.
 */
class UpdateStudentUseCase @Inject constructor(
    private val studentRepository: StudentRepository
) {
    /**
     * Updates the data of a student in the repository and handles related file operations if necessary.
     *
     * @param student The updated student data to be saved.
     */
    suspend fun execute(student: Student): UseCaseResult<Unit> {
        // Create a new StudentEntity object with the updated data
        return try {
            val studentEntity = StudentEntity(
                id = student.id,
                firstName = student.firstName,
                lastName = student.lastName,
                dateOfBirth = student.dateOfBirth,
                photo = student.photo
            )
            // Update the student data in the repository
            studentRepository.updateStudent(studentEntity)
            UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            UseCaseResult.Error("\"Failed to update student data.\"")
        }
    }
}
