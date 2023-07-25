package com.personal.student_management.domain.usecase

import com.personal.student_management.data.repository.StudentRepository
import com.personal.student_management.domain.model.Student
import javax.inject.Inject

/**
 * Use case for finding a student by their ID in the repository.
 *
 * @param studentRepository The repository for student data.
 */
class FindStudentByIdUseCase @Inject constructor(private val studentRepository: StudentRepository) {

    /**
     * Executes the use case to find a student by their ID in the repository.
     *
     * @param studentId The ID of the student to find.
     * @return A [Student] object representing the found student, or null if not found.
     */
    suspend fun execute(studentId: Int): Student? {
        // Retrieve the student entity from the repository based on the student ID
        val studentEntity = studentRepository.findStudentById(studentId)

        // If the student entity is not null, create a Student object from it
        // and return the Student, else return null
        return studentEntity?.let {
            Student(
                id = it.id,
                firstName = it.firstName,
                lastName = it.lastName,
                dateOfBirth = it.dateOfBirth,
                photo = it.photo
            )
        }
    }
}
