package com.ubermensch.ruangamandua.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubermensch.ruangamandua.R
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.databinding.FragmentHomeBinding
import com.ubermensch.ruangamandua.ui.education.ArticleAdapter
import com.ubermensch.ruangamandua.utils.SessionManager

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var articleAdapter: ArticleAdapter

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        val firstName = sessionManager.getUserName().split(" ").firstOrNull() ?: "Pengguna"
        binding.tvGreeting.text = "Halo, $firstName!"

        articleAdapter = ArticleAdapter { article ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToArticleDetailFragment(article.id)
            )
        }
        binding.rvEdukasi.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter
            isNestedScrollingEnabled = false
        }

        binding.cardLaporSekarang.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_reportFragment)
        }
        binding.tvLihatSemua.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_educationFragment)
        }

        viewModel.loadDashboardData(sessionManager.getUserId())
        viewModel.laporanProses.observe(viewLifecycleOwner)  { binding.tvLaporanProses.text = "$it Laporan" }
        viewModel.laporanSelesai.observe(viewLifecycleOwner) { binding.tvLaporanSelesai.text = "$it Laporan" }
        viewModel.articles.observe(viewLifecycleOwner) { articleAdapter.submitList(it.take(3)) }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}