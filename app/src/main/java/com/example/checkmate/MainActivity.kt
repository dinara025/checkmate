package com.example.checkmate

import TasksDatabaseHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checkmate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: TasksDatabaseHelper
    private lateinit var tasksAdapter: TasksAdapter
    private var originalTasksList: List<Task> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)
        originalTasksList = db.getAllTasks()
        tasksAdapter = TasksAdapter(originalTasksList, this)

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = tasksAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        // Set up search functionality
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTasks(newText)
                return true
            }
        })
    }

    private fun filterTasks(query: String?) {
        val filteredTasksList = ArrayList<Task>()
        if (!query.isNullOrEmpty()) {
            for (task in originalTasksList) {
                if (task.title.contains(query, true)) {
                    filteredTasksList.add(task)
                }
            }
        } else {
            filteredTasksList.addAll(originalTasksList)
        }
        tasksAdapter.refreshData(filteredTasksList)
    }

    override fun onResume() {
        super.onResume()
        originalTasksList = db.getAllTasks()
        tasksAdapter.refreshData(originalTasksList)
    }
}
