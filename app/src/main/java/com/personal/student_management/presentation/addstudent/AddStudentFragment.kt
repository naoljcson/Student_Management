package com.personal.student_management.presentation.addstudent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.personal.student_management.R
import com.personal.student_management.databinding.FragmentAddStudentBinding
import com.personal.student_management.domain.model.Student
import com.personal.student_management.utils.FileHelper
import com.personal.student_management.utils.UseCaseResult
import com.personal.student_management.utils.toStringDate
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddStudentFragment : Fragment() {

    private var _binding: FragmentAddStudentBinding? = null
    private val binding get() = _binding
    private val viewModel: AddStudentViewModel by viewModels()
    private lateinit var pickPhotoLauncher: ActivityResultLauncher<Intent>
    private var dateOfBirth: Long = 0
    private lateinit var photoPath: String
    private lateinit var oldPhotoPath: String
    private var id: Int? = null
    private var selectedImageUri: Uri? = null

    @Inject
    lateinit var fileHelper: FileHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPickPhotoLauncher()
        setupDatePicker()

        val studentId: Int? = arguments?.getInt("studentId")

        if (studentId != null && studentId > 0) {
            id = studentId
            setupUpdateUI()
            viewModel.getStudentById(studentId)
        } else {
            setupSaveUI()
        }

        binding?.apply {
            editTextUploadPhoto.setOnClickListener {
                val pickPhotoIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickPhotoLauncher.launch(pickPhotoIntent)
            }
            buttonCancel.setOnClickListener {
                if (photoPath.isNotEmpty() && (id == null || id == 0)) {
                    fileHelper.deleteImageFile(photoPath)
                }
                findNavController().popBackStack()
            }
        }
        setupTextWatchers()
    }

    private fun setTitle(title: String) {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = title
    }

    private fun setupUpdateUI() {
        setTitle("Edit Student")
        binding?.apply {
            buttonSave.apply {
                setOnClickListener { updateStudentData() }
                text = getString(R.string.update)
            }
        }
        observeFindStudent()
    }

    private fun setupSaveUI() {
        setTitle("Create New Student")
        binding?.buttonSave?.setOnClickListener {
            saveStudentData()
        }
        photoPath = ""
    }

    private fun observeFindStudent() {
        lifecycleScope.launchWhenStarted {
            viewModel.findStudent.collect { student ->
                student?.let {
                    updateUI(student)
                }
            }
        }
    }

    private fun saveStudentData() {
        if (isInputFieldsValidate()) {
            saveImageToLocalStorage()
            val student = createStudentFromUI()
            viewModel.saveStudent(student)
            observeAddStudent()
            findNavController().popBackStack()
        }
    }

    private fun updateStudentData() {
        if (isInputFieldsValidate()) {
            id?.let {
                saveImageToLocalStorage()
                val student = createStudentFromUI()
                viewModel.updateStudent(student)
                observeUpdateStudent()
                if (oldPhotoPath != photoPath) {
                    fileHelper.deleteImageFile(oldPhotoPath)
                }
            }
            findNavController().popBackStack()
        }
    }

    private fun observeAddStudent() {
        lifecycleScope.launchWhenStarted {
            viewModel.addStudent.collect { result ->
                when (result) {
                    is UseCaseResult.Success -> {
                        showSnackbar("Student data saved successfully.")
                    }
                    is UseCaseResult.Error -> {
                        // Error occurred while saving student data. Display error message if needed.
                        showSnackbar(result.message)
                    }
                    else -> {
                        showSnackbar("Something went wrong")
                    }
                }
            }
        }
    }

    private fun observeUpdateStudent() {
        lifecycleScope.launchWhenStarted {
            viewModel.updateStudent.collect { result ->
                when (result) {
                    is UseCaseResult.Success -> {
                        showSnackbar("Student data Updated successfully.")
                    }
                    is UseCaseResult.Error -> {
                        showSnackbar(result.message)
                    }
                    else -> {
                        showSnackbar("Something went wrong")
                    }
                }
            }
        }
    }


    private fun createStudentFromUI(): Student {
        val studentId = id ?: 0
        return Student(
            studentId,
            binding?.txtInputFirstName?.text.toString(),
            binding?.txtInputLastName?.text.toString(),
            dateOfBirth,
            photoPath
        )
    }

    private fun setupDatePicker() {
        val endOfMonth = Date().time
        binding?.editTextBirthDate?.setOnClickListener {
            val constraintsBuilder =
                CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())
            constraintsBuilder.setEnd(endOfMonth)

            val builder = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())

            builder.setTitleText("Select Birth Date")
            val picker = builder.build()
            picker.show(parentFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener { selection ->
                dateOfBirth = selection
                binding?.editTextBirthDate?.setText(selection.toStringDate())
            }
        }
    }

    private fun setupPickPhotoLauncher() {
        pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    result.data?.data?.let { selectedImageUri ->
                        this.selectedImageUri = selectedImageUri
                        binding?.editTextUploadPhoto?.setText("1 file selected")
                    }
                }
            }
    }

    private fun setupTextWatchers() {
        binding?.apply {
            txtInputFirstName.addTextChangedListener(createTextWatcher(txtLayoutFirstName))
            txtInputLastName.addTextChangedListener(createTextWatcher(txtLayoutLastName))
            editTextBirthDate.addTextChangedListener(createTextWatcher(txtLayoutBirthDate))
            editTextUploadPhoto.addTextChangedListener(createTextWatcher(txtLayoutUploadPhoto))
        }
    }

    private fun createTextWatcher(inputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Clear the error when the user starts typing
                inputLayout.isErrorEnabled = false
            }
        }
    }

    private fun saveImageToLocalStorage() {
        selectedImageUri?.let {
            val filename = "${Date().time}_.jpg"
            val filePath: String? = fileHelper.saveImageToInternalStorage(it, filename)
            if (filePath != null) {
                photoPath = filePath
            } else {
                photoPath = ""
                showSnackbar("Error saving image")
            }
        }
    }

    private fun updateUI(student: Student) {
        binding?.apply {
            txtInputFirstName.setText(student.firstName)
            txtInputLastName.setText(student.lastName)
            editTextBirthDate.setText(student.dateOfBirth.toStringDate())
            editTextUploadPhoto.setText(getLastChunkOfFilepath(student.photo))
        }
        dateOfBirth = student.dateOfBirth
        photoPath = student.photo
        oldPhotoPath = photoPath
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun getLastChunkOfFilepath(filePath: String): String {
        val delimiter = "/"
        return filePath.substringAfterLast(delimiter)
    }

    private fun isInputFieldsValidate(): Boolean {
        var isValid = false
        binding?.apply {
            isValid = validateField(
                txtInputFirstName,
                txtLayoutFirstName,
                "Please Enter First Name"
            ) && validateField(
                txtInputLastName, txtLayoutLastName, "Please Enter Last Name"
            ) && validateField(
                editTextBirthDate, txtLayoutBirthDate, "Please Select Date Of Birth"
            ) && validateField(
                editTextUploadPhoto, txtLayoutUploadPhoto, "Please Upload student Photo"
            )
        }
        return isValid
    }

    private fun validateField(
        inputField: TextInputEditText, inputLayout: TextInputLayout, errorMessage: String
    ): Boolean {
        val isValid = !inputField.text.isNullOrEmpty()
        if (!isValid) {
            inputLayout.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            inputLayout.isErrorEnabled = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
