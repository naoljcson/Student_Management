package com.personal.student_management.domain.usecase

import com.personal.student_management.data.repository.StudentRepository
import com.personal.student_management.domain.model.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Use case for retrieving a list of students from the repository.
 *
 * @param studentRepository The repository for student data.
 */
class GetStudentsUseCase @Inject constructor(private val studentRepository: StudentRepository) {
    /**
     * Executes the use case to retrieve a list of students from the repository.
     *
     * @return A list of [Student] objects representing all students.
     */
    suspend fun execute(): List<Student> {
        val studentEntities = studentRepository.getAllStudents()
        return studentEntities.map { studentEntity ->
            Student(
                id = studentEntity.id,
                firstName = studentEntity.firstName,
                lastName = studentEntity.lastName,
                dateOfBirth = studentEntity.dateOfBirth,
                photo = studentEntity.photo
            )
        }
    }
}
