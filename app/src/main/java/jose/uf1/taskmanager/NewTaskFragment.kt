package jose.uf1.taskmanager

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import jose.uf1.taskmanager.databinding.FragmentNewTaskBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.Calendar


class NewTaskFragment : Fragment() {
    private lateinit var binding: FragmentNewTaskBinding
    private var selectedDeadline: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        val btnHome = binding.fab
        btnHome.setOnClickListener{
            view.findNavController().navigate(R.id.action_newTaskFragment_to_choiceFragment)
        }
        binding.buttonCalendar.setOnClickListener {
            showDatePickerDialog()
        }
        binding.confirmButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && selectedDeadline != 0L) {
                val newTask = Task(title, description, selectedDeadline)

                saveTaskToFile(requireContext(), newTask)
                Snackbar.make(binding.root, "Tarea guardada correctamente", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Por favor, completa todos los campos y selecciona una fecha", Snackbar.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(requireContext())
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            // Obtener la fecha seleccionada y establecerla como la fecha límite
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDeadline = calendar.timeInMillis
        }
        datePicker.show()
    }
    private fun saveTaskToFile(context: Context, newTask: Task) {
        val tasks = loadTasksFromFile(context).toMutableList()
        tasks.add(newTask)

        val gson = Gson()
        val json = gson.toJson(tasks)
        context.openFileOutput("tasks.json", Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }
    private fun loadTasksFromFile(context: Context): List<Task> {
        val gson = Gson()
        return try {
            val inputStream = context.openFileInput("tasks.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val json = reader.readText()
            val type = object : TypeToken<List<Task>>() {}.type
            gson.fromJson(json, type)
        } catch (e: FileNotFoundException) {
            emptyList()
        }
    }

}
