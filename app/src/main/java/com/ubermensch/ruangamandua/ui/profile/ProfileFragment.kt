package com.ruangaman.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ruangaman.app.R
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ruangaman.app.databinding.FragmentProfileBinding
import com.ruangaman.app.ui.auth.AuthActivity
import com.ruangaman.app.utils.SessionManager

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        binding.tvNama.text     = sessionManager.getUserName()
        binding.tvInstansi.text = sessionManager.getUserInstansi()

        viewModel.loadProfile(sessionManager.getUserId())
        viewModel.laporanSelesai.observe(viewLifecycleOwner) { binding.tvLaporanSelesai.text = it.toString() }
        viewModel.artikelDisimpan.observe(viewLifecycleOwner) { binding.tvArtikelDisimpan.text = it.toString() }

        binding.menuLaporanSaya.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_reportListFragment)
        }
        binding.menuArtikelDisimpan.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_savedArticlesFragment)
        }
        binding.menuPengaturan.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.btnKeluar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Keluar")
                .setMessage("Yakin ingin keluar dari akun?")
                .setPositiveButton("Keluar") { _, _ ->
                    sessionManager.logout()
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Batal", null).show()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}