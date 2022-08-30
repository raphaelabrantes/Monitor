package dev.abrantes.monitor.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.abrantes.monitor.MonitorApplication
import dev.abrantes.monitor.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!


    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (activity?.application as MonitorApplication).database.responseDao(),
            (activity?.application as MonitorApplication).database.registerUrlDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerMain
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val adapter = MainGridAdapter {
            lifecycle.coroutineScope.launch {
                viewModel.deleteRegisterUrl(registerUrl = it)
            }
        }
        binding.addHealthCheck.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToRegisterNewUri()
            view.findNavController().navigate(action)
        }
        lifecycle.coroutineScope.launch{
            viewModel.getAllRegisterUrl().collect{
                adapter.submitList(it)
            }
        }
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}