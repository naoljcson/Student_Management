package com.personal.student_management.presentation.addstudent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.student_management.domain.model.Student
import com.personal.student_management.domain.usecase.AddStudentUseCase
import com.personal.student_management.domain.usecase.FindStudentByIdUseCase
import com.personal.student_management.domain.usecase.UpdateStudentUseCase
import com.personal.student_management.utils.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStudentViewModel @Inject constructor(
    private val addStudentUseCase: AddStudentUseCase,
    private val findStudentByIdUseCase: FindStudentByIdUseCase,
    private val updateStudentUseCase: UpdateStudentUseCase
) : ViewModel() {

    private var _addStudent = MutableStateFlow<UseCaseResult<Unit>?>(null)
    val addStudent = _addStudent.asStateFlow()

    private var _updateStudent = MutableStateFlow<UseCaseResult<Unit>?>(null)
    val updateStudent = _updateStudent.asStateFlow()

    private var _findStudent = MutableStateFlow<Student?>(null)
    val findStudent = _findStudent.asStateFlow()

    fun saveStudent(student: Student) = viewModelScope.launch {
        val result = addStudentUseCase.execute(student)
        _addStudent.value = result
    }

    fun getStudentById(studentId: Int) = viewModelScope.launch {
        val result = findStudentByIdUseCase.execute(studentId)
        _findStudent.value = result
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        _updateStudent.value = updateStudentUseCase.execute(student)
    }
}