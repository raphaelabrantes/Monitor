package dev.abrantes.monitor.ui.main

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dev.abrantes.monitor.MonitorApplication
import dev.abrantes.monitor.databinding.FragmentRegisterNewUriBinding
import dev.abrantes.monitor.infrastructure.TIME
import kotlinx.coroutines.launch

class RegisterNewUriFragment : Fragment() {

    private var _binding: FragmentRegisterNewUriBinding? = null
    private val binding get() = _binding!!


    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (activity?.application as MonitorApplication).database.responseDao(),
            (activity?.application as MonitorApplication).database.registerUrlDao(),
        application = activity?.application as MonitorApplication
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        binding.httpsSwitch.setOnCheckedChangeListener { _, on ->
            run {
                if (on) {
                    binding.typeConnection.text = "https://"
                } else {
                    binding.typeConnection.text = "http://"
                }

            }
        }
        binding.submitNewUrl.setOnClickListener {
            val uri = binding.typeConnection.text.toString() + binding.healthUrl.text.toString()
            if (checkValidUri(uri)) {
                val repeat: TIME = getRepetition()
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.insertNewRegisterUrl(uri, repeat)
                }
                viewModel.startRequest(uri, repeat)
                val action = RegisterNewUriFragmentDirections.actionRegisterNewUriToMainFragment()
                view.findNavController().navigate(action)
            }
        }
    }

    private fun getRepetition(): TIME {
        when (binding.timer.checkedRadioButtonId) {
            binding.every15Minutes.id -> return TIME.FIFTEEN_MINUTES
            binding.every20Minutes.id -> return TIME.TWENTY_MINUTES
            binding.every30Minutes.id -> return TIME.THIRTY_MINUTES
        }
        return TIME.TWENTY_MINUTES
    }

    private fun checkValidUri(url: String): Boolean {
        if ((url == "http://") || url == "https://") {
            binding.healthUrl.error = "Empty url"
            return false
        }
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            binding.healthUrl.error = "This url is not valid"
            return false
        }
        binding.healthUrl.error = null
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}