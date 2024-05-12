package jose.uf1.taskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import jose.uf1.taskmanager.databinding.FragmentChoiceBinding

class ChoiceFragment : Fragment() {
    private lateinit var binding : FragmentChoiceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoiceBinding.inflate(layoutInflater)
        val view = binding.root
        val btnTasks = binding.btnTasks
        btnTasks.setOnClickListener{
            view.findNavController().navigate(R.id.action_choiceFragment_to_tasksFragment2)
        }
        val btnNotes = binding.btnNotes
        btnNotes.setOnClickListener{
            view.findNavController().navigate(R.id.action_choiceFragment_to_notesFragment)
        }
        val btnNext = binding.btnNext
        btnNext.setOnClickListener {
            val choiceGroup = binding.choicegroup
            val radioTask = binding.radioTask
            val radioNote = binding.radioNote
            val choiceBtn = choiceGroup.checkedRadioButtonId
            if (choiceBtn == -1){
                val msg = "Debes seleccionar una operación"
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
            }
            if (radioTask.isChecked){
                view.findNavController().navigate(R.id.action_choiceFragment_to_newTaskFragment)
            }
            if (radioNote.isChecked){
                view.findNavController().navigate(R.id.action_choiceFragment_to_newNoteFragment)
            }
        }
        return view
    }
}