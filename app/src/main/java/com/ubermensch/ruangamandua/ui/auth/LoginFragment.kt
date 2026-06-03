package com.ruangaman.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ruangaman.app.R
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ruangaman.app.databinding.FragmentLoginBinding
import com.ruangaman.app.ui.dashboard.MainActivity
import com.ruangaman.app.utils.SessionManager

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        binding.btnMasuk.setOnClickListener {
            viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
        binding.tvDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.tvLupaKataSandi.setOnClickListener {
            Snackbar.make(binding.root, "Fitur lupa kata sandi segera hadir", Snackbar.LENGTH_SHORT).show()
        }

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> { binding.btnMasuk.isEnabled = false; binding.progressBar.visibility = View.VISIBLE }
                is AuthState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    sessionManager.saveLoginSession(state.user.id, state.user.namaLengkap, state.user.email, state.user.namaInstansi)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                is AuthState.Error -> {
                    binding.btnMasuk.isEnabled = true; binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                else -> { binding.btnMasuk.isEnabled = true; binding.progressBar.visibility = View.GONE }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}