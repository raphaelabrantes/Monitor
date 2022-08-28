package dev.abrantes.monitor.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dev.abrantes.monitor.MonitorApplication
import dev.abrantes.monitor.databinding.FragmentRegisterNewUriBinding
import dev.abrantes.monitor.infrastructure.TIME
import kotlinx.coroutines.launch

class RegisterNewUri : Fragment() {

    private var _binding: FragmentRegisterNewUriBinding? = null
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
        _binding = FragmentRegisterNewUriBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitNewUrl.setOnClickListener {
            val uri = binding.healthUrl.text.toString()
            var schema = ""
            if (uri.isNotEmpty() && !(uri.startsWith("http://", true) || uri.startsWith("https://", true))) {
                schema = if (binding.httpsSwitch.isActivated) "https://" else "http://"
            }
            if (checkValidUri(schema + uri)) {
                val repeat: TIME = getRepetition()
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.insertNewRegisterUrl(schema + uri, repeat)
                }
                val action = RegisterNewUriDirections.actionRegisterNewUriToMainFragment()
                view.findNavController().navigate(action)
            }
        }
    }

    private fun getRepetition(): TIME {
        when (binding.timer.checkedRadioButtonId) {
            binding.everySecond.id -> return TIME.ONE_SECOND
            binding.everyMinute.id -> return TIME.ONE_MINUTE
            binding.every5Minutes.id -> return TIME.FIVE_MINUTE
        }
        return TIME.ONE_MINUTE
    }

    private fun checkValidUri(uri: String): Boolean {
        if (uri.isEmpty()) {
            binding.healthUrl.error = "Empty url"
            return false
        }
        if (!URLUtil.isValidUrl(uri)) {
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