package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //1. remove item from list
                listOfTasks.removeAt(position)

                //2. notify adapter that data changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //1. detect when user clicks on add
//        findViewById<Button>(R.id.button).setOnClickListener {
//            //code is executed when click on button
//            Log.i("Caren", "User clicked on button")
//        }

        loadItems()

        //look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up button and input field so user can enter task into list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.button).setOnClickListener {
            //1. grab text user inputted @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. add string to task listOfTasks
            listOfTasks.add(userInputtedTask)

            //notify adapter
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. reset text
            inputTextField.setText("")

            saveItems()
        }
    }

    //save data that user inputted by writing and reading from a file

    //create method to get file needed
    fun getDataFile(): File {

        //every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    //load item by reading every line in file
    fun loadItems() {
        try{
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //save items by writing them into data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)

        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}