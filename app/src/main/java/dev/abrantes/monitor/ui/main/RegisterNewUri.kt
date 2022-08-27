package dev.abrantes.monitor.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dev.abrantes.monitor.MonitorApplication
import dev.abrantes.monitor.databinding.FragmentRegisterNewUriBinding

class RegisterNewUri: Fragment() {

    private var _binding: FragmentRegisterNewUriBinding? = null
    private val binding get() = _binding!!


    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((activity?.application as MonitorApplication).database.responseDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterNewUriBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      binding.submitNewUrl.setOnClickListener {
            val action = RegisterNewUriDirections.actionRegisterNewUriToMainFragment()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}