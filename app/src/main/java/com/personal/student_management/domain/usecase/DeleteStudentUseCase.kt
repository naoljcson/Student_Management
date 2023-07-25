package com.personal.student_management.domain.usecase

import com.personal.student_management.data.local.model.StudentEntity
import com.personal.student_management.data.repository.StudentRepository
import com.personal.student_management.domain.model.Student
import com.personal.student_management.utils.FileHelper
import com.personal.student_management.utils.UseCaseResult
import javax.inject.Inject

/**
 * Use case for deleting an existing student from the repository.
 *
 * @param studentRepository The repository for student data.
 */
class DeleteStudentUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
    private val fileHelper: FileHelper
) {
    /**
     * Executes the use case to delete an existing student from the repository.
     *
     * @param student The [Student] object representing the student to be deleted.
     */
    suspend fun execute(student: Student): UseCaseResult<Unit> {
        return try {
            val studentEntity = StudentEntity(
                id = student.id,
                firstName = student.firstName,
                lastName = student.lastName,
                dateOfBirth = student.dateOfBirth,
                photo = student.photo
            )
            studentRepository.deleteStudent(studentEntity)
            fileHelper.deleteImageFile(student.photo)
            UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            UseCaseResult.Error("Failed to delete student data.")
        }
    }
}
