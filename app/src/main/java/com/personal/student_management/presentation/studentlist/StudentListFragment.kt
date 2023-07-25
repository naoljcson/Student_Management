package com.personal.student_management.presentation.studentlist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.personal.student_management.R
import com.personal.student_management.databinding.FragmentStudentListBinding
import com.personal.student_management.domain.model.Student
import com.personal.student_management.presentation.studentlist.adapter.StudentListAdapter
import com.personal.student_management.utils.OnMenuItemClickListener
import com.personal.student_management.utils.UseCaseResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StudentListFragment : Fragment(), OnMenuItemClickListener {

    companion object {
        fun newInstance() = StudentListFragment()
        private val TAG = StudentListFragment::class.qualifiedName
    }

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding
    private val viewModel: StudentListViewModel by viewModels()
    private val students = mutableListOf<Student>()
    private lateinit var studentssListAdapter: StudentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudentListBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentssListAdapter = StudentListAdapter(students, this)
        binding?.apply {
            rvStudentList.adapter = studentssListAdapter
            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_studentListFragment_to_addStudentFragment)
            }
        }
        viewModel.loadStudents()
        observerStudentList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observerStudentList() {
        lifecycleScope.launchWhenStarted {
            viewModel.students.collect { result ->
                students.clear()
                students.addAll(result)
                studentssListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun observerDeleteStudent() {
        lifecycleScope.launchWhenStarted {
            viewModel.deleteStudentResult.collect { result ->
                when (result) {
                    is UseCaseResult.Success -> {
                        showSnackbar("Student data Deleted successfully.")
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Student List"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onEditClick(position: Int) {
        val student = students[position]
        val action =
            StudentListFragmentDirections.actionStudentListFragmentToAddStudentFragment(student.id)
        findNavController().navigate(action)
    }

    override fun onDeleteClick(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Student")
        builder.setMessage("Are you sure you want to delete this Student?")
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteStudent(students[position])
            observerDeleteStudent()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}