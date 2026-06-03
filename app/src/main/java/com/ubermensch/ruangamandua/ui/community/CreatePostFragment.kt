package com.ruangaman.app.ui.community

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ruangaman.app.databinding.FragmentCreatePostBinding
import com.ruangaman.app.utils.SessionManager

class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: CommunityViewModel by viewModels {
        CommunityViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        val topics = listOf("Peer Support", "Healing Stories", "Counselor Q&A", "Tips & Trik")
        binding.spinnerTopic.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, topics)

        binding.ivBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnPostkan.setOnClickListener {
            val topic = binding.spinnerTopic.selectedItem.toString()
            viewModel.createPost(
                userId     = sessionManager.getUserId(),
                authorName = sessionManager.getUserName(),
                judul      = binding.etJudul.text.toString(),
                konten     = binding.etKonten.text.toString(),
                topic      = topic,
                isAnonim   = binding.switchAnonim.isChecked
            )
        }

        viewModel.postState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PostState.Loading -> binding.btnPostkan.isEnabled = false
                is PostState.Success -> {
                    Snackbar.make(binding.root, "Post berhasil dibuat!", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                is PostState.Error -> {
                    binding.btnPostkan.isEnabled = true
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                else -> binding.btnPostkan.isEnabled = true
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}