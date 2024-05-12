package jose.uf1.taskmanager

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jose.uf1.taskmanager.databinding.FragmentNewNoteBinding
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

class NewNoteFragment : Fragment() {
    private lateinit var binding: FragmentNewNoteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        val view = binding.root
        val btnHome = binding.fab
        btnHome.setOnClickListener{
            view.findNavController().navigate(R.id.action_newNoteFragment_to_choiceFragment)
        }

        binding.confirmButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val newNote = Note(title, description)

                saveNoteToFile(requireContext(), newNote)
                Snackbar.make(binding.root, "Nota guardada correctamente", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Por favor, completa todos los campos y selecciona una fecha", Snackbar.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun saveNoteToFile(context: Context, newNote: Note) {
        val notes = loadNotesFromFile(context).toMutableList()
        notes.add(newNote)
        val gson = Gson()
        val json = gson.toJson(notes)
        context.openFileOutput("notes.json", Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
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
}