package com.personal.student_management.domain.usecase

import com.personal.student_management.data.local.model.StudentEntity
import com.personal.student_management.data.repository.StudentRepository
import com.personal.student_management.domain.model.Student
import com.personal.student_management.utils.UseCaseResult
import javax.inject.Inject

/**
 * Use case for adding a new student to the repository.
 *
 * @param studentRepository The repository for student data.
 */
class AddStudentUseCase @Inject constructor(private val studentRepository: StudentRepository) {
    /**
     * Executes the use case to add a new student to the repository.
     *
     * @param student The [Student] object representing the student to be added.
     */
    suspend fun execute(student: Student): UseCaseResult<Unit> {
        return try {
            val studentEntity = StudentEntity(
                firstName = student.firstName,
                lastName = student.lastName,
                dateOfBirth = student.dateOfBirth,
                photo = student.photo
            )
            studentRepository.insertStudent(studentEntity)
            UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            UseCaseResult.Error("Failed to save student data.")
        }
    }
}
