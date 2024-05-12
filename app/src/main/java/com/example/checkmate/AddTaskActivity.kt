package com.example.checkmate

import TasksDatabaseHelper
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.checkmate.databinding.ActivityAddTaskBinding


class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var  db:TasksDatabaseHelper
    private lateinit var deadlineEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= TasksDatabaseHelper(this)
        deadlineEditText = binding.deadlineEditText

        binding.saveButton.setOnClickListener{
            val title=binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val deadline = binding.deadlineEditText.text.toString()
            val task = Task(0,title,content,deadline)
            db.insertTask(task)
            finish()
            Toast.makeText(this,"Task Saved",Toast.LENGTH_SHORT).show()
        }
    }
    fun showDatePickerDialog(v: View?) {
        // Get current date
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog and show
        val datePickerDialog = DatePickerDialog(this,
            { view, year, monthOfYear, dayOfMonth -> // Set selected date on EditText
                val selectedDate = (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                deadlineEditText.setText(selectedDate)
            }, year, month, dayOfMonth
        )
        datePickerDialog.show()
    }
}