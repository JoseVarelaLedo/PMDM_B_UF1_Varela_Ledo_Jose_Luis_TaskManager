package jose.uf1.taskmanager

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import jose.uf1.taskmanager.databinding.FragmentTasksBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import kotlin.random.Random


class TasksFragment : Fragment() {
    private lateinit var chipGroup: ChipGroup
    private lateinit var binding: FragmentTasksBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        binding = FragmentTasksBinding.inflate(layoutInflater)
        val view = binding.root
        val btnHome = binding.fab

        btnHome.setOnClickListener{
            view.findNavController().navigate(R.id.action_tasksFragment_to_choiceFragment)
        }
        chipGroup = binding.chipGroup

        val tasks = loadTasksFromFile(requireContext())
        tasks.forEach { task ->
            val chip = Chip(requireContext())
            val randomColor = generateRandomColor()
            val isDarkColor = isDark(randomColor)
            chip.setTextColor(if (isDarkColor) Color.WHITE else Color.BLACK)

            chip.setChipBackgroundColor(generateRandomColorStateList(randomColor))

            chip.text = task.title
            chip.setOnClickListener {
                sharedViewModel.selectTask(task)
                view.findNavController().navigate(R.id.action_tasksFragment_to_showTaskFragment)
            }
            chipGroup.addView(chip)
        }

        return view
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
    private fun generateRandomColorStateList(color: Int): ColorStateList {
        return ColorStateList.valueOf(color)
    }
    private fun generateRandomColor(): Int {
        return Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }
    private fun isDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
}