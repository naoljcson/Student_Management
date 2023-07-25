package com.personal.student_management.presentation.studentlist.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.personal.student_management.R
import com.personal.student_management.databinding.ItemStudentBinding
import com.personal.student_management.domain.model.Student
import com.personal.student_management.utils.OnMenuItemClickListener
import com.personal.student_management.utils.toStringDate

class StudentListAdapter(
    private val students: List<Student>,
    private val onMenuItemClickListener: OnMenuItemClickListener
) :
    RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fullName = "${students[position].firstName} ${students[position].lastName}"
        holder.binding.apply {
            when {
                students[position].photo.isEmpty() -> {
                    imgStudent.setImageResource(R.drawable.student)
                }
                else -> {
                    val bitmap = BitmapFactory.decodeFile(students[position].photo)
                    imgStudent.setImageBitmap(bitmap)
                }
            }
            txtStudentName.text = fullName
            txtStudentBirthDate.text = students[position].dateOfBirth.toStringDate()
            imgMenu.setOnClickListener { view ->
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.inflate(R.menu.item_menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit -> {
                            onMenuItemClickListener.onEditClick(position)
                            true
                        }
                        R.id.action_delete -> {
                            onMenuItemClickListener.onDeleteClick(position)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }

    override fun getItemCount() = students.size
}