package com.example.checkmate

import TasksDatabaseHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.checkmate.databinding.ActivityUpdateTaskBinding

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var db: TasksDatabaseHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        taskId = intent.getIntExtra("note_id", -1)
        if (taskId == -1) {
            finish()
            return
        }

        val task = db.getTaskByID(taskId)
        if (task != null) {
            binding.updateTitleEditText.setText(task.title)
            binding.updateContentEditText.setText(task.content)
            binding.updateDeadlineEditText.setText(task.content)

            binding.updateSaveButton.setOnClickListener {
                val newTitle = binding.updateTitleEditText.text.toString()
                val newContent = binding.updateContentEditText.text.toString()
                val newDeadline = binding.updateContentEditText.text.toString()
                val updatedTask = Task(taskId, newTitle, newContent,newDeadline)
                db.updateTask(updatedTask)
                finish()
                Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle case when task is not found.
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
