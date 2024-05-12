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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jose.uf1.taskmanager.databinding.FragmentShowNotesBinding
import jose.uf1.taskmanager.databinding.FragmentShowTaskBinding
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.Date
import java.util.Locale

class ShowNotesFragment : Fragment() {
    private lateinit var binding: FragmentShowNotesBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowNotesBinding.inflate(inflater, container, false)
        val view = binding.root
        val btnHome = binding.fab
        btnHome.setOnClickListener{
            view.findNavController().navigate(R.id.action_showNotesFragment_to_choiceFragment)
        }

        val textViewTitle = binding.textViewTitle
        val textViewDescription = binding.textViewDescription
        val textViewCreationDate = binding.textViewCreationDate
        val buttonDelete = binding.buttonDelete

        sharedViewModel.selectedNote.observe(viewLifecycleOwner) { note ->
            textViewTitle.text = note.title
            textViewDescription.text = note.description
            textViewCreationDate.text =
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                    Date(note.creationDate)
                )
            buttonDelete.setOnClickListener {
                val noteToDelete = sharedViewModel.selectedNote.value
                if (noteToDelete != null) {
                    deleteNote(requireContext(), noteToDelete)
                    Snackbar.make(binding.root, "Nota eliminada correctamente", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
    private fun deleteNote(context: Context, note: Note) {
        val notes = loadNotesFromFile(context).toMutableList()
        notes.remove(note)

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