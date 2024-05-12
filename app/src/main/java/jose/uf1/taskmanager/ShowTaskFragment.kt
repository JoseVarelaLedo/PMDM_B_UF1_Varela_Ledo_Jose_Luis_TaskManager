package jose.uf1.taskmanager

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import jose.uf1.taskmanager.databinding.FragmentShowTaskBinding
import java.util.Date
import java.util.Locale
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

class ShowTaskFragment : Fragment() {
    private lateinit var binding: FragmentShowTaskBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        val btnHome = binding.fab
        btnHome.setOnClickListener{
            view.findNavController().navigate(R.id.action_showTaskFragment_to_choiceFragment)
        }

        val textViewTitle = binding.textViewTitle
        val textViewDescription = binding.textViewDescription
        val textViewCreationDate = binding.textViewCreationDate
        val textViewDeadline = binding.textViewDeadline
        val buttonDelete = binding.buttonDelete

        sharedViewModel.selectedTask.observe(viewLifecycleOwner) { task ->
            textViewTitle.text = task.title
            textViewDescription.text = task.description
            textViewCreationDate.text =
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                    Date(task.creationDate)
                )
            textViewDeadline.text = SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
            ).format(Date(task.deadline))
            buttonDelete.setOnClickListener {
                val taskToDelete = sharedViewModel.selectedTask.value
                if (taskToDelete != null) {
                    deleteTask(requireContext(), taskToDelete)
                    Snackbar.make(binding.root, "Tarea eliminada correctamente", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
    private fun deleteTask(context: Context, task: Task) {
        val tasks = loadTasksFromFile(context).toMutableList()
        tasks.remove(task)

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

