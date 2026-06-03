package com.ubermensch.ruangamandua.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.databinding.FragmentRegisterBinding
import com.ubermensch.ruangamandua.ui.dashboard.MainActivity
import com.ubermensch.ruangamandua.utils.SessionManager

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        binding.btnDaftar.setOnClickListener {
            viewModel.register(
                binding.etNamaLengkap.text.toString(),
                binding.etEmail.text.toString(),
                binding.etInstansi.text.toString(),
                binding.etPassword.text.toString(),
                binding.etKonfirmasiPassword.text.toString(),
                binding.cbTerms.isChecked
            )
        }
        binding.tvMasuk.setOnClickListener { findNavController().navigateUp() }

        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> { binding.btnDaftar.isEnabled = false; binding.progressBar.visibility = View.VISIBLE }
                is AuthState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    sessionManager.saveLoginSession(state.user.id, state.user.namaLengkap, state.user.email, state.user.namaInstansi)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                is AuthState.Error -> {
                    binding.btnDaftar.isEnabled = true; binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                else -> { binding.btnDaftar.isEnabled = true; binding.progressBar.visibility = View.GONE }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}