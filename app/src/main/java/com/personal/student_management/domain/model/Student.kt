package com.personal.student_management.domain.model

/**
 * Represents a student entity in the domain layer.
 *
 * @param id The unique identifier for the student.
 * @param firstName The first name of the student.
 * @param lastName The last name of the student.
 * @param dateOfBirth The date of birth of the student.
 * @param photo The file path or URL of the student's photo.
 */
data class Student(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: Long,
    val photo: String
)
