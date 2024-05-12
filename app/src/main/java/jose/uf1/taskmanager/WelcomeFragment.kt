package jose.uf1.taskmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import jose.uf1.taskmanager.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        val btnStart = binding.btnStart
        btnStart.setOnClickListener{
            view.findNavController().navigate(R.id.action_welcomeFragment_to_choiceFragment)
        }
        return view
    }
}