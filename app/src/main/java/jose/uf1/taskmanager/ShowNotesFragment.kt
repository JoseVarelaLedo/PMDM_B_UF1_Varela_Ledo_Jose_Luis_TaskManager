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
    private var isEditing = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowNotesBinding.inflate(inflater, container, false)

        val view = binding.root
        val textViewTitle = binding.textViewTitle
        val textViewDescription = binding.textViewDescription
        val textViewCreationDate = binding.textViewCreationDate
        val btnHome = binding.fab
        val buttonDelete = binding.buttonDelete
        val buttonEdit = binding.buttonEdit
        val buttonConfirmEdit = binding.buttonConfirmEdit
        val editTextDescription = binding.editTextDescription
        buttonConfirmEdit.visibility = View.GONE

        btnHome.setOnClickListener{
            view.findNavController().navigate(R.id.action_showNotesFragment_to_choiceFragment)
        }

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
                    val snackBar = Snackbar.make(binding.root, "Nota eliminada correctamente", Snackbar.LENGTH_SHORT)
                    snackBar.setAnchorView(btnHome)
                    snackBar.show()
                    view.findNavController().navigate(R.id.action_showNotesFragment_to_notesFragment)
                }
            }
            buttonEdit.setOnClickListener {
                isEditing = true
                buttonEdit.visibility = View.GONE
                editTextDescription.visibility = View.VISIBLE
                textViewDescription.visibility = View.GONE
                buttonDelete.visibility = View.INVISIBLE
                buttonConfirmEdit.visibility = View.VISIBLE
                editTextDescription.setText(note.description)
            }
            buttonConfirmEdit.setOnClickListener {
                isEditing = false
                val updatedDescription = editTextDescription.text.toString()
                note.description = updatedDescription
                updateNote(requireContext(), note)
                editTextDescription.visibility = View.GONE
                textViewDescription.visibility = View.VISIBLE
                buttonDelete.visibility = View.VISIBLE
                buttonConfirmEdit.visibility = View.GONE
                buttonEdit.visibility = View.VISIBLE
            }
        }
        return view
    }
    private fun updateNote(context: Context, note: Note) {
        val notes = loadNotesFromFile(context).toMutableList()
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
            val gson = Gson()
            val json = gson.toJson(notes)
            context.openFileOutput("notes.json", Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        }
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