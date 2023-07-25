package com.personal.student_management.presentation.studentlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.personal.student_management.domain.model.Student
import com.personal.student_management.domain.usecase.DeleteStudentUseCase
import com.personal.student_management.domain.usecase.GetStudentsUseCase
import com.personal.student_management.utils.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the list of students.
 *
 * This ViewModel is responsible for providing a list of students to the UI layer.
 * It interacts with the [GetStudentsUseCase] to retrieve the list of students and exposes
 * it through a [students] flow.
 *
 * @property getStudentsUseCase The use case to retrieve the list of students.
 */
@HiltViewModel
class StudentListViewModel @Inject constructor(
    private val getStudentsUseCase: GetStudentsUseCase,
    private val deleteStudentUseCase: DeleteStudentUseCase
) : ViewModel() {

    // A mutable state flow to hold the list of students. It can be updated from within the ViewModel.
    private val _students = MutableStateFlow<List<Student>>(emptyList())

    // An immutable state flow that exposes the list of students to the UI layer.
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _deleteStudentResult = MutableStateFlow<UseCaseResult<Unit>?>(null)
    val deleteStudentResult = _deleteStudentResult.asStateFlow()


    /**
     * Launches a coroutine to load the list of students.
     *
     * This function uses the [viewModelScope] to launch a coroutine in the ViewModel's scope.
     * It calls the [GetStudentsUseCase] to get the list of students and emits it to the [_students]
     * mutable state flow.
     *
     * Note: The [loadStudents] function is automatically called when the ViewModel is created
     * to initialize the list of students.
     */
    fun loadStudents() = viewModelScope.launch {
        val studentsList = getStudentsUseCase.execute()
        _students.value = studentsList
    }

    fun deleteStudent(student: Student){
        viewModelScope.launch {
            val result = deleteStudentUseCase.execute(student)
            _deleteStudentResult.value = result
            when (result) {
                is UseCaseResult.Success -> {
                    val updatedList = _students.value.toMutableList()
                    updatedList.remove(student)
                    _students.value = updatedList
                }
                is UseCaseResult.Error -> {

                }
            }
        }
    }


}
