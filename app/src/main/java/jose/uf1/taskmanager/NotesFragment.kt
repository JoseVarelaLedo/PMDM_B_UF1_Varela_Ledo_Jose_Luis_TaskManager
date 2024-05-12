package jose.uf1.taskmanager

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jose.uf1.taskmanager.databinding.FragmentNotesBinding
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import kotlin.random.Random
import android.content.res.ColorStateList


class NotesFragment : Fragment() {
    private lateinit var chipGroup: ChipGroup
    private lateinit var binding: FragmentNotesBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(layoutInflater)
        val view = binding.root
        chipGroup = binding.chipGroup
        val btnHome = binding.fab
        btnHome.setOnClickListener{
            view.findNavController().navigate(R.id.action_notesFragment_to_choiceFragment)
        }

        val notes = loadNotesFromFile(requireContext())
        notes.forEach { note ->
            val chip = Chip(requireContext())
            val randomColor = generateRandomColor()
            val isDarkColor = isDark(randomColor)
            chip.setTextColor(if (isDarkColor) Color.WHITE else Color.BLACK)
            chip.setChipBackgroundColor(generateRandomColorStateList(randomColor))

            chip.text = note.title
            chip.setOnClickListener {
                sharedViewModel.selectNote(note)
                view.findNavController().navigate(R.id.action_notesFragment_to_showNotesFragment)
            }
            chipGroup.addView(chip)
        }
        return view
    }
    private fun loadNotesFromFile(context: Context): List<Note> {
        val gson = Gson()
        return try {
            val inputStream = context.openFileInput("notes.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val json = reader.readText()
            val type = object : TypeToken<List<Note>>() {}.type
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